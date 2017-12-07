(function ($) {

    var messageForm = document.querySelector('#messageForm');
    var messageInput = document.querySelector('#message');
    var messageArea = document.querySelector('#messageArea');
    var channelsArea = document.querySelector("#joinchannel");
    var leaveChannelArea = document.querySelector("#leaveChannel");
    var channelInput = document.querySelector("#createChannel");
    var createChannelArea = document.querySelector("#createChannelForm");

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
            url: '/user/rooms',
            data: 'json',
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
        var currentUserList = $(".users");
        currentUserList.empty();
        currentUserList.append(userList[index]); //userList is empty; no users appended to div
    }

    function loadUsers(channelName) {
        var ul = document.createElement('ul');
        $.ajax({
            type: 'GET',
            url: '/user/room/' + channelName,
            data: 'json',
            success: function (users) {
                for (var i = 0; i < users.length; i++) {
                    var messageElement = document.createElement('li');
                    var currentUser = users[i];
                    appendUserAvatar(currentUser.username, messageElement);
                    var image = currentUser.image;
                    ul.appendChild(messageElement);
                    ul.scrollTop = ul.scrollHeight;
                }
                var channelName = $('.current').text();
                var index = channelsList.indexOf(channelName);
                userList.splice(index, 0, ul);
            }
        });
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
            showChannel(channelName);

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
            loadUsers(currentRoom);
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

    function appendUserAvatar(user, messageElement) {
        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(user[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(user);

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
            appendUserAvatar(message.user, messageElement);
        }

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.message);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);

        var currentChannelUl = phrasesList[channelsList.indexOf(message.roomName)];
        currentChannelUl.appendChild(messageElement);
        currentChannelUl.scrollTop = currentChannelUl.scrollHeight;

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
            url: '/rooms/all',
            data: 'json',
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

    leaveChannelArea.addEventListener("click", leaveChannel, true);
    channelsArea.addEventListener("click", loadChannels, true);
    messageForm.addEventListener('submit', sendMessage, true);
    createChannelArea.addEventListener('submit', createNewChannel, true);

})
(jQuery)



