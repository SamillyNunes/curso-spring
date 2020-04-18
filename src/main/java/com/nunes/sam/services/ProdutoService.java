package com.nunes.sam.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nunes.sam.domain.Categoria;
import com.nunes.sam.domain.Produto;
import com.nunes.sam.repositories.CategoriaRepository;
import com.nunes.sam.repositories.ProdutoRepository;
import com.nunes.sam.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired 
	private ProdutoRepository repo;

	@Autowired 
	private CategoriaRepository categoriaRepository;
	
	public Produto find(Integer id) {
		//Busca no repositorio pelo id. O optional eh  para encapsular a questao de ser um obj instanciado ou nao. Feito para eliminar o problema do nulo.
		Optional<Produto> obj = repo.findById(id); 
		return obj.orElseThrow(()-> new ObjectNotFoundException( //esta levantando uma excecao personalizada caso n encontre
				"Objeto não encontrado! Id:"+id+", Tipo: "+Produto.class.getName())); //Nesse caso, para retornar nulo se nao tiver encontrado, coloca o orelse(null) 
				
	}
	
	//o search vai receber um pedaco de palavra q sera uma parte do nome do produto e uma lista das categorias selecionadas
	//busca paginada
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String directionOrder){
		
		//objeto q vai preparar as informacoes para q a consulta seja feita
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(directionOrder), orderBy);
		
		List<Categoria> categorias = categoriaRepository.findAllById(ids); 
		
		return repo.search(nome, categorias, pageRequest);
		
	}

}
