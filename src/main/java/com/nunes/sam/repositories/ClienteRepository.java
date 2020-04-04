package com.nunes.sam.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nunes.sam.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Integer>{  //capaz de acessar os dados com base no objeto q passar

}
