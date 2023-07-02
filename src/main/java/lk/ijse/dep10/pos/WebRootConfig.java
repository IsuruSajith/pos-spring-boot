package lk.ijse.dep10.pos;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class WebRootConfig {

    @Bean
    public BasicDataSource dataSource(Environment env){
        BasicDataSource bds = new BasicDataSource();
        bds.setUsername(env.getRequiredProperty("spring.datasource.username"));
        bds.setPassword(env.getRequiredProperty("spring.datasource.password"));
        bds.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
        bds.setUrl(env.getRequiredProperty("spring.datasource.url"));
        bds.setMaxTotal(env.getRequiredProperty("spring.datasource.dbcp2.max-total", Integer.class));
        bds.setInitialSize(env.getRequiredProperty("spring.datasource.dbcp2.initial-size", Integer.class));
        return bds;
    }
}
