package com.marcos.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.marcos.cursomc.domain.enums.TipoCliente;
import com.marcos.cursomc.dto.ClienteNewDTO;
import com.marcos.cursomc.resources.exception.handler.FieldMessage;
import com.marcos.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	@Override
	public void initialize(ClienteInsert constraintAnnotation) {
	}

	@Override
	public boolean isValid(ClienteNewDTO value, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<FieldMessage>();

		if(value.getTipo().equals(TipoCliente.PESSOAFISICA.getId()) && !BR.isValidCpf(value.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if(value.getTipo().equals(TipoCliente.PESSOAJURIDICA.getId()) && !BR.isValidCnpj(value.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}
		
		for (FieldMessage e : list) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();

		}

		return list.isEmpty();
	}

}
