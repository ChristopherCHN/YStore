package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicatedException;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    // 文件大小限制：单位是字节，10M = 10*1024*1024
    public static final int AVATAR_MAX_SIZE = 10*1024*1024;
    // 文件类型限制：定义一个
    public static final List<String> AVATAR_TYPE = new ArrayList<>();
    static { // 静态块用于给List添加数据（或赋值）
        AVATAR_TYPE.add("image/jpeg"); //包含了jpg
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");
    }

    /**
     * MultipartFile是SpringMVC提供的一个可以接收任何类型文件的接口
     * 为我们包装了获取文件类型的数据。
     * SpringBoot又整合了SpringMVC，只需要在处理请求的方法参数列表上声明一个参数类型为MultipartFile的参数，
     * SpringBoot就会自动地将文件当中的数据赋值给这个参数。
     * // @RequestParam用于强行匹配：html标签名对应内容→注入→请求参数名
     * @param session
     * @param file
     * @return
     */
    @RequestMapping("change_avatar")
    public JsonResult<String> changeAvatar(HttpSession session,
                                           @RequestParam("file") MultipartFile file) {
        // 在这个方法内排除一系列异常
        // 方法外定义常量：文件大小上限、文件类型限制

        // 判断文件是否为空
        if (file.isEmpty()) {
            throw new FileEmptyException("文件为空");
        }
        // 判断文件大小
        if (file.getSize() > AVATAR_MAX_SIZE) {
            throw new FileSizeException("文件大小超出限制");
        }
        // 判断文件类型是否符合要求
        String contentType = file.getContentType();
        System.out.println(contentType);
        // 如果集合包含某个元素
        if (! AVATAR_TYPE.contains(contentType)) {
            throw new FileTypeException("文件类型不支持上传");
        }

        // [后期有必要修改]上传的文件，目录结构是.../upload/文件.jpg
        String parent = session.getServletContext()
                        .getRealPath("upload");
        // File对象指向这个路径，File是否存在
        File dir = new File(parent);
        if (!dir.exists()) {
            dir.mkdirs(); // 不存在就创建一个新的目录
            System.out.println(parent); //用于测试整个项目
        }
        // 为了杜绝直接覆盖，需要获取这个文件名称：UUID工具类生成一个新的字符串，作为文件名
        String originalFilename = file.getOriginalFilename();
        // 例如：avatar01.png（原名带后缀）
        System.out.println("originalFilename: " + originalFilename);
        // 获取字符串中最后一个小数点的位置
        int index = 0;
        try {
            index = originalFilename.lastIndexOf(".");
        } catch (FileUploadException e) {
            throw new FileTypeException("文件无扩展名！");
        }
        // 截取文件扩展名
        String suffix = originalFilename.substring(index);
        // 生成一个文件名并拼接扩展名
        String filename =
                UUID.randomUUID().toString().toUpperCase()
                + suffix;
        // 创建一个当前的名字的空文件
        File dest = new File(dir, filename);
        // 将参数file中的数据写入空文件中
        try {
            file.transferTo(dest); // 把file内的数据写入到了后缀相同的目标文件dest中
        } catch (FileStateException e) {
            throw new FileStateException("文件状态异常！");
        } catch (IOException e) {
            throw new FileUploadIOException("文件读写异常！");
        }

        // 之后可以在这里添加一段代码，把图片尺寸修改成统一的尺寸再上传保存
        // 为了在前端也能显示大小一致的图片，在css中也有必要更改图片显示控件的一些属性

        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        // 返回头像的路径 /upload/test.png
        // 这里的/upload/之前，是服务器的某个位置
        // 例如：C:\Users\Christopher\AppData\Local\Temp\tomcat-docbase.8080.4048066700806376762/
        String avatar = "/upload/" + filename;
        userService.changeAvatar(uid, avatar, username);

        // 返回头像的路径给前端页面，将来用于头像的展示使用
        return new JsonResult<>(OK, avatar);
    }
}
