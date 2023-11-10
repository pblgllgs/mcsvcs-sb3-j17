package com.pblgllgs.users;

import com.pblgllgs.users.model.entities.AuthorityEntity;
import com.pblgllgs.users.model.entities.RoleEntity;
import com.pblgllgs.users.model.entities.UserEntity;
import com.pblgllgs.users.model.enums.Roles;
import com.pblgllgs.users.repositories.AuthorityRepository;
import com.pblgllgs.users.repositories.RoleRepository;
import com.pblgllgs.users.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author pblgl
 * Created on 07-11-2023
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class InitialUsersSetup {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Transactional
    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("From application ready event");
        AuthorityEntity readAuthority = createAuthority("READ");
        AuthorityEntity writeAuthority = createAuthority("WRITE");
        AuthorityEntity deleteAuthority = createAuthority("DELETE");

        createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
        RoleEntity roleAdmin = createRole(
                Roles.ROLE_ADMIN.name(),
                Arrays.asList(readAuthority, writeAuthority, deleteAuthority)
        );
        if (roleAdmin == null) {
            return;
        }
        UserEntity adminUser = UserEntity.builder()
                .firstName("pbl")
                .lastName("gllgs")
                .email("pbl.gllgs@gmail.com")
                .userId(UUID.randomUUID().toString())
                .encryptedPassword(bCryptPasswordEncoder.encode("12345678"))
                .roles(List.of(roleAdmin))
                .build();
        UserEntity storedUser = userRepository.findByEmail(adminUser.getEmail());
        if (storedUser == null) {
            userRepository.save(adminUser);
        }
    }

    @Transactional
    public AuthorityEntity createAuthority(String name) {
        AuthorityEntity authoritiesEntity = authorityRepository.findByName(name);
        if (authoritiesEntity == null) {
            authoritiesEntity = new AuthorityEntity(name);
            authorityRepository.save(authoritiesEntity);
        }
        return authoritiesEntity;
    }

    @Transactional
    public RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
        RoleEntity roleEntity = roleRepository.findByName(name);
        if (roleEntity == null) {
            roleEntity = new RoleEntity(name, authorities);
            roleRepository.save(roleEntity);
        }
        return roleEntity;
    }
}
