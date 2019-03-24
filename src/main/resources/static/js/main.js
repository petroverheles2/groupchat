'use strict';

var messageInput = $('#message');
var messageArea = $('#messageArea');
var countersArea = $('#countersArea');
var chatConnectingElement = $('#chatConnecting');

var stompClient = null;
var username = null;

function connect(event) {
    username = $('#name').val().trim();

    if(username) {
        $('#username-page').addClass('hidden');
        $('#chat-page').removeClass('hidden');

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

    chatConnectingElement.addClass('hidden');
    $('#counterConnecting').addClass('hidden');
}


function onError(error) {
    chatConnectingElement.text('Could not connect to WebSocket server. Please refresh this page to try again!');
    chatConnectingElement.css('color', 'red');
}


function sendMessage(event) {
    var messageText = messageInput.val().trim();
    if(messageText && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageText,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.val('');
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = $('<li></li>');

    var dateString = '[' + new Date(message.timestamp).toLocaleTimeString() + '] ';

    if(message.type === 'JOIN') {
        messageElement.addClass('event-message');
        message.content = dateString + ' ' + message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.addClass('event-message');
        message.content = dateString + ' ' + message.sender + ' left!';
    } else {
        messageElement.addClass('chat-message');

        var timestampElement = $('<span></span>');
        timestampElement.text(dateString);

        var usernameElement = $('<span></span>');
        usernameElement.text(message.sender);

        messageElement.append(timestampElement);
        messageElement.append(usernameElement);

        $.each(message.increments, function(index, value) {
            var counterValue = document.querySelector("#" + value);
            if (counterValue !== null) {
                counterValue.textContent = Number(counterValue.textContent) + 1;
            } else {
                appendCounterElement(value, 1);
            }
        });
    }

    var textElement = $('<p></p>');
    textElement.text(message.content);

    messageElement.append(textElement);

    messageArea.append(messageElement);
    messageArea.scrollTop(messageArea.prop("scrollHeight"));
}

function initWordCounters () {
    $.get('/current-counters',
        function (data) {
            $.each(data, function(key, value) {
                countersArea.append(createCounterElement(key, value));
            });
        }
    );
}

function createCounterElement(key, value) {

    var counterElement = $('<li></li>');
    counterElement.addClass('chat-message');

    var wordElement = $('<span></span>');
    wordElement.text(key + ': ');

    counterElement.append(wordElement);

    var valueElement = $('<span></span>');
    valueElement.attr('id', key);
    valueElement.text(value);

    counterElement.append(valueElement);

    return counterElement;
}

function appendCounterElement(key, value) {
    var counterElement = createCounterElement(key, value);


    if (countersArea.length == 0) {
        countersArea.append(counterElement);
    } else {
        // alphabetical order
        var added = false;
        countersArea.find('li').each(function() {
            var word = $(this).find('span:last').attr('id');

            if (word.localeCompare(key) > 0) {
                $(this).before(counterElement);
                added = true;
                return false;
            }
        });

        if (!added) {
            countersArea.append(counterElement);
        }
    }

}

$('#usernameForm').submit(connect);
$('#messageForm').submit(sendMessage)