package com.nunes.sam.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nunes.sam.domain.Categoria;
import com.nunes.sam.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired 
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		//Busca no repositorio pelo id. O optional eh  para encapsular a questao de ser um obj instanciado ou nao. Feito para eliminar o problema do nulo.
		Optional<Categoria> obj = repo.findById(id); 
		return obj.orElse(null); //Nesse caso, para retornar nulo se nao tiver encontrado, coloca o orelse(null) 
				
	}

}
