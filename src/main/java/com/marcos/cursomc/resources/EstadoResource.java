package com.marcos.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.cursomc.domain.Cidade;
import com.marcos.cursomc.domain.Estado;
import com.marcos.cursomc.dto.CidadeDTO;
import com.marcos.cursomc.dto.EstadoDTO;
import com.marcos.cursomc.services.CidadeService;
import com.marcos.cursomc.services.EstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoResource {

	@Autowired
	EstadoService service;
	
	@Autowired
	CidadeService cidadeService;

	@GetMapping
	public ResponseEntity<List<EstadoDTO>> findAll() {

		List<Estado> estados = service.findAll();

		List<EstadoDTO> dtos = estados.stream().map(e -> new EstadoDTO(e)).collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("{estadoId}/cidades")
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {

		List<Cidade> cidades = cidadeService.findByEstado(estadoId);

		List<CidadeDTO> dtos = cidades.stream().map(e -> new CidadeDTO(e)).collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}
	
	
	
}
