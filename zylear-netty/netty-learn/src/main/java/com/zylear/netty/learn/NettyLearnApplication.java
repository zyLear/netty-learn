package com.zylear.netty.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.zylear.netty.learn.*")
public class NettyLearnApplication /*extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer*/ {

	public static void main(String[] args) {
		SpringApplication.run(NettyLearnApplication.class, args);
	}

//	@Override
//	public void customize(ConfigurableEmbeddedServletContainer container) {
//		container.setPort(8152);
//		container.setContextPath("/blokus");
//	}
}
