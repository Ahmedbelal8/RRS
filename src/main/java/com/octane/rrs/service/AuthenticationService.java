package com.octane.rrs.service;

import com.octane.rrs.exception.CustomException;
import com.octane.rrs.exception.ErrorCode;
import com.octane.rrs.model.User;
import com.octane.rrs.model.auth.AuthenticationRequest;
import com.octane.rrs.model.auth.AuthenticationResponse;
import com.octane.rrs.model.auth.RegisterRequest;
import com.octane.rrs.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User register(RegisterRequest request) {

        if (userService.checkIfUserExist(request.getEmail())) {
            logger.debug("register()>> this email : {} already registered", request.getEmail());
            throw new CustomException(ErrorCode.USER_EXIST);
        }
        User user = userService.createUser(request);
        return user;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException error) {
            logger.debug("authenticate()>> BAD CREDENTIALS, user email: {}", request.getEmail());
            throw new CustomException(ErrorCode.BAD_CREDENTIALS);
        }
        User user = userService.getUserByEmail(request.getEmail());
        logger.debug("authenticate()>> user id: {}", user.getUserId());
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

}
