package com.lin.blog.dao;

import com.lin.blog.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//JpaSpecificationExecutor<Blog>可以实习动态sql
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    @Query("select b from t_blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    //对搜索结果进行模糊查询
    @Query("select b from t_blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Transactional
    @Modifying//修改需要的注解
    @Query("update t_blog b set b.views = b.views + 1 where b.id = ?1")
    int updateviews(Long id);

    @Query("select function('date_format',b.updateTime,'%Y') as year from t_blog b group by year order by function('date_format',b.updateTime,'%Y') desc ")
    List<String> findGroupYear();

    @Query("select b from t_blog b where function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);
}
