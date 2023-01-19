package ru.peshekhonov.authservice.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.peshekhonov.api.dto.VisitorRegistrationDto;
import ru.peshekhonov.authservice.exceptions.RegistrationException;
import ru.peshekhonov.authservice.services.VisitorService;

import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class VisitorRegistrationDtoValidationRulesEngine {

    private String errors;
    private final VisitorService visitorService;

    private final Predicate<VisitorRegistrationDto> isUsernameNull = data -> data.getUsername() == null;
    private final Predicate<VisitorRegistrationDto> isUsernameBlank = data -> data.getUsername().isBlank();
    private final Predicate<VisitorRegistrationDto> isUsernameValid = data -> data.getUsername().matches("^[a-zA-Z0-9]+$");
    private final Predicate<VisitorRegistrationDto> isUsernameExists = new Predicate<>() {
        @Override
        public boolean test(VisitorRegistrationDto data) {
            return visitorService.existsByUsername(data.getUsername());
        }
    };
    private final Predicate<VisitorRegistrationDto> isPasswordNull = data -> data.getPassword() == null;
    private final Predicate<VisitorRegistrationDto> isPasswordBlank = data -> data.getPassword().isBlank();
    private final Predicate<VisitorRegistrationDto> isEmailNotNull = data -> data.getEmail() != null;
    private final Predicate<VisitorRegistrationDto> isEmailExists = new Predicate<>() {
        @Override
        public boolean test(VisitorRegistrationDto data) {
            return visitorService.existsByEmail(data.getEmail());
        }
    };

    private final Predicate<VisitorRegistrationDto> isUsernameNullOrBlank = isUsernameNull.or(isUsernameBlank);
    private final Predicate<VisitorRegistrationDto> isUsernameNotNullAndNotBlankAndNotValid =
            isUsernameNullOrBlank.negate().and(isUsernameValid.negate());
    private final Predicate<VisitorRegistrationDto> isPasswordNullOrBlank = isPasswordNull.or(isPasswordBlank);
    private final Predicate<VisitorRegistrationDto> isUsernameNotNullAndNotBlankAndValidAndExists =
            isUsernameNullOrBlank.negate().and(isUsernameValid).and(isUsernameExists);
    private final Predicate<VisitorRegistrationDto> isEmailNotNullAndExists = isEmailNotNull.and(isEmailExists);

    public final void check(VisitorRegistrationDto visitor) {
        Validator<VisitorRegistrationDto> validator = new Validator<>();
        validator.addRule(isUsernameNullOrBlank, "Имя пользователя не задано.");
        validator.addRule(isUsernameNotNullAndNotBlankAndNotValid, "В имени пользователя допустимы только латиница и цифры.");
        validator.addRule(isPasswordNullOrBlank, "Пароль не задан.");
        validator.addRule(isUsernameNotNullAndNotBlankAndValidAndExists, "Такое имя пользователя уже существует.");
        validator.addRule(isEmailNotNullAndExists, "Такой Email уже есть.");
        errors = validator.validate(visitor);
        onError();
    }

    private void onError() {
        if (errors != null && !errors.isEmpty()) {
            throw new RegistrationException(errors);
        }
    }
}
