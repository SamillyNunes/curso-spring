package com.nunes.sam.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component //para que possa ser injetada em outras classes como componente
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis()+expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
	public boolean tokenValido(String token) {
		Claims claims = getClaims(token); //claims eh um obj que armazena as reivindicacoes do token
	
		if(claims!=null) {
			String username = claims.getSubject(); //retorna o usuario
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			
			if(username!= null && expirationDate!=null && now.before(expirationDate)) { //ou seja, o meu instante atual ainda eh anterior a data de expiracao
				return true;
			}
			
		}
		
		return false;
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch(Exception e) {
			return null;
		}
		
	}
	
	public String getUsername(String token) {
		Claims claims = getClaims(token); //claims eh um obj que armazena as reivindicacoes do token
	
		if(claims!=null) {
			return  claims.getSubject(); //retorna o usuario
			
		}
		
		return null;
		
	}

}
