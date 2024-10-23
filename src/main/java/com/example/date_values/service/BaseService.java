package com.example.date_values.service;

import com.example.date_values.entity.BaseEntity;
import com.example.date_values.model.request.SearchReq;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

public interface BaseService<T extends BaseEntity> {
    Page<T> search(SearchReq req);

    T create(T t) throws Exception;

    T update(T t) throws Exception;

    T getById(Long id) throws Exception;

    List<T> getByActive();

    List<T> getAll();

    void delete(Long id);
}
