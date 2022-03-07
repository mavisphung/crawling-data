package com.example.crawlingdata.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Xử lí truy cập vào resources css, fonts, images, js
@Configuration
//không nên dùng thằng dưới vì nó sẽ disable tự động config resources
@EnableWebMvc
public class ResourcesConfig implements WebMvcConfigurer {
	
	//Mặc định nó sẽ đi vào /webapp/resources
	//để đúng với những gì mình cần thì phải trỏ thẳng vào /resources/
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//lấy hết toàn bộ trong resources/static
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        
		//chỉ mỗi dòng này hoạt động
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        
        //registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/resources/");
	}
	
	
}