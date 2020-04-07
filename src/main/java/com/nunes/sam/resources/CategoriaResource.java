package com.nunes.sam.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nunes.sam.domain.Categoria;
import com.nunes.sam.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias") //endpoint que vai ser a resposta p essa classe
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET) //o value significa que o endpoint	agora tem que receber o id para o retorna-lo
	public ResponseEntity<Categoria> find(@PathVariable Integer id) { //o path variable eh para que o spring saiba que o id q vem no endpoint eh o que eh passado nesse metodo
		
		Categoria obj = service.find(id);
		
		//o ok eh p/ operacao feita com sucesso e o corpo vai ser o obj
		return ResponseEntity.ok().body(obj); 
		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody Categoria obj){ //quando inserir algo com sucesso vai retornar com o corpo vazio (por isso o void). o RequestBody faz com q o json seja convertido em objeto java automaticamente
		obj = service.insert(obj);
		
		//abaixo uma boa pratica para retornar a url do novo objeto quandro cria-lo
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}").buildAndExpand(obj.getId()).toUri(); //o from currente request vai pegar a requisicao atual, e logo depois a gente add o /id
		
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id){
		obj.setId(id); //pra garantir so 
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
		
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		
		return ResponseEntity.noContent().build();
		
		
	}

}
