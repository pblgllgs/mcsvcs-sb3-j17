package com.pblgllgs.users.services;

import com.pblgllgs.users.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);
    UserDto getUserDetailsByEmail(String email);

    void deleteUser(String userId);
    UserDto findUserById(String userId, String authorization);
}
