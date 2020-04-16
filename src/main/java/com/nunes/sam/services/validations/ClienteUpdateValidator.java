package com.nunes.sam.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.nunes.sam.domain.Cliente;
import com.nunes.sam.dto.ClienteDTO;
import com.nunes.sam.repositories.ClienteRepository;
import com.nunes.sam.resources.exceptions.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private HttpServletRequest request; //usado para pegar o id no endereco, ja que na atualizacao ele eh passado so no endereco endpoint
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteUpdate ann) { //metodo para alguma programacao de inicicializacao	
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) { //esse eh o metodo que verifica e faz a validacao personalizada
	
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); //ta fazendo o casting para o map. isso eh usado para pegar as variaveis de uri que tao na requisicao 
		
		Integer uriId = Integer.parseInt(map.get("id"));
		
		//Essa lista eh responsavel por ter os erros, caso hajam. Se ela for vazia ta ok, senao...
		List<FieldMessage> list = new ArrayList<>();

		// inclua os testes aqui, inserindo erros na lista
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux!= null && !aux.getId().equals(uriId)) { //se os ids nao forem iguais, significa que sao pessoas diferentes
			list.add(new FieldMessage("email", "Email já existente."));
		}
		
		//se tiver alguem na lista, entao devera ser adicionado esses erros no framework (padrao)
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
