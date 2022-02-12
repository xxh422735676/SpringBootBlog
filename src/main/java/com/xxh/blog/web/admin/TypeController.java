package com.xxh.blog.web.admin;

import com.xxh.blog.dbU.Type;
import com.xxh.blog.service.TypeService;
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
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/blog-types")
    public String list(@PageableDefault(size = 10,sort = {"id"},direction = Sort.Direction.DESC)
                                   Pageable pageable, Model model){
        model.addAttribute("page",typeService.listType(pageable));//分页实现
        return "admin/blog-types";
    }

    @GetMapping("blog-types/addType")
    public String addInput(Model model){
        model.addAttribute("type",new Type()); //新建Type对象给Error
        return "admin/blog-addType";
    }

    @GetMapping("/blog-types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/blog-addType";
    }

    @PostMapping("/blog-types")
    public String saveType(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        Type type1=typeService.getTypeByName(type.getName());
        if(type1!=null){
            result.rejectValue("name","nameError","分类名已存在，不能重复添加！");//增加重复名验证错误
//            return "admin/blog-addType";
        }
        if(result.hasErrors()){
            return "admin/blog-addType";
        }

        Type t = typeService.saveType(type);

        if(t == null){
            attributes.addFlashAttribute("message","添加失败");
        } else{
            attributes.addFlashAttribute("message","添加成功");
        }
        return "redirect:/admin/blog-types";
    }

    @PostMapping("/blog-types/{id}")
    public String editType(@Valid Type type, BindingResult result,
                           @PathVariable Long id, RedirectAttributes attributes) {
        Type type1=typeService.getTypeByName(type.getName());
        if(type1!=null){
            result.rejectValue("name","nameError","分类名已存在，不能重复添加！");//增加重复名验证错误
//            return "admin/blog-addType";
        }
        if(result.hasErrors()){
            return "admin/blog-addType";
        }

        Type t = typeService.updateType(id,type);

        if(t == null){
            attributes.addFlashAttribute("message","更新失败");
        } else{
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/blog-types";
    }

    @GetMapping("/blog-types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blog-types";
    }

}
