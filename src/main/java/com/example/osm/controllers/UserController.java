package com.example.osm.controllers;

import com.example.osm.entity.DTO.UserDTO;
import com.example.osm.entity.User;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.repository.UserRepository;
import com.example.osm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        Optional<UserDTO> userDTO = userService.createUser(user);
        return userDTO.map(dto -> ResponseEntity
                .created(URI.create("/api/users/" + dto.getId()))
                .body(dto))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFound(String.format("User with id: %d was not found", id)));

        Optional<UserDTO> userDTO = userService.updateUser(user, userDetails);
        return userDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFound(String.format("User with id: %d was not found", id)));

        userRepository.delete(user);
        return new ResponseEntity<>("User was deleted", HttpStatus.OK);
    }
}
