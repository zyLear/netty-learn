package com.zylear.netty.learn.util;


import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author 28444
 * @date 2018/1/10.
 */
public class MybatisDataSourceConfigHelper {


    private static final Logger logger = LoggerFactory.getLogger(MybatisDataSourceConfigHelper.class);
    private static String validation_query = "SELECT 1 FROM DUAL";


    public static DataSourceTransactionManager getTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource);
        return txManager;
    }

//    public static DataSourceHealthIndicator getDataSourceHealthIndicator(DataSource dataSource) {
//        return new DataSourceHealthIndicator(dataSource, validation_query);
//    }

    public static SqlSessionFactory getSqlSessionFactory(List<String> xmlPaths, String configPath, DataSource dataSource) {
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            List<Resource> resources = new ArrayList();
            Iterator i$ = xmlPaths.iterator();

            while (i$.hasNext()) {
                String xmlPath = (String) i$.next();
                Resource[] t = (new PathMatchingResourcePatternResolver()).getResources(xmlPath);
                resources.addAll(Arrays.asList(t));
            }

            if (!resources.isEmpty()) {
                bean.setMapperLocations((Resource[]) resources.toArray(new Resource[resources.size()]));
            }

            if (!StringUtils.isEmpty(configPath)) {
                bean.setConfigLocation((new PathMatchingResourcePatternResolver()).getResource(configPath));
            }

            bean.setDataSource(dataSource);
            return bean.getObject();
        } catch (Exception var8) {
            logger.error("failed to create data sql session", var8);
            throw new RuntimeException("failed to create data sql session", var8);
        }
    }

//    public static DataSource createDataSource(String url, String username, String password) {
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        dataSource.setMaxIdle(30);
//        dataSource.setMinIdle(10);
//        dataSource.setMaxActive(50);
//        dataSource.setRemoveAbandoned(true);
//        dataSource.setRemoveAbandonedTimeout(280);
//        dataSource.setMaxWait(30000L);
//        dataSource.setValidationQuery(validation_query);
//        dataSource.setTestOnBorrow(true);
//        return dataSource;
//    }

    public static DataSource createDruidDataSource(String url, String username, String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(50);
        dataSource.setMaxWait(30000L);
        dataSource.setInitialSize(10);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(280);
        dataSource.setTimeBetweenEvictionRunsMillis(60000L);
        dataSource.setMinEvictableIdleTimeMillis(300000L);
        dataSource.setValidationQuery(validation_query);
        dataSource.setTestOnBorrow(true);
        dataSource.setTimeBetweenLogStatsMillis(60000L);
        return dataSource;
    }
}

