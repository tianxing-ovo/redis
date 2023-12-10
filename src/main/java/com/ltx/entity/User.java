package com.ltx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends BasePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 密码,加密存储
     */
    private String password;

    /**
     * 昵称,默认是随机字符
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String icon = "";


}
