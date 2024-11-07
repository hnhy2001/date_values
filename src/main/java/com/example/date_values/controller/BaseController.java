package com.example.date_values.controller;

import com.example.date_values.entity.BaseEntity;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.request.SearchReq;
import com.example.date_values.service.BaseService;
import com.example.date_values.util.MapperUtil;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
//@PreAuthorize("@appAuthorizer.authorize(authentication, #action, this)")
public abstract class BaseController<T extends BaseEntity, U> {
    private final Class<U> dtoClass;

    // Constructor nhận class type của U
    public BaseController(Class<U> dtoClass) {
        this.dtoClass = dtoClass;
    }

    protected abstract BaseService<T> getService();

    @PostMapping("/create")
    public BaseResponse create(@RequestBody T t) throws Exception {
        return new BaseResponse(200, "Tạo thành công!", MapperUtil.map(this.getService().create(t), dtoClass));
    }

    @GetMapping("/search")
//    @PreAuthorize("@appAuthorizer.authorize(authentication, 'VIEW', this)")
    public BaseResponse search(SearchReq req) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", MapperUtil.mapEntityPageIntoDtoPage(this.getService().search(req), dtoClass));
    }

    @GetMapping("/detail")
    public BaseResponse getById(@RequestParam(value = "id") Long id) throws Exception {
        T t = this.getService().getById(id);
        if (t == null){
            return new BaseResponse().fail(String.format("Dữ liệu có id %s không tồn tại!", id));
        }
        return new BaseResponse(200, "Lấy dữ liệu thành công!", MapperUtil.map(t, dtoClass));
    }

    @GetMapping("/get-all")
    public BaseResponse getAll(@RequestParam(value = "id") Long id) throws Exception {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", MapperUtil.mapEntityListIntoDtoPage(this.getService().getByActive(), dtoClass));
    }

    @PutMapping("/update")
    public BaseResponse update(@RequestBody T t) throws Exception {
        return new BaseResponse(200, "Cập nhật thành công!", MapperUtil.map(this.getService().update(t), dtoClass));
    }


    @DeleteMapping("/delete")
    public BaseResponse deleteById(@RequestParam(name = "id") Long id) {
        this.getService().delete(id);
        return new BaseResponse(200, "Xóa thành công!");
    }

}
