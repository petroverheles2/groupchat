package com.virtuace.groupchat.controller;

import com.virtuace.groupchat.model.ChatMessage;
import com.virtuace.groupchat.service.TimeService;
import com.virtuace.groupchat.service.WordsCounterService;
import com.virtuace.groupchat.service.WordsCounterUpdateService;
import com.virtuace.groupchat.service.WordsExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final WordsExtractorService wordsExtractorService;
    private final TimeService timeService;
    private final WordsCounterService wordsCounterService;
    private final WordsCounterUpdateService wordsCounterUpdateService;

    @Autowired
    public ChatController(WordsExtractorService wordsExtractorService, WordsCounterService wordsCounterService, TimeService timeService, WordsCounterUpdateService wordsCounterUpdateService) {
        this.wordsExtractorService = wordsExtractorService;
        this.timeService = timeService;
        this.wordsCounterService = wordsCounterService;
        this.wordsCounterUpdateService = wordsCounterUpdateService;
    }


    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

        Iterable<String> wordsToIncrement = wordsExtractorService.extract(chatMessage.getContent());
        wordsCounterService.incrementCounters(wordsToIncrement);
        wordsCounterUpdateService.addUpdatedWord(wordsToIncrement);

        return chatMessage;
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
