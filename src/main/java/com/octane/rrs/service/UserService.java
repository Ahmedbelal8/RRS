package com.octane.rrs.service;

import com.octane.rrs.enums.Role;
import com.octane.rrs.exception.CustomException;
import com.octane.rrs.exception.ErrorCode;
import com.octane.rrs.model.User;
import com.octane.rrs.model.auth.RegisterRequest;
import com.octane.rrs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    public User createUser(RegisterRequest request){
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        User savedUser = repository.save(user);
        logger.debug("createUser()>> USER created SUCCESSFULLY, savedUser id: {}",
                savedUser.getUserId());
        return user;
    }
    public boolean checkIfUserExist(String email){
        return repository.findUserByEmail(email).isPresent();
    }

    public User getUserByEmail(String email){
        logger.debug("getUserByEmail()>> email: {}",email);
        return repository.findUserByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}
