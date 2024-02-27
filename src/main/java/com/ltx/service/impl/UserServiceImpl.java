package com.ltx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltx.constant.RedisConstant;
import com.ltx.constant.SystemConstant;
import com.ltx.dto.LoginFormDTO;
import com.ltx.dto.UserDTO;
import com.ltx.entity.R;
import com.ltx.entity.User;
import com.ltx.mapper.UserMapper;
import com.ltx.service.UserService;
import com.ltx.util.RegexUtil;
import com.ltx.util.UserHolder;
import io.github.tianxingovo.common.SMSUtil;
import io.github.tianxingovo.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ltx.constant.RedisConstant.Code;
import static com.ltx.constant.RedisConstant.USER_SIGN_KEY;
import static com.ltx.constant.SystemConstant.USER_NICK_NAME_PREFIX;


@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ObjectMapper mapper;


    /**
     * 发送短信验证码
     *
     * @param phone   手机号码
     * @param session 会话
     */
    @Override
    public R sendCode(String phone, HttpSession session) {
        // 校验手机号
        if (RegexUtil.isPhoneInvalid(phone)) {
            return R.fail("手机号格式错误!");
        }
        // 保存验证码到redis中,过期时间为2分钟
        String code = RandomUtil.randomNumbers(6);
        // session和redis二选一
        redisUtil.set(Code.LOGIN_CODE_KEY + phone, code, Code.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 发送验证码
        SMSUtil.sendMessage("tanhua交友", "SMS_276055333", phone, code);
        return R.ok();
    }

    /**
     * 登录
     */
    @Override
    public R login(LoginFormDTO loginForm, HttpSession session) {
        // 校验手机号
        String phone = loginForm.getPhone();
        if (RegexUtil.isPhoneInvalid(phone)) {
            return R.fail("手机号格式错误!");
        }
        // 从redis/session中获取验证码并校验
        String cacheCode = redisUtil.get(Code.LOGIN_CODE_KEY + phone);
        if (cacheCode == null || !cacheCode.equals(loginForm.getCode())) {
            return R.fail("验证码错误!");
        }
        // 根据手机号查询用户
        User user = query().eq("phone", phone).one();
        // 如果用户不存在,创建新用户
        if (user == null) {
            user = createUserWithPhone(phone);
        }
        // 随机生成token作为key
        String token = UUID.randomUUID().toString(true);
        String tokenKey = RedisConstant.Token.LOGIN_TOKEN_KEY + token;
        // UserDTO转为Map进行存储
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, String> userMap = mapper.convertValue(userDTO, new TypeReference<Map<String, String>>() {
        });
        // 保存用户信息到redis/session中
        redisUtil.putAll(tokenKey, userMap);
        // 设置token有效期,过期时间为30分钟
        redisUtil.expire(tokenKey, RedisConstant.Token.LOGIN_TOKEN_TTL, TimeUnit.MINUTES);
        // 返回token给前端
        return R.ok(token);
    }

    @Override
    public R sign() {
        // 获取当前登录用户
        Long userId = UserHolder.getUserId();
        // 获取日期
        LocalDateTime now = LocalDateTime.now();
        // 拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_KEY + userId + keySuffix;
        // 获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth();
        return R.ok();
    }

    @Override
    public R signCount() {
        return R.ok();
    }

    /**
     * 根据手机号创建用户
     *
     * @param phone 手机号码
     */
    private User createUserWithPhone(String phone) {
        String nickName = USER_NICK_NAME_PREFIX + RandomUtil.randomString(10);
        // 创建用户
        User user = new User().setPhone(phone).setNickName(nickName);
        // 保存用户
        save(user);
        return user;
    }

    /**
     * 将验证码保存到session中
     *
     * @param session 会话
     * @param code    验证码
     */
    private void saveCodeToSession(HttpSession session, String code) {
        session.setAttribute(SystemConstant.SESSION_ATTRIBUTE_VERIFICATION_CODE, code);
    }

    /**
     * 从session中获取验证码
     *
     * @param session 会话
     * @return 验证码
     */
    private Object getCodeFromSession(HttpSession session) {
        return session.getAttribute(SystemConstant.SESSION_ATTRIBUTE_VERIFICATION_CODE);
    }

    /**
     * 将用户信息保存到session中
     *
     * @param session 会话
     * @param userDTO 用户信息
     */
    private void saveUserToSession(HttpSession session, UserDTO userDTO) {
        session.setAttribute(SystemConstant.SESSION_ATTRIBUTE_USER, userDTO);
    }
}
