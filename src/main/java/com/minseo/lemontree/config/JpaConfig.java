package com.minseo.lemontree.config;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * class: JpaConfig.
 *
 * @author devminseo
 * @version 8/10/24
 */
@Configuration
public class JpaConfig {

    @Bean
    public DataSource getDataSource(){
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/lemontree");
        dataSource.setUsername("root");
        dataSource.setPassword("minseo1234");

        dataSource.setInitialSize(20);
        dataSource.setMaxIdle(20);
        dataSource.setMinIdle(20);
        dataSource.setMaxTotal(20);

        dataSource.setMaxWaitMillis(20);

        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);

        return dataSource;
    }
}
