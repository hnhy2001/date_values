package com.example.date_values.repository;


import com.example.date_values.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findAllByUsername(String userName);
}
