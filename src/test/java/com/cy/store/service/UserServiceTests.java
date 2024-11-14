package com.cy.store.service;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@SpringBootTest注解 表示标注当前的类是一个测试类，不会随同项目一起打包发送
@SpringBootTest
//@RunWith注解 表示启动这个单元测试类。
@RunWith(SpringRunner.class)
public class UserServiceTests {
    //老版本的idea有检测功能，接口是不能够直接创建Bean的（动态代理技术来解决）
    //没报错就不用管
    @Autowired
    private IUserService iUserService;
    /**
     * 单元测试方法要想单独独立运行（不用启动整个项目即可进行单元测试），必须同时满足的特点如下：
     * 1 必须被Test注解所修饰
     * 2 返回值类型必须是void
     * 3 方法的参数列表不能指定任何类型
     * 4 方法的访问修饰符必须是public
     */
    @Test
    public void reg() {
        try {
            User user = new User();
            user.setUsername("yuanxin03");
            user.setPassword("123");
            iUserService.reg(user);
            System.out.println("插入成功");
        } catch (ServiceException e) {
            //先获取类的对象，再获取类的名称
            System.out.println(e.getClass().getSimpleName());
            //获取异常的具体描述信息（先前自定义了被抛出时打印的内容）
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void login() {
        try {
            User limitedUser =
                    iUserService.login("test001", "123");
            System.out.println(limitedUser);
        } catch (ServiceException e) {
            //throw new RuntimeException(e);
            //先获取类的对象，再获取类的名称
            System.out.println(e.getClass().getSimpleName());
            //获取异常的具体描述信息（先前自定义了被抛出时打印的内容）
            System.out.println(e.getMessage());
        }
    }
}
