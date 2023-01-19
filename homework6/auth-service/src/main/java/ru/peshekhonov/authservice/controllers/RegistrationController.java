package ru.peshekhonov.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.peshekhonov.api.dto.JwtResponse;
import ru.peshekhonov.api.dto.VisitorRegistrationDto;
import ru.peshekhonov.api.exceptions.AppError;
import ru.peshekhonov.authservice.exceptions.RegistrationException;
import ru.peshekhonov.authservice.services.VisitorService;
import ru.peshekhonov.authservice.utils.JwtTokenUtil;
import ru.peshekhonov.authservice.validation.VisitorRegistrationDtoValidationRulesEngine;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final VisitorService visitorService;
    private final JwtTokenUtil jwtTokenUtil;
    private final VisitorRegistrationDtoValidationRulesEngine validationRulesEngine;

    @PostMapping
    public ResponseEntity<?> registerNewUser(@RequestBody VisitorRegistrationDto visitor) {
        if (visitor.getEmail() != null && visitor.getEmail().isBlank()) {
            visitor.setEmail(null);
        }

        validationRulesEngine.check(visitor);

        visitorService.createUser(visitor);

        UserDetails userDetails = visitorService.loadUserByUsername(visitor.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .roles(visitorService.getUserRoleNames(visitor.getUsername()))
                .build();
        return ResponseEntity.ok(jwtResponse);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleRegistrationException(RegistrationException e) {
        AppError appError = AppError.builder()
                .code("USER_DATA_ERROR")
                .error(e.getMessage())
                .build();
        return new ResponseEntity<>(appError, HttpStatus.BAD_REQUEST);
    }
}
