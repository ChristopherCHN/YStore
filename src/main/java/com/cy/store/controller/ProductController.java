package com.cy.store.controller;

import com.cy.store.entity.District;
import com.cy.store.entity.Product;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.IProductService;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController extends BaseController{
    @Autowired
    private IProductService productService;

    // 凡是以districts开头的请求，都被拦截到getByParent()方法
    // @RequestMapping但注解还不能省略，传入的是一个斜杠或空，都行
    @RequestMapping("hot_list")
    // 从前端传来“属性=值”的数据，自动注入方法的参数。
    public JsonResult<List<Product>> getHotList() {
        List<Product> data = productService.findHotList();
        //System.out.println(data);
        return new JsonResult<>(OK, data);
    }

    /*
    // 优化，用于product.html?id=#{id}调用。
    // 模仿了RestFul风格的参数调用。
    @RequestMapping("details")
    public JsonResult<Product> getProductDetails(@RequestParam("id") Integer id) {
        System.out.println(id);
        Product product = productService.findProductById(id);
        System.out.println("控制层");
        System.out.println("product");
        return new JsonResult<>(OK,product);
    }
    */

    // 模仿了RestFul风格的参数调用。
    // getMapping只能发送get类型的请求
    @RequestMapping("{id}/details")
    public JsonResult<Product> getProductDetails(@PathVariable("id") Integer id) {
        //System.out.println(id);
        Product product = productService.findProductById(id);
        //System.out.println("控制层");
        //System.out.println("product");
        return new JsonResult<Product>(OK, product);
    }


}
