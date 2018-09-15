package com.example.sweater2.controller;

import com.example.sweater2.domain.User;
import com.example.sweater2.domain.dto.CaptchaResponseDto;
import com.example.sweater2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private static final String CAPTCHA_URL =
            "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secretkey}")
    private String recaptchaSecretKey;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String add(
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @RequestParam("password2") String passwordConfirmation,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {

        String url = String.format(CAPTCHA_URL, recaptchaSecretKey,captchaResponse);

        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if(!response.isSuccess()) {
            model.addAttribute("captchaError","Captcha unsolved");
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirmation);
        if(isConfirmEmpty)
            model.addAttribute("password2Error","Password confirmation cannot be empty");

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirmation)) {
            model.addAttribute("passwordError", "Passwords are different");
            return "registration";
        }

        if(isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "/registration";
        }

        if(!userService.addUser(user)) {
            model.addAttribute("usernameError","User already exist");
            return "/registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if(isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType","danger");
            model.addAttribute("message", "Activation code is not found");
        }

        return "/login";
    }

}
