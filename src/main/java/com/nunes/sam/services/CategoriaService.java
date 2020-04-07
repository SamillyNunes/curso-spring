package com.nunes.sam.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nunes.sam.domain.Categoria;
import com.nunes.sam.dto.CategoriaDTO;
import com.nunes.sam.repositories.CategoriaRepository;
import com.nunes.sam.services.exceptions.DataIntegrityException;
import com.nunes.sam.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired 
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		//Busca no repositorio pelo id. O optional eh  para encapsular a questao de ser um obj instanciado ou nao. Feito para eliminar o problema do nulo.
		Optional<Categoria> obj = repo.findById(id); 
		return obj.orElseThrow(()-> new ObjectNotFoundException( //esta levantando uma excecao personalizada caso n encontre
				"Objeto não encontrado! Id:"+id+", Tipo: "+Categoria.class.getName())); //Nesse caso, para retornar nulo se nao tiver encontrado, coloca o orelse(null) 
				
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null); //so pra garantir que tenha oo id nulo
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		find(obj.getId()); //ja chama antes pq se o id for nulo vai lancar uma excecao
		return repo.save(obj); //eh a mesma coisa para o inserir, a diferença eh q quando o id eh nulo ele insere e quando nao ele atualiza
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível deletar excluir uma categoria que possui produtos.");
		}
		
	}
	
	public List<Categoria> findAll(){
		return repo.findAll();
	}
	
	//o page ja eh uma classe do java que tem informacoes legais da paginacao
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String directionOrder){
		
		//objeto q vai preparar as informacoes para q a consulta seja feita
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(directionOrder), orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDTO) {
		return new Categoria(objDTO.getId(),objDTO.getNome());
	}

}
