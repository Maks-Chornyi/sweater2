package com.example.sweater2.controller;

import com.example.sweater2.domain.Message;
import com.example.sweater2.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/")
    public String greeting(Map<String,Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String,Object> model) {
        Iterable<Message> messages = messageRepository.findAll();

        model.put("messages", messages);

        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String text,
                      @RequestParam String tag,
                      Map<String,Object> model) {
       Message message = new Message(text,tag);

       messageRepository.save(message);

       Iterable<Message> messages = messageRepository.findAll();

       model.put("messages",messages);

        return "main";

    }

    @PostMapping("filter")
    public String filter(@RequestParam(name = "filter") String text, Map<String, Object> model) {
        Iterable<Message> messages;
        if(text!=null && !text.isEmpty())
            messages = messageRepository.findByTag(text);
        else
            messages = messageRepository.findAll();

        model.put("messages", messages);

        return "main";
    }
}