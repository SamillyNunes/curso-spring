package com.nunes.sam.services.validations;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nunes.sam.domain.enums.TipoCliente;
import com.nunes.sam.dto.ClienteNewDTO;
import com.nunes.sam.resources.exceptions.FieldMessage;
import com.nunes.sam.services.validations.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	@Override
	public void initialize(ClienteInsert ann) { //metodo para alguma programacao de inicicializacao	
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) { //esse eh o metodo que verifica e faz a validacao personalizada
		//Essa lista eh responsavel por ter os erros, caso hajam. Se ela for vazia ta ok, senao...
		List<FieldMessage> list = new ArrayList<>();

		// inclua os testes aqui, inserindo erros na lista
		
		if(objDto.getTipo().equals(TipoCliente.PESSOA_FISICA.getCodigo()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF Inválido"));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOA_JURIDICA.getCodigo()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ Inválido"));
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
