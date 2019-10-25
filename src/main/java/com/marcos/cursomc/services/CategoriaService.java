package com.marcos.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.marcos.cursomc.domain.Categoria;
import com.marcos.cursomc.repositories.CategoriaRepository;
import com.marcos.cursomc.services.exceptions.DataIntegrityException;
import com.marcos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {

		return repo.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException
						("Categoria com id " + id + " não foi encontrada"
								+ ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria obj) {
		
		obj.setId(null);
		
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) {

		find(obj.getId());
		
		return repo.save(obj);
	}

	public void delete(Integer id) {

		find(id);

		try {
			
			repo.deleteById(id);
			
		} catch(DataIntegrityViolationException e) {
			
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}

	public List<Categoria> findAll() {
		return repo.findAll();
	}

}
