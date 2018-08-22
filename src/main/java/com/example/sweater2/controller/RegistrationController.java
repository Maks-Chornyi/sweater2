package com.example.sweater2.controller;

import com.example.sweater2.domain.Role;
import com.example.sweater2.domain.User;
import com.example.sweater2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String add(User user, Map<String, Object> model) {
        User userFromDb= userRepository.findByUsername(user.getUsername());

        if(userFromDb != null) {
            model.put("message","User already exist");
            return "/registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        return "redirect:/login";
    }
}
