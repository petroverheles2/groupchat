'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var countersArea = document.querySelector('#countersArea');
var chatConnectingElement = document.querySelector('#chatConnecting');
var counterConnectingElement = document.querySelector('#counterConnecting');

var stompClient = null;
var username = null;

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    initWordCounters();

    chatConnectingElement.classList.add('hidden');
    counterConnectingElement.classList.add('hidden');
}


function onError(error) {
    chatConnectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    chatConnectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var timestampElement = document.createElement('span');
        var timestampText = document.createTextNode('[' + new Date(message.timestamp).toLocaleTimeString() + '] ');
        timestampElement.appendChild(timestampText);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);

        messageElement.appendChild(timestampElement);
        messageElement.appendChild(usernameElement);

        $.each(message.counters, function(key, value) {
            var counterValue = document.querySelector("#" + key);
            if (counterValue !== null) {
                counterValue.textContent = value;
            } else {
                appendCounterElement(key, value);
            }
        });
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function initWordCounters () {
    $.get('/current-counters',
        function (data) {
            console.log("init counters: " + JSON.stringify(data));

            $.each(data, function(key, value) {
                console.log(key + ": " + value );
                appendCounterElement(key, value);
            });

            countersArea.scrollTop = messageArea.scrollHeight;
        }
    );
}

function appendCounterElement(key, value) {
    var counterElement = document.createElement('li');

    counterElement.classList.add('chat-message');

    var wordElement = document.createElement('span');
    var wordText = document.createTextNode(key + ': ');
    wordElement.appendChild(wordText);
    counterElement.appendChild(wordElement);

    var valueElement = document.createElement('span');
    valueElement.id = key;
    var valueText = document.createTextNode(value);
    valueElement.appendChild(valueText);
    counterElement.appendChild(valueElement);

    countersArea.appendChild(counterElement);
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)