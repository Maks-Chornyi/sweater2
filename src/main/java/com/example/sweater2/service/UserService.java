package com.example.sweater2.service;

import com.example.sweater2.domain.Role;
import com.example.sweater2.domain.User;
import com.example.sweater2.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MailSender mailSender;

    public UserService(UserRepository userRepository, MailSender mailSender) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        boolean userSaved = false;

        if(userFromDb == null) {
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            user.setActivationCode(UUID.randomUUID().toString());
            userRepository.save(user);

            if(!StringUtils.isEmpty(user.getEmail())) {
                String message = String.format(
                        "Hello, %s! \n" +
                        "Welcome to Sweater. Please, activate your accout: http://localhost:8080/activate/%s",
                        user.getUsername(),
                        user.getActivationCode()
                );
                mailSender.send(user.getEmail(), "Activation code", message);
            }

            userSaved = true;
        }

        return userSaved;
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if(user == null)
            return false;

        user.setActivationCode(null);

        userRepository.save(user);

        return true;
    }
}
