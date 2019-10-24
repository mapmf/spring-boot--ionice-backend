package com.marcos.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcos.cursomc.domain.Cliente;
import com.marcos.cursomc.repositories.ClienteRepository;
import com.marcos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	public Cliente find(Integer id) {

		return repo.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException
						("Cliente com id " + id + " não foi encontrada"
								+ ", Tipo: " + Cliente.class.getName()));
	}

}
