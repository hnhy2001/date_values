package com.example.date_values.service;

import com.example.date_values.dto.UserDto;
import com.example.date_values.entity.User;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.request.ChangeRoleReq;
import com.example.date_values.model.request.LoginReq;

public interface UserService extends BaseService<User> {
    BaseResponse login(LoginReq req);
    BaseResponse register(User req);
    BaseResponse changeRole(ChangeRoleReq req) throws Exception;
}
