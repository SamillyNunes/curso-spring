package com.nunes.sam.resources;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nunes.sam.domain.Cliente;
import com.nunes.sam.dto.ClienteDTO;
import com.nunes.sam.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	@RequestMapping(value="/{id}", method= RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id){
		
		Cliente obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
		
	}
	

	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDTO, @PathVariable Integer id){
		Cliente obj = service.fromDTO(objDTO);
		
		obj.setId(id); //pra garantir so 
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
		
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		
		return ResponseEntity.noContent().build();
		
		
	}
	
	@RequestMapping(method=RequestMethod.GET) 
	public ResponseEntity<List<ClienteDTO>> findAll() { //pegando as categorias dtos que sera apenas o necessario
		
		List<Cliente> list = service.findAll();
		
		//veja que o stream eh para percorrer a lista, o map eh para dizer uma funcao q vai manipular cada elemento da lista
		//nesse caso, para elemento na lista ele sera passado para a categoriadto 
		//e no fim eh convertida novamente para uma lista
		List<ClienteDTO> listDTO = list.stream().map(obj->new ClienteDTO(obj)).collect(Collectors.toList());
		
		//o ok eh p/ operacao feita com sucesso e o corpo vai ser o obj
		return ResponseEntity.ok().body(listDTO); 
		
	}
	
	//o requestparam eh para tornar os parametros opcionais, alem de nao ter que fazer /pages/1/20/..., nesse caso
	//vai ficar /pages?pages=0&linesPerPage=20&...
	@RequestMapping(value="/page",method=RequestMethod.GET) 
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, //24 pq eh multiplo de 1, 2 3 e 4
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String directionOrder) { //asc eh ascendente 
		
		Page<Cliente> list = service.findPage(page,linesPerPage,orderBy,directionOrder);
		
		Page<ClienteDTO> listDTO = list.map(obj->new ClienteDTO(obj));
		
		return ResponseEntity.ok().body(listDTO); 
		
	}


}
