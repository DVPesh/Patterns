package ru.peshekhonov.core.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.peshekhonov.api.dto.StringResponse;
import ru.peshekhonov.api.exceptions.AppError;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ProfileController {

    @GetMapping
    public ResponseEntity<?> confirmTheAdminRole(@RequestHeader String username, @RequestHeader String roles) {
        if (roles.contains("ROLE_ADMIN")) {
            return ResponseEntity.ok(new StringResponse("admin"));
        }
        return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.toString(), "Нет прав для доступа к ресурсу"),
                HttpStatus.FORBIDDEN);
    }
}
