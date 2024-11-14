package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.PasswordNotMatchException;
import com.cy.store.service.ex.UserNotFoundException;
import com.cy.store.service.ex.UsernameDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

// 用户模块业务层的实现类
@Service // 不加就报错：依赖异常。该注解起到将当前类的对象交给Spring管理的作用，自动创建对象、维护对象。
// 继承业务实际操作（如登录）的抽象方法并重写
public class UserServiceImpl implements IUserService {
    @Autowired
    // 调用UserMapper这个xml映射文件，用于使用其中的数据库sql语句
    private UserMapper userMapper;
    @Override
    public void reg(User user) {
        // 通过user参数获取传递过来的username
        String username = user.getUsername();
        /* 后续需要在此添加用户名或密码判空逻辑，并抛出对应的异常
        String oldPassword = user.getPassword();
        if (user == null or oldPassword == null) {
            throw new UsernamePasswordNoValueException("用户名或密码为空");
        }
         */

        // 调用userMapper的findByUsername，检查是否已存在同名用户
        User result = userMapper.findByUsername(username);
        // 判断结果集是否为null。如果不为null，则抛出用户名被占用的异常。
        if (result != null) {
            //抛出异常
            throw new UsernameDuplicatedException("用户名被占用");
        }

        // 密码的加密处理：md5算法的形式
        // 串 + 真实password + 串 ---- 交给md5算法进行加密，连续加载三次
        // 串是谁？是盐值。盐值 + password + 盐值 ---- 盐值就是一个随机的字符串
        String oldPassword = user.getPassword();
        // 获取盐值（随机生成一个 全大写的 盐值字符串）
        String salt = UUID.randomUUID().toString().toUpperCase();
        // 记录盐值，补全对应字段
        user.setSalt(salt);
        // 将密码和盐值作为一个整体进行加密处理
        // 忽略原密码的强度，提升了数据的安全性
        String md5Password = getMD5Password(oldPassword, salt);
        // 将加密之后的密码重新补全设置到user对象中去
        user.setPassword(md5Password);

        // 在insert之前，补全其他必要的属性：is_delete=0，4个日志字段
        user.setIsDelete(0);
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);

        // 若无异常，执行 注册 业务逻辑
        Integer rows = userMapper.insert(user);

        // 如果执行成功，返回值为1，否则抛出异常
        if(rows != 1) {
            throw new InsertException("在用户注册过程中，产生了未知的异常");
        }
    }

    // 定义一个md5算法的加密处理方法
    private String getMD5Password(String password, String salt) {
        // md5加密算法的调用（进行三次加密）
        for (int i=0; i<3; i++) {
            password = DigestUtils.md5DigestAsHex((salt+password+salt).getBytes()).toUpperCase();
        }
        //返回加密之后的密码
        return password;
    }

    @Override
    public User login(String username, String password) {

        // 根据用户名称来查询用户的数据是否存在。若不存在，则抛异常。
        // 调用userMapper的findByUsername方法
        User result = userMapper.findByUsername(username);
        if (result == null) {
            throw new UserNotFoundException("用户数据不存在");
        }

        // 检测用户的密码是否匹配
        //1. 先获取到数据库中的加密后的密码
        String encryptedPassword = result.getPassword();
        //2. 和用户传递过来的密码进行比较（不能直接比）
        //2.1 获取注册时自动生成的盐值
        String salt = result.getSalt();
        //2.2 将用户的密码按照相同的md5算法规则进行加密
        String newMD5Password = getMD5Password(password, salt);
        //3. 将密码进行比较
        if (! newMD5Password.equals(encryptedPassword)) {
            throw new PasswordNotMatchException("用户密码错误");
        }

        // 判断用户是否已注销账号（数据库表中is_delete的值是否为1）
        if (result.getIsDelete() == 1) {
            throw new UserNotFoundException("用户数据不存在");
        }

        // 无异常，调用mapper层的findByUsername方法，对用户数据记录执行查询
        // User user = userMapper.findByUsername(username);
        // 不用调取记录所有的字段，轻量化数据，提高传输效率和响应速度，提升了系统性能
        User limitedUser = new User();
        limitedUser.setUid(result.getUid());
        limitedUser.setUsername(result.getUsername());
        limitedUser.setAvatar(result.getAvatar());

        // 返回用户数据。
        // 发现返回的数据是为了辅助其他页面作数据展示使用（只涉及uid、username和avatar）
        //
        return limitedUser;
    }


}
