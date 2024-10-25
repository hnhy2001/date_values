package com.example.date_values.controller;

import com.example.date_values.dto.UserDto;
import com.example.date_values.entity.User;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.request.ChangeRoleReq;
import com.example.date_values.model.request.LoginReq;
import com.example.date_values.service.BaseService;
import com.example.date_values.service.UserService;
import com.example.date_values.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController extends BaseController<User, UserDto>{
    @Autowired
    UserService userService;

    public UserController() {
        super(UserDto.class);
    }

    @Override
    protected BaseService<User> getService() {
        return userService;
    }

    @PostMapping("/create")
    public BaseResponse create(@RequestBody User t) throws Exception {
        return userService.register(t);
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginReq req) throws Exception {
        return userService.login(req);
    }

    @PostMapping("/change-role")
    public BaseResponse changeRole(@RequestBody ChangeRoleReq req) throws Exception {
        return userService.changeRole(req);
    }
}
