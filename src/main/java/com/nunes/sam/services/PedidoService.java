package com.nunes.sam.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nunes.sam.domain.Pedido;
import com.nunes.sam.repositories.PedidoRepository;
import com.nunes.sam.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired 
	private PedidoRepository repo;
	
	public Pedido buscar(Integer id) {
		//Busca no repositorio pelo id. O optional eh  para encapsular a questao de ser um obj instanciado ou nao. Feito para eliminar o problema do nulo.
		Optional<Pedido> obj = repo.findById(id); 
		return obj.orElseThrow(()-> new ObjectNotFoundException( //esta levantando uma excecao personalizada caso n encontre
				"Objeto não encontrado! Id:"+id+", Tipo: "+Pedido.class.getName())); //Nesse caso, para retornar nulo se nao tiver encontrado, coloca o orelse(null) 
				
	}

}
