package com.lin.blog.service;

import com.lin.blog.NotFoundException;
import com.lin.blog.dao.BlogRepository;
import com.lin.blog.pojo.Blog;
import com.lin.blog.pojo.Type;
import com.lin.blog.util.MarkdownUtils;
import com.lin.blog.util.MyBeanUtils;
import com.lin.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import javax.persistence.criteria.*;
import java.sql.Time;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                //List转为数组
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {//新增时添加创建时间
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }


        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");//关联表的查询

                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Transactional
    @Override//修改文本格式后传给前端
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("不存在");
        }
        Blog blog1 = new Blog();//不会对数据库进行操作
        BeanUtils.copyProperties(blog, blog1);
        String content = blog1.getContent();
        blog1.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateviews(id);
        return blog1;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> groupYear = blogRepository.findGroupYear();
        Map<String, List<Blog>> maps = new HashMap<>();
        for (String year : groupYear) {
            maps.put(year, blogRepository.findByYear(year));
        }
        return maps;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Blog updateBlog(Blog blog, Long id) {
        Blog one = blogRepository.getOne(id);
        if (one == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, one, MyBeanUtils.getNullPropertyNames(blog));
        one.setUpdateTime(new Date());
        return blogRepository.save(one);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
