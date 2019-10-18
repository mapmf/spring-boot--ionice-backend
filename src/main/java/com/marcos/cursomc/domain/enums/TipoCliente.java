package com.marcos.cursomc.domain.enums;

public enum TipoCliente {
	
	PESSOAFISICA(1, "Pessoa Fisica"),
	PESSOAJURIDICA(2, "Pessoa Jurídica");
	
	private int id;
	private String descricao;
	
	private TipoCliente(int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static TipoCliente toEnum(Integer id) {
		
		if(id == null) {
			return null;
		}
		
		for (TipoCliente tipoCliente : TipoCliente.values()) {
			
			if(id.equals(tipoCliente.getId())) {
				return tipoCliente;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + id);
	}
	
	
}
