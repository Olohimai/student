package uk.ac.leedsbeckett.student;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.ac.leedsbeckett.student.controller.StudentInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StudentInterceptor studentInterceptor;

    public WebConfig(StudentInterceptor studentInterceptor) {
        this.studentInterceptor = studentInterceptor;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/signup").setViewName("signup");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/profile").setViewName("profile");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(studentInterceptor).addPathPatterns("/courses/**", "/home", "/enrolments/**", "/profile/**", "/graduation");
    }
}
