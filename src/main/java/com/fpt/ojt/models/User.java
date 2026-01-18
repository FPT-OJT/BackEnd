package com.fpt.ojt.models;

import com.fpt.ojt.constants.Constants;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractBaseEntity {
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Constants.RoleEnum role;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    public String getFullName() {
        return lastName + " " + firstName;
    }
}