package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

//@SpringBootTest注解 表示标注当前的类是一个测试类，不会随同项目一起打包发送
@SpringBootTest
//@RunWith注解 表示启动这个单元测试类。
@RunWith(SpringRunner.class)
public class UserMapperTests {
    //老版本的idea有检测功能，接口是不能够直接创建Bean的（动态代理技术来解决）
    //没报错就不用管
    @Autowired
    private UserMapper userMapper;
    /**
     * 单元测试方法要想单独独立运行（不用启动整个项目即可进行单元测试），必须同时满足的特点如下：
     * 1 必须被Test注解所修饰
     * 2 返回值类型必须是void
     * 3 方法的参数列表不能指定任何类型
     * 4 方法的访问修饰符必须是public
     */
    @Test
    public void insert() {
        User user = new User();
        user.setUsername("Tim");
        user.setPassword("123");
        Integer rows = userMapper.insert(user);
        System.out.println(rows);
    }

    @Test
    public void findByUsername() {
        User user = userMapper.findByUsername("tim");
        System.out.println(user);
    }

    @Test
    // 单元测试方法 不能有参数列表
    public void updatePasswordByUid() {
        Integer rows = userMapper.updatePasswordByUid(
                7, "321",
                "管理员", new Date());
        System.out.println("受影响的行数为：" + rows);
    }

    @Test
    public void findByUid() {
        User user = userMapper.findByUid(7);
        System.out.println(user);
    }

    @Test
    public void updateInfoByUid() {
        User user = new User();
        user.setUid(7);
        user.setPhone("15511110000");
        user.setEmail("test001.qq.com");
        user.setGender(1);
        userMapper.updateInfoByUid(user);
    }

    @Test
    public void updateAvatarByUid() {
        Integer uid = 1;
        String avatar = "/a.png";
        String modifiedUser = "系统管理员";
        Date modifiedTime = new Date();
        userMapper.updateAvatarByUid(uid, avatar,
                                    modifiedUser, modifiedTime);
    }
}
