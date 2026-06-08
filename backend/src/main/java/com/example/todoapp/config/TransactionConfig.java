package com.example.todoapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据库事务配置类
 * 启用基于注解的事务管理
 */
@Slf4j
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    
    // 启用事务管理，通过@Transactional注解来管理事务
}
