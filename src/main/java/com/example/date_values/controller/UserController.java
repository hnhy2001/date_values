package com.example.date_values.controller;

import com.example.date_values.dto.UserDto;
import com.example.date_values.entity.User;
import com.example.date_values.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping()
    public List<User> getAll() throws Exception {
        return userService.getAll();
    }
}
