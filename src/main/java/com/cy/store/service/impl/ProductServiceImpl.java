package com.cy.store.service.impl;

import com.cy.store.entity.District;
import com.cy.store.entity.Product;
import com.cy.store.mapper.DistrictMapper;
import com.cy.store.mapper.ProductMapper;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.IProductService;
import com.cy.store.service.ex.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> findHotList() {
        List<Product> list = productMapper.findHotList();
        // 为保结果完整，暂不置结果中的某些属性为null。
        return list;
    }

    @Override
    public Product findProductById(Integer id) {
        Product product = productMapper.findProductById(id);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        return product;
    }
}
