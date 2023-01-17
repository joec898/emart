package com.jctech.emart.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // api-gateway is based on webFlux
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
		serverHttpSecurity.csrf()
			.disable()
			.authorizeExchange(exchange -> exchange.pathMatchers("/eureka/**") 
			.permitAll()
			.anyExchange()
			.authenticated())
		.oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
		
		return serverHttpSecurity.build();
	}
}
