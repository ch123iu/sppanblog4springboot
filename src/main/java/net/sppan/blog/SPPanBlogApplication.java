package net.sppan.blog;

import javax.annotation.Resource;

import net.sppan.blog.config.intercepter.CommonIntercepter;
import net.sppan.blog.config.intercepter.LoginIntercepter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class SPPanBlogApplication  extends WebMvcConfigurerAdapter {
	
	@Autowired
    private CommonIntercepter commonInterceptor;
	@Resource
	private LoginIntercepter loginIntercepter;

	public static void main(String[] args) {
		SpringApplication.run(SPPanBlogApplication.class, args);
	}
	
	  /**
     * add interceptors
     *
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(commonInterceptor)
                .addPathPatterns("/**");
//        registry.addInterceptor(loginIntercepter).addPathPatterns("/admin/**")
//        		.excludePathPatterns("/admin/login");
    }
}
