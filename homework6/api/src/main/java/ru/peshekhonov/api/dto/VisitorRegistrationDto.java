package ru.peshekhonov.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorRegistrationDto {

    private String username;
    private String password;
    private String email;
}
