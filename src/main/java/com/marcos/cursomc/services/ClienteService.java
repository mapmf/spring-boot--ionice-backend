package com.marcos.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcos.cursomc.domain.Cidade;
import com.marcos.cursomc.domain.Cliente;
import com.marcos.cursomc.domain.Endereco;
import com.marcos.cursomc.domain.enums.TipoCliente;
import com.marcos.cursomc.dto.ClienteDTO;
import com.marcos.cursomc.dto.ClienteNewDTO;
import com.marcos.cursomc.repositories.ClienteRepository;
import com.marcos.cursomc.repositories.EnderecoRepository;
import com.marcos.cursomc.services.exceptions.DataIntegrityException;
import com.marcos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	public Cliente find(Integer id) {

		return repo.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException
						("Cliente com id " + id + " não foi encontrada"
								+ ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		
		obj.setId(null);
		
		obj = repo.save(obj);
		
		enderecoRepository.saveAll(obj.getEnderecos());
		
		return obj;
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
			
			throw new DataIntegrityException("Não é possível excluir uma cliente porque há pedidos relacionados");
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
	
	public Cliente fromDTO(ClienteNewDTO dto) {
		
		Cliente cli = new Cliente(null, dto.getNome(), dto.getEmail(), dto.getCpfOuCnpj(), TipoCliente.toEnum(dto.getTipo()));

		Cidade cid = new Cidade(dto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, dto.getLogradouro(), dto.getNumero(), dto.getComplemento(), dto.getBairro(), dto.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		
		cli.getTelefones().add(dto.getTelefone1());
		
		if(dto.getTelefone2() != null) {
			cli.getTelefones().add(dto.getTelefone2());			
		}
		
		if(dto.getTelefone3() != null) {
			cli.getTelefones().add(dto.getTelefone3());			
		}
		
		return cli;
	}

	private void updateData(Cliente newObj, Cliente obj) {
		
		newObj.setEmail(obj.getEmail());
		newObj.setNome(obj.getNome());
		
	}


}
