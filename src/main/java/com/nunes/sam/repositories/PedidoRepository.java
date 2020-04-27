package com.nunes.sam.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nunes.sam.domain.Cliente;
import com.nunes.sam.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido,Integer>{  //capaz de acessar os dados com base no objeto q passar
	
	@Transactional(readOnly=true)
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);
}
