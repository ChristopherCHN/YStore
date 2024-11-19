package com.cy.store.controller;

import com.cy.store.entity.District;
import com.cy.store.service.IDistrictService;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("districts")
public class DistrictController extends BaseController{
    @Autowired
    private IDistrictService districtService;

    // 凡是以districts开头的请求，都被拦截到getByParent()方法
    // @RequestMapping但注解还不能省略，传入的是一个斜杠或空，都行
    @RequestMapping({"/",""})
    // 从前端传来“属性=值”的数据，自动注入方法的参数。
    public JsonResult<List<District>> getByParent(String parent) {
        // 向下，从业务层接收数据
        List<District> data = districtService.getByParent(parent);
        // 向上，向前端返回数据
        return new JsonResult<>(OK, data);
    }
}
