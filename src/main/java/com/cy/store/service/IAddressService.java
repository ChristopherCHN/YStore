package com.cy.store.service;

import com.cy.store.entity.Address;

import java.util.List;

// 收货地址业务层接口
public interface IAddressService {

    // 这是为了从uid获取到登录用户的名字，以填充“创建人”字段。
    void addNewAddress(Integer uid, String username,
                       Address address);

    List<Address> getByUid(Integer uid);

    /**
     * 修改某个用户的某条收货地址数据为默认收货地址
     * @param aid 收货地址的id值
     * @param uid 用户的id
     * @param username 修改执行者
     */
    void setDefault(Integer aid, Integer uid, String username);


    /**
     * 删除用户选中的收货地址数据
     * @param aid 收货地址id
     * @param uid 用户的id
     * @param username 用户名（为了更新modified_name字段）
     */
    void delete(Integer aid, Integer uid, String username);

    /**
     *
     * @param aid
     * @return
     */
    Address getByAid(Integer aid, Integer uid);

}
