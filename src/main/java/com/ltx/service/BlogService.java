package com.ltx.service;

import com.ltx.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.tianxingovo.common.R;


public interface BlogService extends IService<Blog> {

    R queryBlogById(Long id);

    R queryBlogLikes(Long id);

    R queryBlogOfFollow(Long max, Integer offset);

    R queryHotBlog(Integer current);

    R likeBlog(Long id);

    R saveBlog(Blog blog);
}