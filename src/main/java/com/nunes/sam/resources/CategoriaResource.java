package com.nunes.sam.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nunes.sam.domain.Categoria;
import com.nunes.sam.dto.CategoriaDTO;
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
	
	@RequestMapping(method=RequestMethod.GET) 
	public ResponseEntity<List<CategoriaDTO>> findAll() { //pegando as categorias dtos que sera apenas o necessario
		
		List<Categoria> list = service.findAll();
		
		//veja que o stream eh para percorrer a lista, o map eh para dizer uma funcao q vai manipular cada elemento da lista
		//nesse caso, para elemento na lista ele sera passado para a categoriadto 
		//e no fim eh convertida novamente para uma lista
		List<CategoriaDTO> listDTO = list.stream().map(obj->new CategoriaDTO(obj)).collect(Collectors.toList());
		
		//o ok eh p/ operacao feita com sucesso e o corpo vai ser o obj
		return ResponseEntity.ok().body(listDTO); 
		
	}
	
	//o requestparam eh para tornar os parametros opcionais, alem de nao ter que fazer /pages/1/20/..., nesse caso
	//vai ficar /pages?pages=0&linesPerPage=20&...
	@RequestMapping(value="/page",method=RequestMethod.GET) 
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, //24 pq eh multiplo de 1, 2 3 e 4
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String directionOrder) { //asc eh ascendente 
		
		Page<Categoria> list = service.findPage(page,linesPerPage,orderBy,directionOrder);
		
		Page<CategoriaDTO> listDTO = list.map(obj->new CategoriaDTO(obj));
		
		return ResponseEntity.ok().body(listDTO); 
		
	}

}
