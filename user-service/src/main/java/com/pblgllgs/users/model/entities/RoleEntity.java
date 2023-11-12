package com.pblgllgs.users.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author pblgl
 * Created on 07-11-2023
 */

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RoleEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity>users ;

    @ManyToMany(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "roles_authorities",
            joinColumns = @JoinColumn(
                    name = "roles_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "roles_authorities_id_foreign_key"
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "authorities_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "authorities_roles_id_foreign_key"
                    )
            )
    )
    private Collection<AuthorityEntity> authorities;

    public RoleEntity(String name, Collection<AuthorityEntity> authorities) {
        this.name = name;
        this.authorities = authorities;
    }
}
