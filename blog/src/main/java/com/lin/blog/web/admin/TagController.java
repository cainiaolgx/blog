package com.lin.blog.web.admin;

import com.lin.blog.pojo.Tag;
import com.lin.blog.pojo.Type;
import com.lin.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")//设置pageable的值为长度为十，通过id倒叙id方法
    public String tags(@PageableDefault(size = 2, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
            , Model model) {

        model.addAttribute("page", tagService.ListTag(pageable));

        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", tagService.getTag(id));
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
//        Type type1 = tagService.getTypeByName(type.getName());
//        if (type1 !=null){
//            result.rejectValue("name","nameError","不能重复添加分类");
//        }
//        if(result.hasErrors())
//        {
//            return "admin/tags-input";
//        }
        Tag t = tagService.saveTag(tag);

        if (t == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
//        Type type1 = tagService.getTypeByName(type.getName());
//        if (type1 !=null){
//            result.rejectValue("name","nameError","不能重复添加分类");
//        }
//        if(result.hasErrors())
//        {
//            return "admin/tags-input";
//        }
        Tag t = tagService.updateTag(id, tag);

        if (t == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";

    }
}
