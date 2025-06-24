package com.tms.data_service.mapper;

import com.tms.data_service.dto.UserDTO;
import com.tms.data_service.model.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse(null));
        return dto;
    }

    public static User dtoToUser(UserDTO userDTO)
    {
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        return user;
    }
}
