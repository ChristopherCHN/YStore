package com.cy.store.mapper;

import com.cy.store.entity.Cart;

import java.util.Date;

public interface CartMapper {

    /**
     * 插入购物车数据
     * @param cart 购物车数据
     * @return 受影响的行数
     */
    Integer insert(Cart cart);


    /**
     * 更新购物车某件商品的数量
     * @param cid 购物车数据id
     * @param num 更新购物车商品数量
     * @param modifiedUser 修改者
     * @param modifiedTime 修改时间
     * @return 受影响的行数
     */
    Integer updateNumber(Integer cid, Integer num,
                         String modifiedUser,
                         Date modifiedTime);

    /**
     * 根据用户的id和商品的id，查询购物车中的数据
     * @param uid 用户id
     * @param pid 商品id
     * @return 购物车记录数据
     */
    Cart findByCidAndPid(Integer uid, Integer pid);
}
