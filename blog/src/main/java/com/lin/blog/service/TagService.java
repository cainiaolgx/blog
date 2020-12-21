package com.lin.blog.service;

import com.lin.blog.pojo.Tag;
import com.lin.blog.pojo.Tag;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    //新增
    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    //分页查询
    Page<Tag> ListTag(Pageable pageable);

    List<Tag> listTagTop(Integer size);

    Tag updateTag(Long id, Tag tag);

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    void deleteTag(Long id);
}
