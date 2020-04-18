package com.nunes.sam.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nunes.sam.domain.ItemPedido;
import com.nunes.sam.domain.PagamentoComBoleto;
import com.nunes.sam.domain.Pedido;
import com.nunes.sam.domain.enums.EstadoPagamento;
import com.nunes.sam.repositories.ItemPedidoRepository;
import com.nunes.sam.repositories.PagamentoRepository;
import com.nunes.sam.repositories.PedidoRepository;
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
	private ItemPedidoRepository itemPedidoRepository;
	
	public Pedido find(Integer id) {
		//Busca no repositorio pelo id. O optional eh  para encapsular a questao de ser um obj instanciado ou nao. Feito para eliminar o problema do nulo.
		Optional<Pedido> obj = repo.findById(id); 
		return obj.orElseThrow(()-> new ObjectNotFoundException( //esta levantando uma excecao personalizada caso n encontre
				"Objeto não encontrado! Id:"+id+", Tipo: "+Pedido.class.getName())); //Nesse caso, para retornar nulo se nao tiver encontrado, coloca o orelse(null) 
				
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date()); //colocar o instante atual
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
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco()); //procurando o id do produto e pegando o preco
			ip.setPedido(obj);
			
			
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		return obj;	
	}

}
