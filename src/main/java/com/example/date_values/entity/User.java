package com.example.date_values.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user")
public class User extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String username;

    @Column()
    private String password;

    @Column()
    private String fullName;

    @Column()
    private String role;

    @Column()
    private String email;

    @Column()
    private String phone;

    @Column()
    private String address;
}
