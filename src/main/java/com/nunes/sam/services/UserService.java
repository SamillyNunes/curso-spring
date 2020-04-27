package com.nunes.sam.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.nunes.sam.security.UserSpringSecurity;

public class UserService {
	
	//metodo pra retornar o atual usuario logado e se nao houver retorna nulo
	public static UserSpringSecurity authenticated() {
		
		try {
			//retorna o usuario q ta logado no sistema
			return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch(Exception e) {
			return null;
		}
		 
	}
}
