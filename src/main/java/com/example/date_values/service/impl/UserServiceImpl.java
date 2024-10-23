package com.example.date_values.service.impl;

import com.example.date_values.dto.UserDto;
import com.example.date_values.entity.User;
import com.example.date_values.repository.BaseRepository;
import com.example.date_values.repository.UserRepository;
import com.example.date_values.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    protected BaseRepository<User> getRepository() {
        return userRepository;
    }
}
