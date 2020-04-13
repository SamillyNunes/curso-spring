package com.nunes.sam.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nunes.sam.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Integer>{  //capaz de acessar os dados com base no objeto q passar
	
	@Transactional(readOnly=true) //ela naao necessita ser envolvida como uma transacao, isso facilita e melhora o desenvolvimento
	Cliente findByEmail(String email); //o spring ja identifica que a busca eh por email pelo padrao de nomes
	
}
