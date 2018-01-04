(function ($) {

    var messageForm = document.querySelector('#messageForm'),
        messageInput = document.querySelector('#message'),
        messageArea = document.querySelector('#messageArea'),
        channelsArea = document.querySelector("#joinchannel"),
        leaveChannelArea = document.querySelector("#leaveChannel"),
        channelInput = document.querySelector("#createChannel"),
        createChannelArea = document.querySelector("#createChannelForm"),
        currentUserList = document.querySelector(".users"),
        phrasesContainer = document.querySelector(".phrases-container");

    var tubeRegExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/,
        imageRegExp = /(https?:\/\/.*\.(?:png|jpg))/,
        urlRegExp = /[-a-zA-Z0-9@:%_\+.~#?&//=]{2,256}\.[a-z]{2,4}\b(\/[-a-zA-Z0-9@:%_\+.~#?&//=]*)?/gi;

    var colors = [
        '#2196F3', '#32c787', '#00BCD4', '#ff5652',
        '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
    ];

    var channels = {},
        stompClient = null,
        roomName = null,
        username = null,
        channelsList = [],
        currentChannel = null,
        phrasesList = [],
        userList = [],
        subscriptionList = [],
        channels = $('.channels');


    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);


    function onConnected() {
        loadRooms();
    }


    function loadRooms() {
        $.ajax({
            type: 'GET',
            url: '/users/rooms',
            contentType: "application/json",
            success: function (rooms) {
                for (var i = 0; i < rooms.length; i++) {
                    var currentRoom = rooms[i];
                    subscribeChannel(currentRoom);
                    createChannel(currentRoom);
                    joinChannel(currentRoom);
                }
            }
        });
    }

    function subscribeChannel(currentRoom) {
        var currentSubscription = stompClient.subscribe('/topic/room/' + currentRoom, onMessageReceived);
        subscriptionList.push(currentSubscription);
    }

    function unSubscribeChannel(roomIndex) {
        var currentSubscription = subscriptionList[roomIndex];
        currentSubscription.unsubscribe();
        subscriptionList.splice(roomIndex, 1);
    }

    function createChannel() {
        var ul = document.createElement('ul');
        ul.setAttribute("id", "messageArea");
        phrasesList.push(ul);
    }

    function joinRoom(currentRoom) {
        var room = {
            user: username,
            messageType: 'JOIN',
            roomName: currentRoom
        };

        stompClient.send("/addRoom/" + currentRoom, {}, JSON.stringify(room));
    }

    function leaveRoom(currentRoom) {
        var room = {
            user: username,
            messageType: 'LEAVE',
            roomName: currentRoom
        };

        stompClient.send("/leaveRoom/" + currentRoom, {}, JSON.stringify(room));
    }


    function showChannel(channelName) {
        var index = channelsList.indexOf(channelName);
        channels.find('.current ').removeClass('current');
        channels.find('.channel:eq(' + index + ')').addClass('current');
        var currentPhrases = $(".phrases-container");
        currentPhrases.empty();
        currentPhrases.append(phrasesList[index]);
        currentUserList.empty();
        currentUserList.append(userList[index]); //userList is empty; no users appended to div
    }

    function createNewChannel() {
        var roomName = channelInput.value.trim();
        if (roomName) {
            subscribeChannel(roomName);
            createChannel(roomName);
            joinChannel(roomName);
        }
    }

    function joinChannel(channelName) {
        if (!channels[channelName]) {
            channelsList.push(channelName);
            var channel = $('<div class="channel current">' + channelName + '</div>')
                .on('click', function () {
                    showChannel(channelName);
                });
            channels.append(channel);

        }
        joinRoom(channelName);
        loadUsers(channelName);
        showChannel(channelName);
    }

    function leaveChannel() {
        var currentRoom = $('.current').text();
        var index = channelsList.indexOf(currentRoom);
        currentChannel = channels.find('.current ');
        if (currentChannel && (channelsList[index] === currentRoom)) {
            delete channels.find('.current ');
            $('.current ').remove();
            channelsList.splice(index, 1);
            phrasesList.splice(index, 1);
            userList.splice(index, 1);
            leaveRoom(currentRoom)
            unSubscribeChannel(index);
        }

        if (channelsList.length > 0) {
            showChannel(channelsList[0]);
        }
    }

    function sendMessage(event) {
        var messageContent = messageInput.value.trim();
        currentChannel = $('.current').text();
        if (messageContent && stompClient) {
            var chatMessage = {
                user: username,
                message: messageInput.value,
                messageType: 'CHAT',
                roomName: currentChannel
            };

            stompClient.send("/sendMessage/" + currentChannel, {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
        event.preventDefault();
    }

    function appendUserAvatar(user, image, messageElement) {
        var avatarElement = document.createElement('i');
        if (image) {
            var avatarImage = document.createElement('img');
            avatarImage.src = image;
            avatarImage.className = "img-circle";
            avatarElement.appendChild(avatarImage);
        } else {
            var avatarText = document.createTextNode(user[0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(user);
        }

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(user);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);

    }


    function onMessageReceived(payload) {
        var message = JSON.parse(payload.body);
        var messageElement = document.createElement('li');

        if (message.messageType === 'JOIN') {
            messageElement.classList.add('event-message');
        } else if (message.messageType === 'LEAVE') {
            messageElement.classList.add('event-message');
        } else {
            messageElement.classList.add('chat-message');
            appendUserAvatar(message.user, message.image, messageElement);
        }

        var textElement = document.createElement('p');
        var currentMessage,
            embedCode;

        var matchTube = message.message.match(tubeRegExp),
            matchImg = imageRegExp.test(message.message);

        if (matchTube && matchTube[2].length == 11) {
            embedCode = matchTube[2];
            currentMessage = document.createElement('iframe');
            currentMessage.setAttribute("width", "300");
            currentMessage.setAttribute("height", "200");
            currentMessage.setAttribute("src", "//www.youtube.com/embed/" + embedCode + "?rel=0");
            currentMessage.setAttribute("frameborder", "0");
            currentMessage.setAttribute("allowFullScreen", "");
        } else if (matchImg) {
            currentMessage = document.createElement('img');
            currentMessage.setAttribute("src", message.message);
            currentMessage.setAttribute("width", "300");
            currentMessage.setAttribute("height", "200");
        } else if (urlRegExp.test(message.message)) {
           currentMessage = document.createElement("a");
           currentMessage.setAttribute("href", message.message);
           currentMessage.setAttribute("target","_blank");
           currentMessage.innerHTML = message.message;
        } else {
            currentMessage = document.createTextNode(message.message);
        }

        textElement.appendChild(currentMessage);
        messageElement.appendChild(textElement);

        var currentChannelUl = phrasesList[channelsList.indexOf(message.roomName)];
        currentChannelUl.appendChild(messageElement);
        phrasesContainer.scrollTop = phrasesContainer.scrollHeight;

    }


    function getAvatarColor(messageSender) {
        var hash = 0;
        for (var i = 0; i < messageSender.length; i++) {
            hash = 31 * hash + messageSender.charCodeAt(i);
        }

        var index = Math.abs(hash % colors.length);
        return colors[index];
    }

    function onError(error) {
        console.log('Could not connect to WebSocket server. Please refresh this page to try again!');
    }

    function loadChannels() {
        $.ajax({
            type: 'GET',
            url: '/room/all',
            contentType: "application/json",
            success: function (rooms) {
                $(".modal-body").empty();
                for (var i = 0; i < rooms.length; i++) {
                    var currentRoom = rooms[i];
                    $(".modal-body").append(
                        $("<div></div>").append(
                            $("<input></input>")
                                .addClass("btn btn-info")
                                .text(currentRoom)
                                .val(currentRoom))
                            .on('click', function (room) {
                                var target = $(room.target);
                                target.unbind("click");
                                target.removeClass("btn btn-info").addClass("btn btn-default");
                                var roomName = target.text();
                                subscribeChannel(roomName);
                                createChannel(roomName);
                                joinChannel(roomName);
                            }));
                }
            }
        });
    }

    function loadUsers(channelName) {
        var ul = document.createElement('ul');
        $.ajax({
            type: 'GET',
            url: '/user/room/' + channelName,
            contentType: "application/json",
            success: function (users) {
                for (var i = 0; i < users.length; i++) {
                    var messageElement = document.createElement('li');
                    var currentUser = users[i];
                    appendUserAvatar(currentUser.username, currentUser.image, messageElement);
                    var image = currentUser.image;
                    ul.appendChild(messageElement);
                    ul.scrollTop = ul.scrollHeight;

                }
                var channelName = $('.current').text();
                var index = channelsList.indexOf(channelName);
                userList.splice(index, 0, ul);
                currentUserList.append(userList[index]);
            }
        });
    }

    leaveChannelArea.addEventListener("click", leaveChannel, true);
    channelsArea.addEventListener("click", loadChannels, true);
    messageForm.addEventListener('submit', sendMessage, true);
    createChannelArea.addEventListener('submit', createNewChannel, true);

})
(jQuery)



