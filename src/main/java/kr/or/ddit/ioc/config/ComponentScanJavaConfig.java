package kr.or.ddit.ioc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:/kr/or/ddit/config/spring/datasource-context.xml")
@ComponentScan(basePackages = {"kr.or.ddit"})
@Configuration
public class ComponentScanJavaConfig {

}
