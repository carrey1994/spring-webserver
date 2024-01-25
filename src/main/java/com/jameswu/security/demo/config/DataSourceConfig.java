package com.jameswu.security.demo.config;

// @Configuration
// @EnableTransactionManagement
// public class DataSourceConfig {
//
//    @Autowired
//    private DataSource dataSource;
//    @Bean(name="entityManagerFactory")
//    public LocalSessionFactoryBean sessionFactory() {
//        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        return factoryBean;
//    }
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(sessionFactory().getObject());
//        return transactionManager;
//    }
// }
