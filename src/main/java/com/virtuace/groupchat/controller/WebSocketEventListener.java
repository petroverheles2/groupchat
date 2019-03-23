package com.virtuace.groupchat.controller;

import com.virtuace.groupchat.model.ChatMessage;
import com.virtuace.groupchat.model.MessageType;
import com.virtuace.groupchat.service.TimeService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {

    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private final TimeService timeService;

    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate, TimeService timeService) {
        this.messagingTemplate = messagingTemplate;
        this.timeService = timeService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            log.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(username);
            chatMessage.setTimestamp(timeService.getCurrentTimeMillis());

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
