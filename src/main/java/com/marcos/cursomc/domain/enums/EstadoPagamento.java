package com.marcos.cursomc.domain.enums;

public enum EstadoPagamento {
	
	PENDENTE(1, "Pendente"), 
	QUITADO(2, "Quitado"),
	CANCELADO(3, "Cancelado");
	
	private int id;
	private String decricao;
	
	private EstadoPagamento(int id, String decricao) {
		this.setId(id);
		this.decricao = decricao;
	}

	public String getDecricao() {
		return decricao;
	}

	public void setDecricao(String decricao) {
		this.decricao = decricao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public static EstadoPagamento toEnum(Integer id) {
		
		if(id == null) {
			return null;
		}
		
		for(EstadoPagamento estadoPagamento : EstadoPagamento.values()) {
			
			if(id.equals(estadoPagamento.getId())) {
				
				return estadoPagamento;
			}
			
		}
		
		throw new IllegalArgumentException("Id inválido: " + id);
	}
}
