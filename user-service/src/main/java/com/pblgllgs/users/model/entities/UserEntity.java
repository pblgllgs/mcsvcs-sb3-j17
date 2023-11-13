package com.pblgllgs.users.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "email_UK",
                        columnNames = "email"),
                @UniqueConstraint(
                        name = "user_id_UK",
                        columnNames = "user_id"),
                @UniqueConstraint(
                        name = "encrypted_password_UK",
                        columnNames = "encrypted_password"
                )
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "user_entity_sequence",
            sequenceName = "user_entity_sequence",
            allocationSize = 1
    )
    private Long id;
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "email", nullable = false, length = 120)
    private String email;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @ManyToMany(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "users_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "users_roles_id_foreign_key"
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "roles_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "roles_users_id_foreign_key"
                    )
            )
    )
    private Collection<RoleEntity> roles;
}
