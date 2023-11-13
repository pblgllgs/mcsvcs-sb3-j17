package com.pblgllgs.users.services.impl;

import com.pblgllgs.users.clients.AlbumClient;
import com.pblgllgs.users.dto.UserDto;
import com.pblgllgs.users.model.entities.AuthorityEntity;
import com.pblgllgs.users.model.entities.RoleEntity;
import com.pblgllgs.users.model.entities.UserEntity;
import com.pblgllgs.users.model.responses.AlbumResponseModel;
import com.pblgllgs.users.repositories.UserRepository;
import com.pblgllgs.users.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AlbumClient albumClient;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = mapperUserDtoToUserEntity(userDto);
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userEntity);

        return mapperUserEntityToUserDto(userEntity);
    }

    private UserDto mapperUserEntityToUserDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setPassword(userEntity.getEncryptedPassword());
        userDto.setEmail(userEntity.getEmail());
        userDto.setUserId(userEntity.getUserId());
        return userDto;
    }

    private UserEntity mapperUserDtoToUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        return userEntity;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("USER_NOT_FOUND");
        }
        return mapperUserEntityToUserDto(userEntity);
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("USER_NOT_FOUND");
        }
        userRepository.deleteById(userEntity.getId());
    }

    @Override
    public UserDto findUserById(String userId, String authorization) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("user not found");
        }
        UserDto userDto = mapperUserEntityToUserDto(userEntity);
        log.debug("Before calling albums microservice");
        List<AlbumResponseModel> userAlbums = albumClient.findAllAlbums(userId, authorization);
        log.debug("After calling albums microservice");
        userDto.setAlbums(userAlbums);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("USER_NOT_FOUND");
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Collection<RoleEntity> roles = userEntity.getRoles();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            Collection<AuthorityEntity> authoritiesEntities = role.getAuthorities();
            authoritiesEntities.forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority.getName())));
        });
        return new User(
                userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }
}
