package com.nunes.sam.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nunes.sam.domain.Pedido;

public abstract class AbstractEmailService implements EmailService{

	@Value("${default.sender}") //pegando la no aplication.properties
	private String sender; 
	
	@Autowired
	private TemplateEngine templateEngine; //usado pra processar o template e povoar
	
	@Autowired
	private JavaMailSender javaMailSender;
	
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
	
	protected String htmlFromTemplatePedido(Pedido obj) {
		Context context = new Context(); //usado pra acessar o template
		context.setVariable("pedido", obj); //tem que ser o mesmo apelido que esta sendo usado no template. Pedido eh o apelido para o obj
		return templateEngine.process("email/confirmacaoPedido", context); //tem que passar o caminho do html
	}
	
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		try {
			MimeMessage mm = prepareMimeMessageFromPedido(obj);
			sendHtmlEmail(mm);
		} catch(MessagingException e) {
			sendOrderConfirmationEmail(obj); //se der um erro no processamento, a gente envia de maneira simples
		}
		
		
	}

	protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido confirmado! Código: "+obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true); //o true eh indicando que o conteudo eh um html
		
		return mimeMessage;
	}


}
