package com.cy.store.controller;

import com.cy.store.entity.District;
import com.cy.store.entity.Order;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.IOrderService;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController extends BaseController{
    @Autowired
    private IOrderService orderService;
    // @RequestMapping但注解还不能省略，传入的是一个斜杠或空，都行
    @RequestMapping("create")
    // 从前端传来“属性=值”的数据，自动注入方法的参数。
    public JsonResult<Order> create(Integer aid, Integer[] cids,
                                    HttpSession session) {
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        Order data = orderService.create(aid, uid, username, cids);
        return new JsonResult<Order>(OK, data);
    }
}
