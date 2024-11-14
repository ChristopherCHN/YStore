package com.cy.store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class StoreApplicationTests {

    //@Autowired(required = false) //自动装配
    @Autowired
    private DataSource dataSource;
    @Test
    void contextLoads() {
    }

    //HikariProxyConnection@1532675992 wrapping com.mysql.cj.jdbc.ConnectionImpl@42383cb0
    //hikari是一个数据连接池，其他的连接池有DBCP、C3P0等
    @Test
    void getConnection() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

}
