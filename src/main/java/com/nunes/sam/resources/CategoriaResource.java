package com.nunes.sam.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nunes.sam.domain.Categoria;
import com.nunes.sam.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias") //endpoint que vai ser a resposta p essa classe
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET) //o value significa que o endpoint	agora tem que receber o id para o retorna-lo
	public ResponseEntity<?> find(@PathVariable Integer id) { //o path variable eh para que o spring saiba que o id q vem no endpoint eh o que eh passado nesse metodo
		
		Categoria obj = service.buscar(id);
		
		//o ok eh p/ operacao feita com sucesso e o corpo vai ser o obj
		return ResponseEntity.ok().body(obj); 
		
	}

}
