package com.cy.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

import static org.springframework.util.unit.DataSize.of;

@Configuration // 表示配置类
@SpringBootApplication
// MapperScan注解，指定当前项目中Mapper路径的位置。在项目启动时，会自动加载所有的接口文件
@MapperScan("com.cy.store.mapper")
public class StoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

    /**
     * 如果配置文件改了，这里就不用写了
     * @return 工厂生成的配置
     */
    /**
    @Bean
    public MultipartConfigElement getMultipartConfigElement() {
        // 创建一个配置的工厂类对象
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置需要创建的对象的相关信息
        factory.setMaxFileSize(of(10,
                DataUnit.MEGABYTES));
        factory.setMaxRequestSize(of(15,
                DataUnit.MEGABYTES));
        // 通过工厂类，创建MultipartConfigElement对象
        return factory.createMultipartConfig();
    }*/
}
