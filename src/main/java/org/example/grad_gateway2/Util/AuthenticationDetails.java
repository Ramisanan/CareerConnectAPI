package org.example.grad_gateway2.Util;

import jdk.jfr.Category;
import org.example.grad_gateway2.DAO.UserRepository;
import org.example.grad_gateway2.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
public class AuthenticationDetails {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        return Optional.ofNullable(user);
    }
}
