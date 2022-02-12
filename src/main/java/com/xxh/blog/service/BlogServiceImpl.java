package com.xxh.blog.service;

import com.xxh.blog.NotFoundException;
import com.xxh.blog.dao.BlogRepository;
import com.xxh.blog.dbU.Blog;
import com.xxh.blog.dbU.Type;
import com.xxh.blog.util.MarkdownUtils;
import com.xxh.blog.util.MyBeanUtils;
import com.xxh.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).orElse(null);
            }

            @Override
            public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
                return blogRepository.findAll(new Specification<Blog>() {
                    @Override
                    public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                        //处理动态查询
                        List<Predicate> predicates = new ArrayList<>();
                        if(!"".equals(blog.getTitle()) && blog.getTitle()!=null){
                            predicates.add(criteriaBuilder.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));//设定值到封装好的容器中 ,构造表达式(查询对象的属性和值)
                        }
                        if(blog.getTypeId()!=null){
                            predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),blog.getTypeId()));//精确匹配
                        }
                        if(blog.isRecommand()){
                            predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommandEnabled"),blog.isRecommand()));//推荐
                        }
                        //相当于sql的where，传递条件的数组
                        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));//自动拼接
                        return null;
            }
        },pageable);
    }

    @Transient
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId()==null){
            //新增
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViewCount(0);
        }else{
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b=blogRepository.findById(id).orElse(null);
        if(b==null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {//关联表的查询
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);

    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);

    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);

    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
