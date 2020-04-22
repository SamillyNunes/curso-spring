package com.nunes.sam.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.nunes.sam.domain.Pedido;

public abstract class AbstractEmailService implements EmailService{

	@Value("${default.sender}") //pegando la no aplication.properties
	private String sender; 
	
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(sm); //template method: vc consegue usar um metodo que ainda nem foi implementado
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) { //protected pq subclasses podem usar
		
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail()); //pra quem que vai ser essa mensagem?
		sm.setFrom(sender); //o remetente padrao
		sm.setSubject("Pedido confirmado! Código: "+obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis())); //criada umma data com o servidor
		sm.setText(obj.toString());
		
		return sm ;
	}
}
