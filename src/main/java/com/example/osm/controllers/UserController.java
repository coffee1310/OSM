package com.example.osm.controllers;

import com.example.osm.entity.User;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            user = userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User user = userRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFound(String.format("User with id: %d was not found", id)));

            user.setAge(userDetails.getAge());
            user.setFullName(userDetails.getFullName());

            userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFound(String.format("User with id: %d was not found", id)));

            userRepository.delete(user);
            return new ResponseEntity<>("User was deleted", HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
