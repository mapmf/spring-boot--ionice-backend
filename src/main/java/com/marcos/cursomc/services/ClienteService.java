package com.marcos.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.marcos.cursomc.domain.Cliente;
import com.marcos.cursomc.dto.ClienteDTO;
import com.marcos.cursomc.repositories.ClienteRepository;
import com.marcos.cursomc.services.exceptions.DataIntegrityException;
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
	
	public Cliente update(Cliente obj) {

		Cliente newObj = find(obj.getId());
		
		updateData(newObj, obj);
		
		return repo.save(newObj);
	}

	public void delete(Integer id) {

		find(id);

		try {
			
			repo.deleteById(id);
			
		} catch(DataIntegrityViolationException e) {
			
			throw new DataIntegrityException("Não é possível excluir uma cliente porque há entidades relacionadas");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return repo.findAll(pageRequest);
		
	}
	
	public Cliente fromDTO(ClienteDTO dto) {
		
		return new Cliente(dto.getId(), dto.getNome(), dto.getEmail(), null, null);
		
	}

	private void updateData(Cliente newObj, Cliente obj) {
		
		newObj.setEmail(obj.getEmail());
		newObj.setNome(obj.getNome());
		
	}


}
