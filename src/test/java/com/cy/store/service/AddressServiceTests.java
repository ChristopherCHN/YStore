package com.cy.store.service;

import com.cy.store.entity.Address;
import com.cy.store.entity.User;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@SpringBootTest注解 表示标注当前的类是一个测试类，不会随同项目一起打包发送
@SpringBootTest
//@RunWith注解 表示启动这个单元测试类。
@RunWith(SpringRunner.class)
public class AddressServiceTests {
    //老版本的idea有检测功能，接口是不能够直接创建Bean的（动态代理技术来解决）
    //没报错就不用管
    @Autowired
    private IAddressService addressService;
    /**
     * 单元测试方法要想单独独立运行（不用启动整个项目即可进行单元测试），必须同时满足的特点如下：
     * 1 必须被Test注解所修饰
     * 2 返回值类型必须是void
     * 3 方法的参数列表不能指定任何类型
     * 4 方法的访问修饰符必须是public
     */
    @Test
    public void addNewAddress() {
        try {
            Integer uid = 3;
            String username = "管理员";
            Address address = new Address();
            address.setName("删除测试4");
            address.setPhone("3004005");
            addressService.addNewAddress(uid, username, address);
        } catch (ServiceException e) {
            //先获取类的对象，再获取类的名称
            System.out.println(e.getClass().getSimpleName());
            //获取异常的具体描述信息（先前自定义了被抛出时打印的内容）
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void setDefault() {
        try {
            Integer aid = 4;
            Integer uid = 8;
            String username = "业务层实现测试管理员";
            addressService.setDefault(aid, uid, username);
        } catch (ServiceException e) {
            //先获取类的对象，再获取类的名称
            System.out.println(e.getClass().getSimpleName());
            //获取异常的具体描述信息（先前自定义了被抛出时打印的内容）
            System.out.println(e.getMessage());
        }
    }

    // 经过测试，不论删除的是否为默认地址，都成功实现了功能。
    @Test
    public void delete() {
        Integer aid = 10;
        Integer uid = 3;
        String username = "删除管理员（默认地址）";
        addressService.delete(aid, uid, username);

    }

}
