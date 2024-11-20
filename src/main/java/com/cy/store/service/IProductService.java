package com.cy.store.service;

import com.cy.store.entity.District;
import com.cy.store.entity.Product;

import java.util.List;

public interface IProductService {

    List<Product> findHotList();

    // 这个函数返回一条商品记录，而非所有记录。
    Product findProductById(Integer id);

}
