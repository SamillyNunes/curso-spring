package com.nunes.sam.domain.enums;

public enum Perfil {
	
	ADMIN(1, "ROLE_ADMIN"), //o nome ROLE_ eh exigencia do spring security
	CLIENTE(2, "ROLE_CLIENTE");
	
	private int codigo;
	private String descricao;
	
	private Perfil(int cod, String desc) {
		this.codigo=cod;
		this.descricao=desc;
		
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static Perfil toEnum(Integer cod) { //funcao para ser usada mesmo sem obj instanciado para retornar o enum correspondente
		if(cod==null) {
			return null;
		} 
		
		for(Perfil x : Perfil.values()) { //for para todos os valores possiveis do tipo enumerado
			if(cod.equals(x.getCodigo())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido"+cod);
	}

	
	

}
