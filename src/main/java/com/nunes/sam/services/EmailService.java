package com.nunes.sam.services;

import org.springframework.mail.SimpleMailMessage;

import com.nunes.sam.domain.Pedido;

public interface EmailService { //especie de contrato pra qualquer estrateigia de envio de email utilizada
	
	void sendOrderConfirmationEmail(Pedido obj); //email de confirmacao de pedido
	
	void sendEmail(SimpleMailMessage msg);
	
}
