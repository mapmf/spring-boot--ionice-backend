package com.marcos.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.marcos.cursomc.domain.Cidade;
import com.marcos.cursomc.domain.Cliente;
import com.marcos.cursomc.domain.Endereco;
import com.marcos.cursomc.domain.enums.Perfil;
import com.marcos.cursomc.domain.enums.TipoCliente;
import com.marcos.cursomc.dto.ClienteDTO;
import com.marcos.cursomc.dto.ClienteNewDTO;
import com.marcos.cursomc.repositories.ClienteRepository;
import com.marcos.cursomc.repositories.EnderecoRepository;
import com.marcos.cursomc.security.UserSS;
import com.marcos.cursomc.services.exceptions.AuthorizationException;
import com.marcos.cursomc.services.exceptions.DataIntegrityException;
import com.marcos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	public Cliente find(Integer id) {

		UserSS user = UserService.authenticated();
		
		if(user == null || (!user.hasRole(Perfil.ADMIN) && !user.getId().equals(id))) {
			throw new AuthorizationException("Acesso negado");
		}
		
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
		
		return new Cliente(dto.getId(), dto.getNome(), dto.getEmail(), null, null, null);
		
	}
	
	public Cliente fromDTO(ClienteNewDTO dto) {
		
		Cliente cli = new Cliente(null, dto.getNome(), dto.getEmail(), dto.getCpfOuCnpj(), TipoCliente.toEnum(dto.getTipo()), pe.encode(dto.getSenha()));

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
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		
		UserSS user = UserService.authenticated();
		
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFormat(multipartFile);
		
		String filename = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), filename, "image");
	}


}
