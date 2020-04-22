package com.nunes.sam.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nunes.sam.domain.Pedido;
import com.nunes.sam.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	@RequestMapping(value="/{id}", method= RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id){
		
		Pedido obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj){ //quando inserir algo com sucesso vai retornar com o corpo vazio (por isso o void). o RequestBody faz com q o json seja convertido em objeto java automaticamente
		
		obj = service.insert(obj);
		
		//abaixo uma boa pratica para retornar a url do novo objeto quandro cria-lo
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}").buildAndExpand(obj.getId()).toUri(); //o from currente request vai pegar a requisicao atual, e logo depois a gente add o /id
		
		return ResponseEntity.created(uri).build();
	}

}
