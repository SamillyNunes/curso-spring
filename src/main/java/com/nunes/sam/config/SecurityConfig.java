package com.nunes.sam.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nunes.sam.security.JWTAuthenticationFilter;
import com.nunes.sam.security.JWTUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService; //foi injetada uma interface, o spring sozinho procura uma implementacao dela
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	//pra dizer quem ta liberado de acessar
	private static final String[] PUBLIC_MATCHERS = {
		"/h2-console/**",
	};
	
	//vetor so pra leitura, para que aqueles que nao estejam logados ou nao sejam super usuarios nao possam excluir/editar
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**",
			"/clientes/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//se o perfil teste estiver o ativo, foi o da vez, entao passa esse comando pra ele permitir o acesso
		if(Arrays.asList(env.getActiveProfiles()).contains("teste")) {
			http.headers().frameOptions().disable();
		}
		
		//isso eh necessario pq vai usar multiplas fontes pra acessar o sistema como o email de teste, o app, etc
		http.cors()
			.and().csrf().disable(); //desabilitar o csrf pq nao vai usar sessoes 
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll() //ou seja, so vai permitir o get pra os caras que tao nessa lista caso sejam anonimos
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			.anyRequest().authenticated(); //pra todo o resto sera exigida a autenticacao
		
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //pra assegurar que o backend nao vai criar sessao de usuario
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception { //mecanismo usado pra informar o userDetailsService e o encoder da senha
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	//permitindo o acesso por multiplas fontes com as configuracoes basicas
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() { //metodo usado pra encriptar a senha pra nao a armazene em texto no bd e sim mascarada
		return new BCryptPasswordEncoder();
	}
}
