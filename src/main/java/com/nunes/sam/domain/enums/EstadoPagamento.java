package com.nunes.sam.domain.enums;

public enum EstadoPagamento {
	
	PENDENTE(1, "Pendente"),
	QUITADO(2, "Quitado"),
	CANCEADO(3, "Cancelado");
	
	private int codigo;
	private String descricao;
	
	private EstadoPagamento(int cod, String desc) {
		this.codigo=cod;
		this.descricao=desc;
		
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static EstadoPagamento toEnum(Integer cod) { //funcao para ser usada mesmo sem obj instanciado para retornar o enum correspondente
		if(cod==null) {
			return null;
		} 
		
		for(EstadoPagamento x : EstadoPagamento.values()) { //for para todos os valores possiveis do tipo enumerado
			if(cod.equals(x.getCodigo())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido"+cod);
	}

	
	

}
