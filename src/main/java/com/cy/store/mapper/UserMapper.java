package com.cy.store.mapper;
import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Mapper;

//用户模块的持久层接口
//@Mapper
public interface UserMapper {
    /**
     * 插入用户的数据
     * @param user 用户的数据
     * @return 受影响的行数（在增删改都有受影响行数作为返回值，可以根据返回值判断是否执行成功）
     */
    Integer insert(User user);

    /**
     * 根据用户名，查询用户的数据
     * @param username 用户名
     * @return 如果找到对应的用户则返回这个用户的数据，否则返回null值
     */
    User findByUsername(String username);
}

