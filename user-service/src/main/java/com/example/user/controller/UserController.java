package com.example.user.controller;

import com.example.user.dto.UserRequest;
import com.example.user.entities.User;
import com.example.user.services.IUserService;
import com.example.user.services.KeycloakService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService iUserService;
    private final KeycloakService keycloakService;

    public UserController(IUserService iUserService, KeycloakService keycloakService) {
        this.iUserService = iUserService;
        this.keycloakService = keycloakService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        User createdUser = iUserService.adduser(user);
        if (createdUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding user");
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/allroles")
    public List<String> getall() {
        return keycloakService.getAllRoles();
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/addrole")
    public void addrole(@RequestBody UserRequest userRequest) {
        keycloakService.addRealmRole(userRequest.getUsername());
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = iUserService.getall();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = iUserService.getuser(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @GetMapping("/produit/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = iUserService.getbyid(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = iUserService.updateuser(id, user);
        if (updatedUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        iUserService.deleteuser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
    }
    @GetMapping("/public")
    public String publicEndpoint() {
        return "Accessible par tout le monde";
    }

    @GetMapping("/secured")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String securedEndpoint() {
        return "Accessible uniquement aux utilisateurs avec le rôle USER";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminEndpoint() {
        return "Accessible uniquement aux ADMIN";
    }
}
