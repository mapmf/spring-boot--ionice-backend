package com.marcos.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.cursomc.domain.Produto;
import com.marcos.cursomc.dto.ProdutoDTO;
import com.marcos.cursomc.resources.utils.URL;
import com.marcos.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> find(@PathVariable("id") Integer id) {
		
		Produto produto = service.find(id);
		
		return ResponseEntity.ok(produto);
	}
	
	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(name = "nome", defaultValue = "") String nome,
			@RequestParam(name = "categorias", defaultValue = "") String categorias,
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "linesPerPage", defaultValue = "24")  Integer linesPerPage, 
			@RequestParam(name = "ordeBy", defaultValue = "nome")  String orderBy, 
			@RequestParam(name = "direction", defaultValue = "ASC")  String direction) {
		
		List<Integer> categoriaIds = URL.decodeIntList(categorias);
		
		String decoded = URL.decodeParam(nome);
		
		Page<Produto> produtoList = service.search(decoded, categoriaIds, page, linesPerPage, orderBy, direction);
		
		Page<ProdutoDTO> produtoDTOList = produtoList.map(p -> new ProdutoDTO(p));
		
		return ResponseEntity.ok(produtoDTOList); 
	}
}
