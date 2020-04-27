package com.nunes.sam.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	private JWTUtil jwtUtil;
	//precisamos dele pq esse filtro vai ter que analizar o token, e pra isso eh preciso extrair o usuario dele e buscar no bd
	private UserDetailsService userDetailsService; 
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager,JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil=jwtUtil;
		this.userDetailsService=userDetailsService; 
	}
	
	//metodo que intercepta a requisicao e ve se o cara esta autorizado. Ele executa algo antes de deixar a requisicao continuar
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//pegando o cabecalho da requisicao, a parte de authorization que a gente retornou na requisicao quando tava ok
		String header = request.getHeader("Authorization"); //eh aquele Bearer 485a4d88g5s4a785g4...asdefs5895a
		
		if(header!=null && header.startsWith("Bearer ")) { //testar se existe o authorization mesmo e se comeca com o Bearer. Logo, ta no caminho certo
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7)); //comeca do indice 
		
			if(auth!=null) { // ele pode ser nulo caso o token esteja invalido
				SecurityContextHolder.getContext().setAuthentication(auth);
				
			}
		}
		
		chain.doFilter(request, response); //fala para o filtro que ele pode continuar a execucao da requisicao
		
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {

		if(jwtUtil.tokenValido(token)) {
			String username  = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username); //buscar no banco de dados pelo metodo da classe implementada
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		
		return null;
	}

}
