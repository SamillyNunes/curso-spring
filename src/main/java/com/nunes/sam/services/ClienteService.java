package com.nunes.sam.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nunes.sam.domain.Cidade;
import com.nunes.sam.domain.Cliente;
import com.nunes.sam.domain.Endereco;
import com.nunes.sam.domain.enums.Perfil;
import com.nunes.sam.domain.enums.TipoCliente;
import com.nunes.sam.dto.ClienteDTO;
import com.nunes.sam.dto.ClienteNewDTO;
import com.nunes.sam.repositories.ClienteRepository;
import com.nunes.sam.repositories.EnderecoRepository;
import com.nunes.sam.security.UserSpringSecurity;
import com.nunes.sam.services.exceptions.AuthorizationException;
import com.nunes.sam.services.exceptions.DataIntegrityException;
import com.nunes.sam.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository endRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public Cliente find(Integer id) {
		
		//esta fazendo a verificacao para na hora que for dado um get (acessa este metodo) verificar se esse usuario pode acessar
		//esse cliente (mesma pessoa ou o user eh admin)
		UserSpringSecurity user = UserService.authenticated();
		if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado.");
		}
		
		//Busca no repositorio pelo id. O optional eh  para encapsular a questao de ser um obj instanciado ou nao. Feito para eliminar o problema do nulo.
		Optional<Cliente> obj = repo.findById(id); 
		return obj.orElseThrow(()-> new ObjectNotFoundException( //esta levantando uma excecao personalizada caso n encontre
				"Objeto não encontrado! Id:"+id+", Tipo: "+Cliente.class.getName())); //Nesse caso, para retornar nulo se nao tiver encontrado, coloca o orelse(null) 
				
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null); //so pra garantir que tenha oo id nulo
		obj = repo.save(obj);
		endRepository.saveAll(obj.getEnderecos());
		return obj;
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
			throw new DataIntegrityException("Não é possível deletar porquê há pedidos relacionados");
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
		return new Cliente(objDTO.getId(),objDTO.getNome(),objDTO.getEmail(),null,null, null);
	}
	
	//sobrecarga do metodo feita para poder fazer a insercao do cliente apenas com endereco e cidade e telefones 
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()), bCryptPasswordEncoder.encode(objDTO.getSenha()));
		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		
		if(objDTO.getTelefone2()!=null) {
			cli.getTelefones().add(objDTO.getTelefone2());
		}
		
		if(objDTO.getTelefone3()!=null) {
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		return cli;	
	}
	
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}

}
