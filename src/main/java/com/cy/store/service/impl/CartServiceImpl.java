package com.cy.store.service.impl;

import com.cy.store.entity.Cart;
import com.cy.store.entity.District;
import com.cy.store.entity.Product;
import com.cy.store.mapper.CartMapper;
import com.cy.store.mapper.DistrictMapper;
import com.cy.store.mapper.ProductMapper;
import com.cy.store.service.ICartService;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.ex.AccessDeniedException;
import com.cy.store.service.ex.CartNotFoundException;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UpdateException;
import com.cy.store.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    /**
     * 购物车的业务层依赖于购物车的持久层
     */

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void addToCart(Integer uid, Integer pid,
                          Integer amount, String username) {
        // 查询当前要添加的这个购物车是否在表中已存在
        Cart result = cartMapper.findByUidAndPid(uid, pid);
        // 考虑到if和else都要调用，先在外面new出来
        Date date = new Date();

        if (result == null) {
            // 此商品从未被此用户添加到购物车中，则添加新的记录
            Cart cart = new Cart();
            cart.setUid(uid);
            cart.setPid(pid);
            cart.setNum(amount);
            // 价格：是商品模块的数据，不在前端获取
            Product product = productMapper.findProductById(pid);
            cart.setPrice(product.getPrice());
            // 日志的四个字段
            cart.setCreatedUser(username);
            cart.setCreatedTime(date);
            cart.setModifiedUser(username);
            cart.setModifiedTime(date);
            // 执行插入操作
            Integer rows = cartMapper.insert(cart);
            if (rows != 1) {
                throw new InsertException("插入数据时产生未知的异常");
            }
        }
        else {
            // 此商品曾被该用户添加过，这时需要更新商品数量
            Integer num = result.getNum() + amount;
            // 找到cid
            Integer rows = cartMapper.updateNumByCid(
                    result.getCid(),
                    num,
                    username,
                    date);
            if (rows != 1) {
                throw new UpdateException("更新数据时产生未知的异常");
            }
        }
    }

    @Override
    public List<CartVO> getVOByUid(Integer uid) {
        return cartMapper.findVOByUid(uid);
    }

    @Override
    public Integer addNum(Integer cid,
                          Integer uid,
                          String username) {
        Cart result = cartMapper.findByCid(cid);
        if (result == null) {
            throw new CartNotFoundException("购物车记录不存在");
        }
        if (! result.getUid().equals(uid)) {
            throw new AccessDeniedException("数据非法访问！");
        }
        Integer num = result.getNum() + 1;
        Integer rows = cartMapper.updateNumByCid(
                            cid, num,
                            username,
                            new Date());
        if (rows != 1) {
            throw new UpdateException("更新数据失败");
        }
        // 返回新的商品数，准备给前端显示
        return num;
    }

    @Override
    public List<CartVO> getVOByCid(Integer uid, Integer[] cids) {
        List<CartVO> list = cartMapper.findVOByCid(cids);
        // 这个判断是自己加的，为了避免购物车不勾选任何数据而产生异常
        if (list.isEmpty()) {
            throw new CartNotFoundException("未选中任何商品！");
        }

        // 判断查出来的数据是否属于当前用户
        Iterator<CartVO> it = list.iterator();
        while (it.hasNext()) {
            CartVO cartVO = it.next();
            // 如果当前数据不属于当前用户
            if (! cartVO.getUid().equals(uid)) {
                // 从集合中移除这个元素
                // list.remove(cartVO);

                // 迭代器的remove方法，线程安全，不会引发并发异常
                it.remove();
            }
        }
        return list;
    }
}
