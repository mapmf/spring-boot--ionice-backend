package com.marcos.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.marcos.cursomc.domain.Cliente;
import com.marcos.cursomc.domain.enums.TipoCliente;
import com.marcos.cursomc.dto.ClienteDTO;
import com.marcos.cursomc.dto.ClienteNewDTO;
import com.marcos.cursomc.repositories.ClienteRepository;
import com.marcos.cursomc.resources.exception.handler.FieldMessage;
import com.marcos.cursomc.services.validation.utils.BR;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository clienteRepo;
	
	@Override
	public void initialize(ClienteUpdate constraintAnnotation) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<FieldMessage>();

		Cliente cliente = clienteRepo.findByEmail(objDto.getEmail());
		
		if(cliente != null && !cliente.getId().equals(uriId)) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for (FieldMessage e : list) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();

		}

		return list.isEmpty();
	}

}
