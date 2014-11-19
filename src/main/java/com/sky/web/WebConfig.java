package com.sky.web;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityConfig;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.sky.web.controller" })
public class WebConfig extends WebMvcConfigurerAdapter {
	@Bean
	public VelocityConfig velocityConfig() {
		VelocityConfigurer cfg = new VelocityConfigurer();
		cfg.setResourceLoaderPath("/WEB-INF/views/");

		Properties props = new Properties();
		props.put(RuntimeConstants.INPUT_ENCODING, "UTF-8");
		props.put(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
		props.put(RuntimeConstants.VM_LIBRARY_AUTORELOAD, "true");
		// props.put(RuntimeConstants.VM_LIBRARY, "velocityMacro.html");
		cfg.setVelocityProperties(props);
		return cfg;

	}

	@Bean
	public VelocityLayoutViewResolver viewResolver() throws IOException {
		VelocityLayoutViewResolver resolver = new VelocityLayoutViewResolver();
		resolver.setCache(false);
		resolver.setPrefix("");
		resolver.setSuffix(".html");
		resolver.setLayoutUrl("layout.html");

		resolver.setContentType("text/html;charset=UTF-8");
		resolver.setViewClass(org.springframework.web.servlet.view.velocity.VelocityLayoutView.class);
		resolver.setExposeSpringMacroHelpers(true);
		resolver.setExposeRequestAttributes(true);
		resolver.setExposeSessionAttributes(false);
		resolver.setRedirectHttp10Compatible(false);
		// resolver.setToolboxConfigLocation("/WEB-INF/velocity-toolbox.xml");
		return resolver;

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**") //
				.addResourceLocations("/resources/**");
	}
}
