package com.nunes.sam.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.nunes.sam.domain.Cliente;
import com.nunes.sam.domain.Pedido;

public interface EmailService { //especie de contrato pra qualquer estrateigia de envio de email utilizada
	
	void sendOrderConfirmationEmail(Pedido obj); //email de confirmacao de pedido
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	void sendHtmlEmail(MimeMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPass);
	
}
