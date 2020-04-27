package com.nunes.sam.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nunes.sam.domain.Cliente;
import com.nunes.sam.domain.ItemPedido;
import com.nunes.sam.domain.PagamentoComBoleto;
import com.nunes.sam.domain.Pedido;
import com.nunes.sam.domain.enums.EstadoPagamento;
import com.nunes.sam.repositories.ItemPedidoRepository;
import com.nunes.sam.repositories.PagamentoRepository;
import com.nunes.sam.repositories.PedidoRepository;
import com.nunes.sam.security.UserSpringSecurity;
import com.nunes.sam.services.exceptions.AuthorizationException;
import com.nunes.sam.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired 
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		//Busca no repositorio pelo id. O optional eh  para encapsular a questao de ser um obj instanciado ou nao. Feito para eliminar o problema do nulo.
		Optional<Pedido> obj = repo.findById(id); 
		return obj.orElseThrow(()-> new ObjectNotFoundException( //esta levantando uma excecao personalizada caso n encontre
				"Objeto não encontrado! Id:"+id+", Tipo: "+Pedido.class.getName())); //Nesse caso, para retornar nulo se nao tiver encontrado, coloca o orelse(null) 
				
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date()); //colocar o instante atual
		
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE); //ta acabando de inserir, entao ta pendente
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante()); //um metodo pra preencher no pagamento a data de vencimento do boleto
		}
		
		obj = repo.save(obj);
		
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0); //como nesse caso nao esta sendo considerado o desconto
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco()); //procurando o id do produto e pegando o preco
			ip.setPedido(obj);
			
			
		}
		
		itemPedidoRepository.saveAll(obj.getItens());

		emailService.sendOrderConfirmationHtmlEmail(obj); //aqui esta usando a interface, mas nas configuracoes do perfil respectivo dirá quem sera instanciado para essa interface
		
		return obj;	
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String directionOrder){
		
		UserSpringSecurity user = UserService.authenticated();
		
		if(user==null) { //se nao tiver ninguem logado, ele nega o acesso pq o cliente so pode pegar os dele
			throw new AuthorizationException("Acesso negado"); 
		} 
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(directionOrder), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		
		return repo.findByCliente(cliente, pageRequest);
	}

}
