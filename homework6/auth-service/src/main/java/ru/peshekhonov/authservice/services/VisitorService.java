package ru.peshekhonov.authservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.peshekhonov.api.dto.VisitorRegistrationDto;
import ru.peshekhonov.authservice.entities.Role;
import ru.peshekhonov.authservice.entities.Visitor;
import ru.peshekhonov.authservice.mappers.VisitorDtoMapper;
import ru.peshekhonov.authservice.repositories.VisitorRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService implements UserDetailsService {

    private final VisitorRepository visitorRepository;
    private final VisitorDtoMapper visitorDtoMapper;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;

    public Optional<Visitor> findByUsername(String username) {
        return visitorRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return visitorRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return visitorRepository.existsByEmail(email);
    }

    @Transactional
    public Visitor createUser(VisitorRegistrationDto user) {
        Visitor visitor = visitorDtoMapper.map(user, encoder);
        visitor.setRoles(List.of(roleService.getUserRole("USER")));
        return visitorRepository.save(visitor);
    }

    @Transactional
    public List<String> getUserRoleNames(String username) throws UsernameNotFoundException {
        Visitor visitor = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь '%s' не найден", username)));
        return visitor.getRoles().stream().map(Role::getName).toList();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Visitor visitor = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь '%s' не найден", username)));
        return new User(visitor.getUsername(), visitor.getPassword(), mapRolesToAuthorities(visitor.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
