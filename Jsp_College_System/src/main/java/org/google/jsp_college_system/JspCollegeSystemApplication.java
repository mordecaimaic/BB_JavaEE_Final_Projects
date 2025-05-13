package org.google.jsp_college_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class JspCollegeSystemApplication
        extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JspCollegeSystemApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(JspCollegeSystemApplication.class, args);
    }
}
