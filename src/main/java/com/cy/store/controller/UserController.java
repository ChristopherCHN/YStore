package com.cy.store.controller;

import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicatedException;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@Controller
@RestController //等效于@Controller + @ResponseBody
@RequestMapping("users")
public class UserController extends BaseController{
    @Autowired
    private IUserService userService;

    /*
    @RequestMapping("reg")
    //不建议的方法
    @ResponseBody //表示此方法的响应结果以json格式进行数据的响应给到前端
    public JsonResult<Void> reg(User user) {
        //创建响应结果对象
        JsonResult<Void> result = new JsonResult<>();
        try {
            userService.reg(user);
            result.setState(200);
            result.setMessage("用户注册成功");
        } catch (UsernameDuplicatedException e) {
            result.setState(4000); //状态码
            result.setMessage("用户名被占用");
        }
          catch (InsertException e) {
            result.setState(5000); //状态码
            result.setMessage("注册时产生未知的异常");
        }
        return result;
    };
    */

    /**
     * SpringBoot约定大于配置的开发思想，决定了大量的配置甚至注解的编写可被省略。
     * 第一种接收数据（注入）方式：请求处理方法的参数列表设置为pojo类型来接受前端的数据
     * SpringBoot会将前端的url地址中的参数名和pojo类的属性名进行比较，
     * 如果这两个名称相同，则将值注入到pojo类中对应的属性上。
     */
    @RequestMapping("reg")
    public JsonResult<Void> reg(User user) {
        userService.reg(user);
        return new JsonResult<>(OK);
    }

    /**
     * 第二种接收数据（注入）方式：请求处理方法的参数列表设置为非pojo类型
     * SpringBoot会直接将请求的参数名和方法的参数名直接进行比较，
     * 如果名称相同，则自动完成值的依赖注入
     */
    @RequestMapping("login")
    public JsonResult<User> login(String username, String password,
                                  HttpSession session) {
        User limitedUser = userService.login(username, password);
        // 向session对象中完成数据的绑定（session是全局的）
        session.setAttribute("uid", limitedUser.getUid());
        session.setAttribute("username", limitedUser.getUsername());

        // 测验：获取session中绑定的数据
        // 在终端输出
        //System.out.println(getUidFromSession(session));
        //System.out.println(getUsernameFromSession(session));

        return new JsonResult<User>(OK, limitedUser);
    }

    /**
     * 控制层的changePassword()方法实现
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param session
     * @return OK
     */
    @RequestMapping("change_password")
    public JsonResult<Void> changePassword(String oldPassword,
                                           String newPassword,
                                           HttpSession session) {
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.changePassword(uid, username,
                oldPassword, newPassword);
        return new JsonResult<>(OK);
    }

    @RequestMapping("get_by_uid")
    public JsonResult<User> getByUid(HttpSession session) {
        //System.out.println("测试控制层是否启动");
        Integer uid = getUidFromSession(session);
        User data = userService.getByUid(uid);
        //uid和email都是null？修改了UserServiceImpl中public User getByUid(Integer uid)方法的的返回值
        //System.out.println(data.getUid()+" "+data.getPhone()+" "+data.getEmail()+" "+ data.getGender());
        return new JsonResult<>(OK, data);
    }
    // 在url中测试一下

    @RequestMapping("change_info")
    public JsonResult<Void> changeInfo(User user, HttpSession session) {
        // user对象有四部分数据：username、phone、email、gender
        // （html表单中有同名字段，则会自动注入至user）
        // uid还没有，需要从session里再次封装到user对象中。
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.changeInfo(uid, username, user);
        return new JsonResult<>(OK);
    }

}
