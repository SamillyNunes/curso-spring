package com.nunes.sam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nunes.sam.domain.Cliente;
import com.nunes.sam.repositories.ClienteRepository;
import com.nunes.sam.security.UserSpringSecurity;

//implementacao do user details service. Usada pra buscar o cliente
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente c = clienteRepository.findByEmail(email);
		
		if(c==null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new UserSpringSecurity(c.getId(),c.getEmail(),c.getSenha(),c.getPerfis());
	}

}
