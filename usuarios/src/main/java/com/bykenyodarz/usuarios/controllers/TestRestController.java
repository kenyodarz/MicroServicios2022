package com.bykenyodarz.usuarios.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/test")
public class TestRestController {

    @GetMapping("/all")
    public String allAccess() {
        return "Contenido Público";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('SUPERVISOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "Contenido de Usuario";
    }

    @GetMapping("/supp")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public String supAccess() {
        return "Contenido de Supervisor";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Contenido de Moderador.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Contenido de Administrador.";
    }
}
