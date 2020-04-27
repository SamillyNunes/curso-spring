package com.nunes.sam.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nunes.sam.domain.Cliente;
import com.nunes.sam.repositories.ClienteRepository;
import com.nunes.sam.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random(); 
	
	public void sendNewPassword(String email) {
		
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if(cliente==null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassword();
		System.out.println("NEW PASS:"+ newPass);
		cliente.setSenha(bCryptPasswordEncoder.encode(newPass));
		
		clienteRepository.save(cliente);
		
		emailService.sendNewPasswordEmail(cliente, newPass);
		
	}

	private String newPassword() {
		char[] vet = new char[10];
		for(int i=0;i<10;i++) {
			vet[i] = randomChar(); 
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		if(opt==0) { //gera um digito
			return (char) (rand.nextInt(10)+48); //gera um no de 0 a 9 [10 digitos] e soma c 48 pq eh o codigo na tabela unicode do 0
		} else if (opt==1) { //gera uma letra maiuscula
			return (char) (rand.nextInt(26)+65);
		} else { //gera minuscula
			return (char) (rand.nextInt(26)+97);
		}
	}
}
