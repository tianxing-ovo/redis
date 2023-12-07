package com.ltx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltx.entity.BlogComments;
import com.ltx.mapper.BlogCommentsMapper;
import com.ltx.service.BlogCommentsService;
import org.springframework.stereotype.Service;


@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements BlogCommentsService {

}
