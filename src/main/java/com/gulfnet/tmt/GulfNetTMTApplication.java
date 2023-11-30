package com.gulfnet.tmt;

import com.gulfnet.tmt.config.audit.SpringSecurityAuditorAware;
import com.gulfnet.tmt.mapper.UserRoleMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.modelmapper.config.Configuration.AccessLevel;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@ComponentScan(basePackages = {
        "org.springframework.boot.context.embedded.tomcat",
        "com.gulfnet.tmt",
        "com.amazonaws"
})
@EntityScan(basePackages = "com.gulfnet.tmt")
@EnableJpaRepositories(basePackages = "com.gulfnet.tmt")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class GulfNetTMTApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(GulfNetTMTApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new SpringSecurityAuditorAware();
    }

    @Bean
    public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
        // Configure the model mapper, e.g., set field matching strategy
		modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
        // Add the custom converter
        modelMapper.addConverter(new UserRoleMapper());
        return modelMapper;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

}

