package com.nunes.sam.repositories;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nunes.sam.domain.Categoria;
import com.nunes.sam.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto,Integer>{  //capaz de acessar os dados com base no objeto q passar
	
	//como eh uma consulta diferenciada, nao ta dentro das possiveis do JPA, tem que informar qual o JPQL dessa consulta. 
	//Para informar dentro do JPQL as variaveis passadas no metodo, a gente coloca a notacao @Param() com o nome que ta
	//na consulta dentro dos parenteses
	@Transactional(readOnly=true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> search(@Param("nome") String nome,@Param("categorias") List<Categoria> categorias,Pageable pageRequest);
	
	//poderia ser tambem
//	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(parametros);
}
