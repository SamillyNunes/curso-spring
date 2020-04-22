package com.nunes.sam.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nunes.sam.services.DBService;
import com.nunes.sam.services.EmailService;
import com.nunes.sam.services.MockEmailService;

@Configuration
@Profile("teste") //pra dizer que todos os beans daqui so serao ativados quando o perfil de teste estiver ativado
public class TesteConfig {
	
	@Autowired
	private DBService dbService;
	
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		dbService.instantiateTestDatabase();
		return true; //so pq eh obrigado
	}
	
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}

}
