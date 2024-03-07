package com.octane.rrs.controller;

import com.octane.rrs.model.User;
import com.octane.rrs.model.auth.AuthenticationRequest;
import com.octane.rrs.model.auth.AuthenticationResponse;
import com.octane.rrs.model.auth.RegisterRequest;
import com.octane.rrs.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        logger.info("register()>> email: {}",request.getEmail());
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        logger.info("authenticate()>> email: {}",request.getEmail());
        return ResponseEntity.ok(service.authenticate(request));
    }


}
