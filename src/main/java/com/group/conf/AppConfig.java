package com.group.conf;

import com.group.service.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration//标明此类是一个配置类
public class AppConfig {
    @Bean
    public StudentService studentService(){
        return new StudentService();
    }
}
