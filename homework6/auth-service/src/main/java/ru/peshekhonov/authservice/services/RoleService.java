package ru.peshekhonov.authservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.peshekhonov.api.exceptions.ResourceNotFoundException;
import ru.peshekhonov.authservice.entities.Role;
import ru.peshekhonov.authservice.repositories.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getUserRole(String name) {
        return roleRepository.findByName("ROLE_" + name)
                .orElseThrow(() -> new ResourceNotFoundException("Роль " + name + " не найдена"));
    }
}
