package ru.peshekhonov.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.peshekhonov.authservice.entities.Visitor;

import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Optional<Visitor> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
