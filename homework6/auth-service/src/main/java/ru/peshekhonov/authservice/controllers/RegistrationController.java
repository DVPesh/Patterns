package ru.peshekhonov.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.peshekhonov.api.dto.JwtResponse;
import ru.peshekhonov.api.dto.VisitorRegistrationDto;
import ru.peshekhonov.api.exceptions.AppError;
import ru.peshekhonov.authservice.services.VisitorService;
import ru.peshekhonov.authservice.utils.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final VisitorService visitorService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public ResponseEntity<?> registerNewUser(@RequestBody VisitorRegistrationDto visitor) {
        if (visitor.getUsername() == null || visitor.getUsername().isBlank()) {
            return new ResponseEntity<>(
                    new AppError("USER_DATA_ERROR", "Имя пользователя не задано"), HttpStatus.BAD_REQUEST);
        }
        if (!visitor.getUsername().matches("^[a-zA-Z0-9]+$")) {
            return new ResponseEntity<>(
                    new AppError("USER_DATA_ERROR", "В имени пользователя допустимы только латиница и цифры"),
                    HttpStatus.BAD_REQUEST);
        }
        if (visitor.getPassword() == null || visitor.getPassword().isBlank()) {
            return new ResponseEntity<>(
                    new AppError("USER_DATA_ERROR", "Пароль не задан"), HttpStatus.BAD_REQUEST);
        }
        if (visitor.getEmail() != null && visitor.getEmail().isBlank()) {
            visitor.setEmail(null);
        }
        if (visitorService.existsByUsername(visitor.getUsername())) {
            return new ResponseEntity<>(
                    new AppError("USER_DATA_ERROR", "Такое имя пользователя уже существует"), HttpStatus.BAD_REQUEST);
        }

        if (visitor.getEmail() != null && visitorService.existsByEmail(visitor.getEmail())) {
            return new ResponseEntity<>(
                    new AppError("USER_DATA_ERROR", "Такой Email уже есть"), HttpStatus.BAD_REQUEST);
        }

        visitorService.createUser(visitor);

        UserDetails userDetails = visitorService.loadUserByUsername(visitor.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token, visitorService.getUserRoleNames(visitor.getUsername())));
    }
}
