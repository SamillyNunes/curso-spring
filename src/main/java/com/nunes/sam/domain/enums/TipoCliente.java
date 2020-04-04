package com.nunes.sam.domain.enums;

public enum TipoCliente {
	PESSOA_FISICA(1,"Pessoa Física"), //isso eh feito para evitar futuros erros no futuro em questao de codigo (caso seja adicionado mais e quebre os dados q tavam com os codigos antigos)
	PESSOA_JURIDICA(2, "Pessoa Jurídica");
	
	private int codigo;
	private String descricao;
	
	private TipoCliente(int cod, String desc) {
		this.codigo=cod;
		this.descricao=desc;
	}
	
	public int getCodigo() {
		return this.codigo;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public static TipoCliente toEnum(Integer cod) { //funcao para ser usada mesmo sem obj instanciado para retornar o enum correspondente
		if(cod==null) {
			return null;
		} 
		
		for(TipoCliente x : TipoCliente.values()) { //for para todos os valores possiveis do tipo enumerado
			if(cod.equals(x.getCodigo())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido"+cod);
	}
	
	

}
