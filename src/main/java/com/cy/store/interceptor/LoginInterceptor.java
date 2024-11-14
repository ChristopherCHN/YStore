package com.cy.store.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 定义一个拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 检测全局session对象中是否有uid数据，若有则放行，若无则重定向至登录页
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 处理器（url+Controller：映射）
     * @return 若返回true表示放行当前的请求，否则表示拦截当前请求
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // HttpServletRequest对象来获取session对象
        Object obj = request.getSession().getAttribute("uid");
        if (obj == null) {
            // 说明用户未登录，重定向至index.html
            // 参数是localhost:8080/后的浏览器中实际url
            response.sendRedirect("/web/login.html");
            // 结束后续的调用
            return false;
        }
        // 当前请求被放行
        return true;
    }
}
