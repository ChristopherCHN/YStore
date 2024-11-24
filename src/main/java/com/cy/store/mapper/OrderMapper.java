package com.cy.store.mapper;

import com.cy.store.entity.Order;
import com.cy.store.entity.OrderItem;

public interface OrderMapper {

    /**
     * 插入一条订单记录
     * @param order
     * @return
     */
    Integer insertOrder(Order order);

    /**
     * 插入一条订单项记录
     * @param orderItem
     * @return
     */
    Integer insertOrderItem(OrderItem orderItem);

}
