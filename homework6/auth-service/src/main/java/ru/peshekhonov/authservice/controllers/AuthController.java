package ru.peshekhonov.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.peshekhonov.api.dto.JwtRequest;
import ru.peshekhonov.api.dto.JwtResponse;
import ru.peshekhonov.api.exceptions.AppError;
import ru.peshekhonov.authservice.services.VisitorService;
import ru.peshekhonov.authservice.utils.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final VisitorService visitorService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        UserDetails userDetails = visitorService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .roles(visitorService.getUserRoleNames(authRequest.getUsername()))
                .build();
        return ResponseEntity.ok(jwtResponse);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleBadCredentialsException(BadCredentialsException e) {
        AppError appError = AppError.builder()
                .code("CHECK_TOKEN_ERROR")
                .error("Некорректный логин или пароль")
                .build();
        return new ResponseEntity<>(appError, HttpStatus.UNAUTHORIZED);
    }
}
