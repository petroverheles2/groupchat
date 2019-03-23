package com.virtuace.groupchat.controller;

import com.virtuace.groupchat.model.ChatMessage;
import com.virtuace.groupchat.model.ChatCounterMessage;
import com.virtuace.groupchat.service.TimeService;
import com.virtuace.groupchat.service.WordsCounterService;
import com.virtuace.groupchat.service.WordsExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ChatController {

    private final WordsExtractorService wordsExtractorService;
    private final TimeService timeService;
    private final WordsCounterService wordsCounterService;

    @Autowired
    public ChatController(WordsExtractorService wordsExtractorService, WordsCounterService wordsCounterService, TimeService timeService) {
        this.wordsExtractorService = wordsExtractorService;
        this.timeService = timeService;
        this.wordsCounterService = wordsCounterService;
    }


    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

        Map<String, Long> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(wordsExtractorService.extract(chatMessage.getContent()));

        ChatCounterMessage chatCounterMessage = new ChatCounterMessage();
        chatCounterMessage.setType(chatMessage.getType());
        chatCounterMessage.setSender(chatMessage.getSender());
        chatCounterMessage.setContent(chatMessage.getContent());
        chatCounterMessage.setCounters(countersUpdate);
        chatCounterMessage.setTimestamp(timeService.getCurrentTimeMillis());

        return chatCounterMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setTimestamp(timeService.getCurrentTimeMillis());

        return chatMessage;
    }

}
