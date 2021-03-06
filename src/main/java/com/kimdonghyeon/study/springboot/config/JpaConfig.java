package com.kimdonghyeon.study.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  // JAP Auditing 활성화
public class JpaConfig {}
