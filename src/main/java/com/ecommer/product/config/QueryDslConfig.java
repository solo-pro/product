package com.ecommer.product.config;

import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLTemplates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ProjectColumnCaseFormat(CaseFormat.LOWER_UNDERSCORE)
public class QueryDslConfig {
    @Bean
    public SQLTemplates postgresTemplates() {
        return PostgreSQLTemplates.builder().build();
    }


}
