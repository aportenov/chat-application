(function ($) {

    var messageForm = document.querySelector('#messageForm');
    var messageInput = document.querySelector('#message');
    var messageArea = document.querySelector('#messageArea');

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
                    joinRoom(currentRoom);
                }
            }
        });
    }

    function subscribeChannel(currentRoom) {
        stompClient.subscribe('/topic/room/' + currentRoom, onMessageReceived);
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

        stompClient.send("/addUser/" + currentRoom, {}, JSON.stringify(room));
    }


    function showChannel(channelName) {
        var index = channelsList.indexOf(channelName);
        channels.find('.current ').removeClass('current');
        channels.find('.channel:eq(' + index + ')').addClass('current');
        // var currentWindowUlLenght = phrasesList[index].innerHTML.length;
        // if (currentWindowUlLenght > 0) {
            $("#messageArea").innerHTML = "";
            var a = 0;
        // }
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
        showChannel(channelName);
    }

// function leaveChannel(channelName) {
//     if (channels[channelName]) {
//         delete channels[channelName];
//         var index = channelsList.indexOf(channelName);
//         channelsList.splice(index, 1);
//         if (channelName === currentChannel) {
//             var nextChannel = channelsList[index];
//             if (!nextChannel) {
//                 index = index - 1;
//                 nextChannel = channelsList[index];
//             }
//             if (nextChannel) {
//                 channels.find('.channel:eq(' + index + ')').addClass('current');
//                 showChannel(nextChannel);
//             } else {
//                 clearPhrases();
//             }
//         }
//     } else {
//         alert('Channel "' + channelName + '" doesn\'t exists!');
//     }
// }

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

    function loadData(user, messageElement) {
        messageElement.classList.add('chat-message');

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

//
    function onMessageReceived(payload) {
        var message = JSON.parse(payload.body);
        var messageElement = document.createElement('li');

        if (message.messageType === 'JOIN') {
            messageElement.classList.add('event-message');
            message.message = message.user + ' joined!';
        } else if (message.messageType === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.message = message.user + ' left!';
        } else {
            loadData(message.user, messageElement);
        }

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.message);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);

        var currentChannelUl = phrasesList[channelsList.indexOf(message.roomName)];
        currentChannelUl.appendChild(messageElement);
        currentChannelUl.scrollTop = currentChannelUl.scrollHeight;

        var currentChannel = $(".current").text();
        if (currentChannel === message.roomName) {

            messageArea.appendChild(messageElement);
            messageArea.scrollTop = messageArea.scrollHeight;
        }

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

    messageForm.addEventListener('submit', sendMessage, true);
})
(jQuery)

