package com.pblgllgs.users.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author pblgl
 * Created on 07-11-2023
 */

@Entity
@Table(name = "authorities")
@NoArgsConstructor
@Data
public class AuthorityEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Collection<RoleEntity> roles;

    public AuthorityEntity(String name) {
        this.name = name;
    }
}
