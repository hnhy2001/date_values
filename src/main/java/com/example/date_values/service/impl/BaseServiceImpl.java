package com.example.date_values.service.impl;

import com.example.date_values.entity.BaseEntity;
import com.example.date_values.model.cons.STATUS;
import com.example.date_values.model.request.SearchReq;
import com.example.date_values.query.CustomRsqlVisitor;
import com.example.date_values.repository.BaseRepository;
import com.example.date_values.service.BaseService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import util.DateUtil;
import util.MapperUtil;

import java.util.List;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
    private static final String DELETED_FILTER = ";isActive>-1";

    protected abstract BaseRepository<T> getRepository();

    @Override
    public List<T> getAll() {
        return this.getRepository().findAll();
    }


    @Override
    public Page<T> search(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<T> spec = rootNode.accept(new CustomRsqlVisitor<T>());
        Pageable pageable = getPage(req);
        return this.getRepository().findAll(spec, pageable);
    }

    @Override
    public List<T> getByActive() {
        return this.getRepository().findAllByIsActive(1);
    }

    protected Pageable getPage(SearchReq req) {
        String[] sortList = req.getSort().split(",");
        Sort.Direction direction = sortList[1].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return req.getSize() != null
                ?
                PageRequest.of(req.getPage(), req.getSize(), direction, sortList[0])
                :
                Pageable.unpaged();
    }

    @Override
    public T create(T t) throws Exception {
        t.setIsActive(STATUS.ACTIVE);
        t.setCreateDate(DateUtil.getCurrenDateTime());
        return this.getRepository().save(t);
    }

    @Override
    public T update(T t) throws Exception {
        T entityMy = this.getRepository().findAllById(t.getId());
        MapperUtil.mapValue(t, entityMy);
        t.setUpdateDate(DateUtil.getCurrenDateTime());
        return getRepository().save(entityMy);
    }

    @Override
    public T getById(Long id) throws Exception {
        T t = this.getRepository().findAllByIdAndIsActive(id,1);
        return t;
    }

    @Override
    public void delete(Long id) {
        T t = this.getRepository().findAllById(id);
        t.setIsActive(STATUS.DELETED);
        this.getRepository().save(t);
    }
}
