package com.cy.store.mapper;

import com.cy.store.entity.Address;
import com.cy.store.entity.District;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 表示收货地址持久层的接口
 */
public interface AddressMapper {
    /**
     * 插入用户的收货地址数据
     * @param address 收货地址数据
     * @return 受影响的行数
     */
    Integer insert(Address address); // 接收业务层的调用，向下传给数据库

    /**
     * 根据用户的id，统计收货地址数量
     * @param uid 用户的id
     * @return 当前用户的收货地址总数
     */
    Integer countByUid(Integer uid);

    /**
     * 根据用户的id，查询用户的收货地址数据
     * @param uid 用户id
     * @return 收货地址数据
     */
    List<Address> findByUid(Integer uid);

    /**
     * 根据aid查询收货地址数据
     * @param aid 收货地址
     * @return 收货地址数据。如果没有找到，则返回null值
     */
    Address findByAid(Integer aid);

    /**
     * 根据用户的uid值，将用户所有收货地址都修改为非默认
     * @param uid 用户id
     * @return 受影响的行数
     */
    Integer updateNonDefault(Integer uid);

    /**
     * 修改默认的地址
     * @param aid 地址的aid
     * @return 受影响的行数
     */
    Integer updateDefaultByAid(@Param("aid") Integer aid,
                               @Param("modifiedUser") String modifiedUser,
                               @Param("modifiedTime") Date modifiedTime);


    /**
     * 根据收货地址id删除收货地址数据
     * @param aid 收货地址的id
     * @return 受影响的行数
     */
    Integer deleteByAid(Integer aid);

    /**
     * 根据用户uid，查询回最近修改的地址
     * @param uid 用户uid
     * @return 地址记录
     */
    Address findLastModified(Integer uid);

}
