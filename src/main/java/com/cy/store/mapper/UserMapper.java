package com.cy.store.mapper;
import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

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

    /**
     * 增删改都会返回一个影响的行数，所以用Integer作返回类型
     * 根据用户的Uid修改用户的密码
     * @param uid 用户的id
     * @param password 用户输入的新密码
     * @param modifiedTime 修改的执行者
     * @param modifiedUser 修改的时间
     * @return 返回值为受影响的行数
     */
    // 多参数传入，用注解@Param绑定，避免错误
    Integer updatePasswordByUid(@Param("uid") Integer uid,
                                @Param("password") String password,
                                @Param("modifiedUser") String modifiedUser,
                                @Param("modifiedTime") Date modifiedTime);

    /**
     * 根据用户的id查询用户的数据
     * @param uid 用户的id
     * @return 如果找到了，返回一个对象，反之返回一个null值
     */
    User findByUid(Integer uid);

    /**
     * 更新用户的信息
     * @param user 用户的数据
     * @return 返回值为受影响的行数
     */
    Integer updateInfoByUid(User user);

    /**
     * 根据用户的uid值，修改用户的头像
     * //@Param注解：需要传递一个参数，是sql映射文件中#{}内部的占位符的变量名。
     * 当sql语句的占位符和映射的接口方法参数名不一致时，需要将某个参数强制注入到相应占位符变量中时可以使用。
     * @param uid
     * @param avatar
     * @param modifiedUser
     * @param modifiedTime
     * @return
     */
    Integer updateAvatarByUid(@Param("uid") Integer uid, //代表后面的uid的值要注入到前面占位符uid中去
                              @Param("avatar") String avatar,
                              @Param("modifiedUser") String modifiedUser,
                              @Param("modifiedTime") Date modifiedTime);

}

