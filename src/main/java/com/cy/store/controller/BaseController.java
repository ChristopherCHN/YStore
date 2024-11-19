package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.service.ex.*;
import com.cy.store.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

//表示控制层类的基类
public class BaseController {
    //声明一个状态码OK常量，表示操作成功。
    public static final int OK = 200;

    //如果操作不成功，需要建立一个方法，用于统一处理。
    //请求处理方法，这个方法的返回值就是需要传递给前端的数据。
    //自动将异常对象传递给此方法的参数列表上
    //当项目中产生了异常，会被统一拦截到此方法中，这个方法此时就充当了请求处理方法，方法的返回值直接给到前端。
    @ExceptionHandler({ServiceException.class,
                        FileUploadException.class}) //该注解用于统一处理抛出的异常
    public JsonResult<Void> handleException(Throwable e) {
        JsonResult<Void> result = new JsonResult<>(e);
        if (e instanceof UsernameDuplicatedException) {
            result.setState(4000);
            result.setMessage("用户名已经被占用的异常");
        } else if (e instanceof UserNotFoundException) {
            result.setState(4001);
            result.setMessage("用户数据不存在的异常");
        } else if (e instanceof PasswordNotMatchException) {
            result.setState(4002);
            result.setMessage("用户名对应密码错误的异常");
        } else if (e instanceof AddressCountLimitException) {
            result.setState(4003);
            result.setMessage("更新数据时产生未知的异常");
        } else if (e instanceof AddressNotFoundException) {
            result.setState(4004);
            result.setMessage("用户的收货地址不存在的异常");
        } else if (e instanceof AccessDeniedException) {
            result.setState(4005);
            result.setMessage("收货地址数据非法访问的异常");
        } else if (e instanceof InsertException) {
            result.setState(5000);
            result.setMessage("注册时产生未知的异常");
        } else if (e instanceof UpdateException) {
            result.setState(5001);
            result.setMessage("更新数据时产生未知的异常");
        } else if (e instanceof DeleteException) {
            result.setState(5002);
            result.setMessage("删除数据时产生未知的异常");
        /* 后续补上
        else if (e instanceof UsernamePasswordNoValueException) {
            result.setState(???);
            result.setMessage("用户名或密码为空,请输入！");
        } */


        } else if (e instanceof FileEmptyException) {
              result.setState(6000);
              result.setMessage("上传的文件为空，请重新上传！");
        } else if (e instanceof FileSizeException) {
            result.setState(6001);
            result.setMessage("上传的文件大小超过10M，请重新上传！");
        } else if (e instanceof FileTypeException) {
            result.setState(6002);
            result.setMessage("上传的文件类型有误，请重新上传！");
        } else if (e instanceof FileStateException) {
            result.setState(6003);
            result.setMessage("上传的文件状态异常，请关闭其他正在使用本文件的程序后重新上传！");
        } else if (e instanceof FileUploadIOException) {
            result.setState(6004);
            result.setMessage("上传的文件出现IO异常！");
        }

        return result;
    }

    /**
     * 获取session对象中的uid
     * @param session session对象
     * @return 当前登录用户的uid值
     */
    protected final Integer getUidFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("uid").toString());
    }

    /**
     * 获取当前登录用户的username
     * @param session session对象
     * @return 当前登录用户的用户名
     * 在实现类中，重写父类中的toString()，不是句柄信息的输出
     */
    protected final String getUsernameFromSession(HttpSession session) {
        return session.getAttribute("username").toString();
    }



}
