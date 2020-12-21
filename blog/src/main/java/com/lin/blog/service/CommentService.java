package com.lin.blog.service;

import com.lin.blog.pojo.Comment;

import java.util.List;

public interface CommentService {
    //获取评论区列表
    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);//保存评论
}
