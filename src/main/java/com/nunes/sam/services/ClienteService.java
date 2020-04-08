package com.nunes.sam.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nunes.sam.domain.Cliente;
import com.nunes.sam.dto.ClienteDTO;
import com.nunes.sam.repositories.ClienteRepository;
import com.nunes.sam.services.exceptions.DataIntegrityException;
import com.nunes.sam.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		//Busca no repositorio pelo id. O optional eh  para encapsular a questao de ser um obj instanciado ou nao. Feito para eliminar o problema do nulo.
		Optional<Cliente> obj = repo.findById(id); 
		return obj.orElseThrow(()-> new ObjectNotFoundException( //esta levantando uma excecao personalizada caso n encontre
				"Objeto não encontrado! Id:"+id+", Tipo: "+Cliente.class.getName())); //Nesse caso, para retornar nulo se nao tiver encontrado, coloca o orelse(null) 
				
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId()); //ja chama antes pq se o id for nulo vai lancar uma excecao
		updateData(newObj,obj); //usado para atualizar apenas os dados que vierem na requisicao, e nao todos (caso alguem venha nulo)
		return repo.save(newObj); //eh a mesma coisa para o inserir, a diferença eh q quando o id eh nulo ele insere e quando nao ele atualiza
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível deletar excluir porquê há entidades relacionadas");
		}
		
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	//o page ja eh uma classe do java que tem informacoes legais da paginacao
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String directionOrder){
		
		//objeto q vai preparar as informacoes para q a consulta seja feita
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(directionOrder), orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
//		throw new UnsupportedOperationException(); //excecao de metodo n implementado
		return new Cliente(objDTO.getId(),objDTO.getNome(),objDTO.getEmail(),null,null);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}

}
