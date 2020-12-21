package com.lin.blog.service;

import com.lin.blog.pojo.Blog;

import com.lin.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Blog saveBlog(Blog blog);

    //通过分页进行搜索
    Page<Blog> listBlog(String query, Pageable pageable);

    Page<Blog> listBlog(Long tagId, Pageable pageable);

    Blog getAndConvert(Long id);

    Map<String, List<Blog>> archiveBlog();//归档

    Long countBlog();

    Page<Blog> listBlog(Pageable pageable);

    //推荐博客列表
    List<Blog> listRecommendBlogTop(Integer size);

    Blog updateBlog(Blog blog, Long id);

    void deleteBlog(Long id);
}
