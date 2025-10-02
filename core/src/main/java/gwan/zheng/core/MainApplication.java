package gwan.zheng.core;

import gwan.zheng.annotation.EnableTokenValidation;
import gwan.zheng.annotation.EnableWebSecurityConfig;
import gwan.zheng.springbootcommondemo.annotation.EnableApiResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableApiResponse
@EnableWebSecurityConfig
@EnableTokenValidation
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
