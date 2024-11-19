package com.cy.store.mapper;

import com.cy.store.entity.Address;
import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Param;
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
public class AddressMapperTests {
    //老版本的idea有检测功能，接口是不能够直接创建Bean的（动态代理技术来解决）
    //没报错就不用管
    @Autowired
    private AddressMapper addressMapper;
    /**
     * 单元测试方法要想单独独立运行（不用启动整个项目即可进行单元测试），必须同时满足的特点如下：
     * 1 必须被Test注解所修饰
     * 2 返回值类型必须是void
     * 3 方法的参数列表不能指定任何类型
     * 4 方法的访问修饰符必须是public
     */
    @Test
    public void insert() {
        Address address = new Address();
        // aid自增，不要赋值
        address.setUid(1);
        address.setName("删除测试3");
        address.setPhone("777777");
        address.setModifiedTime(new Date());
        addressMapper.insert(address);
    }

    @Test
    public void countByUid() {
        Integer uid = 8;
        Integer count = addressMapper.countByUid(uid);
        System.out.println(count);
    }

    // 之所以只输出BaseEntity，是因为toString()出了问题
    @Test
    public void findByUid() {
        Integer uid = 8;
        List<Address> la = addressMapper.findByUid(uid);
        // 集合可以直接输出结果，无需遍历
        // System.out.println(la);

        for (Address a : la) {
            System.out.println(a);
        }
    }

    @Test
    public void findByAid() {
        Integer aid = 2;
        Address a = addressMapper.findByAid(aid);
        System.out.println(a);
    }

    @Test
    public void updateNonDefault() {
        Integer uid = 8;
        int i = addressMapper.updateNonDefault(uid);
        System.out.println(i);
    }

    @Test
    public void updateDefaultByAid() {
        Integer aid = 3;
        String modifiedUser = "系统默认设置管理员";
        Date modifiedTime = new Date();
        int b = addressMapper.updateDefaultByAid(aid, modifiedUser, modifiedTime);
        System.out.println(b);
    }

    @Test
    public void deleteByAid() {
        int k = addressMapper.deleteByAid(1);
        System.out.println(k);
    }

    @Test
    public void findLastModified() {
        Integer uid = 8;
        Address a = addressMapper.findLastModified(uid);
        System.out.println(a);
    }

}
