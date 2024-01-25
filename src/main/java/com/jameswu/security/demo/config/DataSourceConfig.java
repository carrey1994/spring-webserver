package com.jameswu.security.demo.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    //    @Autowired
    //    private DataSource dataSource;
    //    @Bean(name="entityManagerFactory")
    //    public LocalSessionFactoryBean sessionFactory() {
    //        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
    //        factoryBean.setDataSource(dataSource);
    //        return factoryBean;
    //    }
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
