package com.cy.store.mapper;

import com.cy.store.entity.District;
import com.cy.store.entity.Order;
import com.cy.store.entity.OrderItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

//@SpringBootTest注解 表示标注当前的类是一个测试类，不会随同项目一起打包发送
@SpringBootTest
//@RunWith注解 表示启动这个单元测试类。
@RunWith(SpringRunner.class)
public class OrderMapperTests {
    //老版本的idea有检测功能，接口是不能够直接创建Bean的（动态代理技术来解决）
    //没报错就不用管
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 单元测试方法要想单独独立运行（不用启动整个项目即可进行单元测试），必须同时满足的特点如下：
     * 1 必须被Test注解所修饰
     * 2 返回值类型必须是void
     * 3 方法的参数列表不能指定任何类型
     * 4 方法的访问修饰符必须是public
     */
    @Test
    public void insertOrder() {
        Order order = new Order();
        order.setUid(1);
        order.setRecvName("明明");
        order.setPayTime(new Date());
        Integer i = orderMapper.insertOrder(order);
        System.out.print(i);
    }

    @Test
    public void insertOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOid(1);
        orderItem.setPid(1);
        orderItem.setTitle("title");
        orderMapper.insertOrderItem(orderItem);
    }



}
