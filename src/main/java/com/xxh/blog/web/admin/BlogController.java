package com.xxh.blog.web.admin;

import com.xxh.blog.dbU.Blog;
import com.xxh.blog.dbU.User;
import com.xxh.blog.service.BlogService;
import com.xxh.blog.service.TagService;
import com.xxh.blog.service.TypeService;
import com.xxh.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT="admin/blog-publish";
    private static final String LIST="admin/blog-list";
    private static final String REDIRECT_LIST="redirect:/admin/blog-list";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blog-list")
    public String blogs(@PageableDefault(size = 5,sort={"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        BlogQuery blog, Model model){
        model.addAttribute("types",typeService.listType());//初始化分类
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return LIST;
    }

    @PostMapping("/blog-list/search")
    public String search(@PageableDefault(size = 5,sort={"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                         BlogQuery blog, Model model){
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blog-list :: blogList";//返回一个片段
    }

    @GetMapping("/blog-list/input")
    public String input(Model model){//初始化避免出错
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    @GetMapping("/blog-publish/{id}/input")
    public String editInput(@PathVariable Long id, Model model){//初始化避免出错
        setTypeAndTag(model);
        Blog b=blogService.getBlog(id);
        b.init();
        model.addAttribute("blog",b);
        return INPUT;
    }

    @PostMapping("/blog-list")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){//新增时的提交
        blog.setUser((User)session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b = blogService.saveBlog(blog);

        if (blog.getId() == null) {
            b =  blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if(b ==null){
            attributes.addFlashAttribute("message","发布失败");
        }else{
            attributes.addFlashAttribute("message","发布成功");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blog-publish/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }
}
