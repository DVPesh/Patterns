package ru.peshekhonov.authservice.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.peshekhonov.api.dto.VisitorDto;
import ru.peshekhonov.api.dto.VisitorRegistrationDto;
import ru.peshekhonov.authservice.entities.Visitor;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitorDtoMapper {

    @Mapping(source = "password", target = "password", qualifiedByName = "getEncodedPassword")
    Visitor map(VisitorRegistrationDto dto, @Context BCryptPasswordEncoder encoder);

    @Named("getEncodedPassword")
    default String getEncodedPassword(String password, @Context BCryptPasswordEncoder encoder) {
        return encoder.encode(password);
    }

    VisitorDto map(Visitor visitor);

    List<VisitorDto> map(List<Visitor> visitors);
}
