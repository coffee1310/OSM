package com.example.osm.service;

import com.example.osm.entity.DTO.UserDTO;
import com.example.osm.entity.User;
import com.example.osm.repository.UserRepository;
import com.example.osm.service.DTOConverter.DTOConverterFactory;
import com.example.osm.service.DTOConverter.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public final DTOConverterFactory converterFactory;

    public UserService(DTOConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserDTO> createUser(User user) {
        EntityConverter<User, UserDTO> converter = converterFactory
                .getConverter(User.class, UserDTO.class);

        UserDTO userDTO = converter.toDTO(user);
        userRepository.save(user);

        return Optional.of(userDTO);
    }

    public Optional<UserDTO> updateUser (User user, User userDetails) {
        user.setAge(userDetails.getAge());
        user.setFullName(userDetails.getFullName());

        EntityConverter<User, UserDTO> converter = converterFactory
                .getConverter(User.class, UserDTO.class);

        UserDTO userDTO = converter.toDTO(userDetails);
        userRepository.save(user);

        return Optional.of(userDTO);
    }
}