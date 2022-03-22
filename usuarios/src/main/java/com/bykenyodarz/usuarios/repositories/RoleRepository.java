package com.bykenyodarz.usuarios.repositories;

import com.bykenyodarz.usuarios.models.ERole;
import com.bykenyodarz.usuarios.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
