package com.marcos.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marcos.cursomc.domain.Cliente;
import com.marcos.cursomc.dto.ClienteDTO;
import com.marcos.cursomc.dto.ClienteNewDTO;
import com.marcos.cursomc.services.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteResource {

	@Autowired
	ClienteService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> find(@PathVariable("id") Integer id){
		
		Cliente cliente = service.find(id);
		
		return ResponseEntity.ok(cliente);
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDTO){
		
		Cliente obj = service.fromDTO(objDTO);
		
		obj = service.insert(obj);
		
		URI uri = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable("id") Integer id, @Valid @RequestBody ClienteDTO objDTO) {
		
		Cliente obj = service.fromDTO(objDTO);
		
		obj.setId(id);
		
		obj = service.update(obj);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Cliente> delete(@PathVariable("id") Integer id) {
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public ResponseEntity<List<ClienteDTO>> findAll() {
		
		List<Cliente> categoriaList = service.findAll();
		
		List<ClienteDTO> categoriaDTOList = categoriaList.stream()
															.map(c -> new ClienteDTO(c))
															.collect(Collectors.toList());
		
		return ResponseEntity.ok(categoriaDTOList);
	}
	
	@GetMapping("/page")
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "linesPerPage", defaultValue = "24")  Integer linesPerPage, 
			@RequestParam(name = "ordeBy", defaultValue = "nome")  String orderBy, 
			@RequestParam(name = "direction", defaultValue = "ASC")  String direction) {
		
		Page<Cliente> categoriaList = service.findPage(page, linesPerPage, orderBy, direction);
		
		Page<ClienteDTO> categoriaDTOList = categoriaList.map(c -> new ClienteDTO(c));
		
		return ResponseEntity.ok(categoriaDTOList); 
	}

}
