package com.ltx.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltx.constant.SystemConstant;
import com.ltx.dto.UserDTO;
import com.ltx.entity.Blog;
import com.ltx.service.BlogService;
import com.ltx.util.UserHolder;
import com.ltx.entity.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @PostMapping
    public R saveBlog(@RequestBody Blog blog) {
        return blogService.saveBlog(blog);
    }

    @PutMapping("/like/{id}")
    public R likeBlog(@PathVariable("id") Long id) {
        return blogService.likeBlog(id);
    }

    @GetMapping("/of/me")
    public R queryMyBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        // 获取登录用户
        UserDTO userDTO = UserHolder.get();
        // 根据用户查询
        Page<Blog> page = blogService.query()
                .eq("user_id", userDTO.getId()).page(new Page<>(current, SystemConstant.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> blogList = page.getRecords();
        return R.ok(blogList);
    }

    @GetMapping("/hot")
    public R queryHotBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return blogService.queryHotBlog(current);
    }

    @GetMapping("/{id}")
    public R queryBlogById(@PathVariable("id") Long id) {
        return blogService.queryBlogById(id);
    }

    @GetMapping("/likes/{id}")
    public R queryBlogLikes(@PathVariable("id") Long id) {
        return blogService.queryBlogLikes(id);
    }

    @GetMapping("/of/user")
    public R queryBlogByUserId(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam("id") Long id) {
        // 根据用户查询
        Page<Blog> page = blogService.query()
                .eq("user_id", id).page(new Page<>(current, SystemConstant.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> blogList = page.getRecords();
        return R.ok(blogList);
    }

    @GetMapping("/of/follow")
    public R queryBlogOfFollow(
            @RequestParam("lastId") Long max, @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return blogService.queryBlogOfFollow(max, offset);
    }
}
