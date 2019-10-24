package com.marcos.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcos.cursomc.domain.Pedido;
import com.marcos.cursomc.repositories.PedidoRepository;
import com.marcos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	public Pedido find(Integer id) {

		return repo.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException
						("Pedido com id " + id + " não foi encontrado"
								+ ", Tipo: " + Pedido.class.getName()));
	}

}
