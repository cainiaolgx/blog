package com.lin.blog.web;

import com.lin.blog.dao.CommentRepository;
import com.lin.blog.pojo.Comment;
import com.lin.blog.pojo.User;
import com.lin.blog.service.BlogService;
import com.lin.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;
    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")//局部刷新评论区
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")//跳转到上面代码
    public String post(Comment comment, HttpSession session) {
        Long id = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(id));//把内容传给comment
        User user;
        user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
//            comment.setNickname(user.getNickname());
        } else {
            comment.setAvatar(avatar);
        }

        commentService.saveComment(comment);//保存评论
        return "redirect:/comments/" + id;
    }
}
