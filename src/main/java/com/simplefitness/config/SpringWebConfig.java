package com.simplefitness.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@ServletComponentScan
@EnableJpaAuditing
public class SpringWebConfig {
	
}
