package com.nunes.sam.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nunes.sam.services.DBService;

@Configuration
@Profile("dev") //pra dizer que todos os beans daqui so serao ativados quando o perfil de teste estiver ativado
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}") //esse cara pega la o que tem nesse valor no aplication properties respectivo
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		if(!"create".equals(strategy)) { //se nao for create, o bd ja existe e ee nao precisa criar de novo/instanciar de novo os dados
			
			return false;
		}
		
		dbService.instantiateTestDatabase();
		return true; //so pq eh obrigado
	}

}
