package com.lin.blog.service;

import com.lin.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    //新增
    Type saveType(Type type);

    Type getType(Long id);

    List<Type> listTypeTop(Integer size);

    Type getTypeByName(String name);

    //分页查询
    Page<Type> ListType(Pageable pageable);

    Type updateType(Long id, Type type);

    void deleteType(Long id);

    List<Type> listType();
}
