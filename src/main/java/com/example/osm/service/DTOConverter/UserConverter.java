package com.example.osm.service.DTOConverter;

import com.example.osm.entity.DTO.UserDTO;
import com.example.osm.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements EntityConverter<User, UserDTO> {

    @Override
    public User toEntity(UserDTO DTO) {
        DTO.validate();

        User user = new User();
        user.setId(DTO.getId());
        user.setFullName(DTO.getFullName());
        user.setAge(DTO.getAge());
        return user;
    }

    @Override
    public UserDTO toDTO(User entity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(entity.getId());
        userDTO.setFullName(entity.getFullName());
        userDTO.setAge(entity.getAge());

        userDTO.validate();
        return userDTO;
    }
}
