package com.cy.store.controller;

import com.cy.store.entity.District;
import com.cy.store.service.ICartService;
import com.cy.store.service.IDistrictService;
import com.cy.store.util.JsonResult;
import com.cy.store.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("carts")
public class CartController extends BaseController{
    @Autowired
    private ICartService cartService;

    // 凡是以districts开头的请求，都被拦截到getByParent()方法
    // @RequestMapping但注解还不能省略，传入的是一个斜杠或空，都行
    @RequestMapping("add_to_cart")
    // 从前端传来“属性=值”的数据，自动注入方法的参数。
    public JsonResult<Void> addToCart(Integer pid,
                                      Integer amount,
                                      HttpSession session) {
        cartService.addToCart(
                getUidFromSession(session),
                pid,
                amount,
                getUsernameFromSession(session));

        return new JsonResult<>(OK);
    }

    @RequestMapping({"","/"})
    //
    public JsonResult<List<CartVO>> getVOByUid(HttpSession session) {
        Integer uid = getUidFromSession(session);
        List<CartVO> data = cartService.getVOByUid(uid);
        return new JsonResult<>(OK, data);
    }

    @RequestMapping("{cid}/num/add")
    public JsonResult<Integer> addNum(HttpSession session,
                                      @PathVariable("cid") Integer cid) {
        Integer data = cartService.addNum(cid,
                                            getUidFromSession(session),
                                            getUsernameFromSession(session));
        return new JsonResult<>(OK, data);
    }
}
