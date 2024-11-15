package com.example.date_values.service.impl;

import com.example.date_values.config.jwt.JwtTokenProvider;
import com.example.date_values.dto.UserDto;
import com.example.date_values.entity.User;
import com.example.date_values.model.cons.STATUS;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.reponse.LoginRes;
import com.example.date_values.model.request.ChangePasswordReq;
import com.example.date_values.model.request.ChangeRoleReq;
import com.example.date_values.model.request.LoginReq;
import com.example.date_values.model.request.SearchReq;
import com.example.date_values.query.CustomRsqlVisitor;
import com.example.date_values.repository.BaseRepository;
import com.example.date_values.repository.UserRepository;
import com.example.date_values.service.UserService;
import com.example.date_values.util.DateUtil;
import com.example.date_values.util.MapperUtil;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    protected BaseRepository<User> getRepository() {
        return userRepository;
    }

    @Override
    public BaseResponse login(LoginReq req) {
        Optional<User> userOptional = userRepository.findAllByUsername(req.getUsername());
        if (userOptional.isEmpty())
            return new BaseResponse(500, "Account không tồn tại", null);

        User user = userOptional.get();
        if (!Objects.equals(user.getIsActive(), 1))
            return new BaseResponse(500, "Account đã bị xóa", null);

        if (!Objects.equals(user.getStatus(), 1))
            return new BaseResponse(500, "Account đã bị khóa", null);

        if (!isValidPassword(user.getPassword(), req.getPassword())) {
            return new BaseResponse(500, "Mật khẩu không chính xác", null);
        }
        LoginRes result = new LoginRes();
        MapperUtil.mapValue(user, result);
        result.setToken(jwtTokenProvider.generateToken(user.getUsername()));
        return new BaseResponse().success(result);
    }

    @Override
    public BaseResponse register(User user) {
        User result = new User();
        try {
            if (user.getUsername() == null) {
                return new BaseResponse().fail("Tài khoản không được để trống");
            }
            if (user.getPassword() == null) {
                return new BaseResponse().fail("Mật khẩu không được để trống");
            }

            if (userRepository.findAllByUsername(user.getUsername()).isPresent()) {
                return new BaseResponse().fail("Tài khoản đã tồn tại");
            }
            user.setCreateDate(DateUtil.getCurrenDateTime());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("user");
            user.setStatus(1);
            result = super.create(user);
            return new BaseResponse().success(MapperUtil.map(result, UserDto.class));
        }catch (Exception e){
            return new BaseResponse(500, "Có lỗi xảy ra khi tạo tài khoản", null);
        }
    }

    @Override
    public BaseResponse changeRole(ChangeRoleReq req) throws Exception {
        if (!req.getRole().equals("admin") && !req.getRole().equals("user")){
            return new BaseResponse().fail("Quyền chỉ có thể là admin hoặc user");
        }
        User user = this.getById(req.getId());
        if (user == null) {
            return new BaseResponse().fail("Tài khoản không tồn tại");
        }
        user.setRole(req.getRole());
        return new BaseResponse().success(MapperUtil.map(user, UserDto.class));
    }

    @Override
    public BaseResponse changePassword(ChangePasswordReq req) throws Exception {
        User user = this.getById(req.getUserId());
        if (user == null){
            return new BaseResponse().fail("Tài khoản không tồn tại!");
        }
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())){
            return new BaseResponse().fail("Mật khẩu cũ không khớp!");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        return new BaseResponse().success("Thay đổi mật khẩu thành công!");
    }

    @Override
    public BaseResponse lockUser(Long id) throws Exception {
        User user = this.getById(id);
        if (user == null){
            return new BaseResponse().fail("Tài khoản không tồn tại Hoặc đã bị khóa!");
        }
        user.setStatus(-1);
        userRepository.save(user);
        return new BaseResponse().success("Khóa tài khoản thành công!");
    }

    @Override
    public BaseResponse unlockUser(Long id) throws Exception {
        User user = this.getById(id);
        if (user == null || user.getIsActive() != 1){
            return new BaseResponse().fail("Tài khoản không tồn tại!");
        }
        user.setStatus(1);
        userRepository.save(user);
        return new BaseResponse().success("Mở Khóa tài khoản thành công!");
    }

    @Override
    public User update(User t) throws Exception {
        User entityMy = this.getRepository().findAllById(t.getId());
        MapperUtil.mapValue(t, entityMy);
        t.setUpdateDate(DateUtil.getCurrenDateTime());
//        entityMy.setPassword(passwordEncoder.encode(t.getPassword()));
        return getRepository().save(entityMy);
    }

    private boolean isValidPassword(String userPass, String reqPass) {
        return !StringUtils.isEmpty(reqPass) && passwordEncoder.matches(reqPass, userPass);
    }
}
