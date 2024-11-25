package com.cy.store.service.impl;

import com.cy.store.entity.Address;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.mapper.DistrictMapper;
import com.cy.store.service.IAddressService;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.ex.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 表示新增收货地址的实现类
 */
@Service
public class AddressServiceImpl implements IAddressService {
    // mapper层的依赖
    @Autowired //自动装配注解是为了实现在此方法中借addressMapper调用底层的方法
    private AddressMapper addressMapper;
    // 在添加用户收货地址的业务层，为了将省市区（独立放置在其它层）读进来，
    // 本层依赖于IDistrictService业务层接口。
    // （思考：直接依赖DistrictMapper行不行？）
    @Autowired
    private IDistrictService districtService;

    // 读取配置文件中定义的数量上限常量。@Value注解把配置文件中的值注入到maxCount变量中。
    @Value("${user.address.max-count}")
    private Integer maxCount;

    @Override
    public void addNewAddress(Integer uid, String username, Address address) {
        // 先判断是否达到地址数上限
        Integer count = addressMapper.countByUid(uid); // 调用了持久层的方法
        if (count >= maxCount) { //如果写20，就写死了。可以在配置文件（properties）中自行定义常量。
            throw new AddressCountLimitException("用户收货地址超出上限");
        }

        // 如果用户可以正常获取表单，考虑到添加地址的时候需要拉取数据库中的表单，
        // 所以需要让本业务层的实现类依赖于District的方法
        // 为address对象补全省、市、区
        String provinceName = districtService.getNameByCode(address.getProvinceCode());
        String cityName = districtService.getNameByCode(address.getCityCode());
        String areaName = districtService.getNameByCode(address.getAreaCode());
        address.setProvinceName(provinceName);
        address.setCityName(cityName);
        address.setAreaName(areaName);

        // uid、isDelete、修改者修改时间、创建者创建时间
        address.setUid(uid);
        // 1是默认，0是不默认
        Integer isDefault = count == 0 ? 1 : 0;
        address.setIsDefault(isDefault);

        // 补全4项日志
        address.setCreatedUser(username);
        address.setCreatedTime(new Date());
        address.setModifiedUser(username);
        address.setModifiedTime(new Date());

        // 插入收货地址的方法
        Integer rows = addressMapper.insert(address); // 调用了持久层的方法
        if (rows != 1) {
            throw new InsertException("插入用户的收货地址时产生未知异常");
        }
    }

    @Override
    public List<Address> getByUid(Integer uid) {
        List<Address> list = addressMapper.findByUid(uid);
        /*
        for (Address address : list) {
            // 数据体量较大，可以将不需要展示的字段置为null
            address.setAid(null);
            address.setUid(null);
            address.setProvinceName(null);
            address.setProvinceCode(null);
            address.setCityName(null);
            address.setCityCode(null);
            address.setAreaName(null);
            address.setAreaCode(null);
            address.setZip(null);
            address.setProvinceCode(null);
        }*/
        return list;
    }

    @Override
    public void setDefault(Integer aid, Integer uid, String username) {
        Address result = addressMapper.findByAid(aid);
        if (result == null) {
            // 没有找到相应的地址数据
            throw new AddressNotFoundException("收货地址不存在");
        }
        // 找到了地址，但不属于当前用户，这是非法访问
        // 检测当前获取到的收货地址数据的归属
        // 后面的uid是从session中获取到的
        if (! (result.getUid().equals(uid))) {
            throw new AccessDeniedException("访问数据非法");
        }
        // 先将所有的收货地址设置为非默认
        Integer rows = addressMapper.updateNonDefault(uid);
        if (rows < 1) {
            throw new UpdateException("更新数据产生未知的异常");
        }
        // 将用户选中的某条地址设置为默认收货地址
        Integer rows2 = addressMapper.updateDefaultByAid(aid,
                username, new Date());
        if (rows2 != 1) {
            throw new UpdateException("更新数据产生未知的异常");
        }
    }

    @Override
    public void delete(Integer aid, Integer uid, String username) {
        Address result = addressMapper.findByAid(aid);
        if (result == null) {
            throw new AddressNotFoundException("收货地址数据不存在");
        }
        if (! (result.getUid().equals(uid))) {
            throw new AccessDeniedException("访问数据非法");
        }

        // 如果是默认，应该执行更改默认
        if (result.getIsDefault() == 1) {
            Integer rows = addressMapper.deleteByAid(aid);
            if (rows != 1) {
                throw new DeleteException("删除地址时产生异常");
            }
            // 删除后用户还剩几条地址记录
            Integer count = addressMapper.countByUid(uid);
            // 如果删完了之后用户就没有地址数据了
            if (count == 0) {
                return;
            }
            // 删掉了默认，查询最新地址并定义为默认
            Address address = addressMapper.findLastModified(uid);
            Integer newDefaultAid = address.getAid();
            // 先将所有的收货地址设置为非默认
            Integer rows2 = addressMapper.updateNonDefault(uid);
            if (rows2 < 1) {
                throw new UpdateException("更新数据产生未知的异常");
            }
            // 将用户选中的某条地址设置为默认收货地址
            Integer rows3 = addressMapper.updateDefaultByAid(
                    newDefaultAid,
                    username, new Date());
            if (rows3 < 1) {
                throw new UpdateException("更新数据产生未知的异常");
            }
        }
        else {
            Integer rows = addressMapper.deleteByAid(aid);
            if (rows != 1) {
                throw new DeleteException("删除地址时产生异常");
            }
            // 删除后用户还剩几条地址记录
            Integer count = addressMapper.countByUid(uid);
            // 如果删完了之后用户就没有地址数据了
            if (count == 0) {
                return;
            }
            // 删掉的不是默认，不需要修改默认
        }

    }

    @Override
    public Address getByAid(Integer aid, Integer uid) {
        Address address = addressMapper.findByAid(aid);
        if (address == null) {
            throw new AddressNotFoundException("收货地址数据不存在");
        }
        if (! address.getUid().equals(uid)) {
            throw new AccessDeniedException("非法数据访问");
        }
        return address;
    }
}
