package com.marcos.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.marcos.cursomc.domain.Categoria;
import com.marcos.cursomc.domain.Produto;
import com.marcos.cursomc.repositories.CategoriaRepository;
import com.marcos.cursomc.repositories.ProdutoRepository;
import com.marcos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto find(Integer id) {

		return repo.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException
						("Produto com id " + id + " não foi encontrado"
								+ ", Tipo: " + Produto.class.getName()));
	}
	
	public Page<Produto> search(String nome, List<Integer> categoriaIds, int page, int size, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, size, Direction.valueOf(direction), orderBy);
		
		List<Categoria> categorias = categoriaRepository.findAllById(categoriaIds);
		
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}

}
