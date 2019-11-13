package com.marcos.cursomc.domain.enums;

public enum Perfil {
	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	private int cod;
	private String descricao;
	
	private Perfil(int cod, String descricao) {
		
		this.setCod(cod);
		this.setDescricao(descricao);
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	public static Perfil toEnum(Integer id) {
		
		if(id == null) {
			return null;
		}
		
		for(Perfil perfil : Perfil.values()) {
			
			if(id.equals(perfil.getCod())) {
				
				return perfil;
			}
			
		}
		
		throw new IllegalArgumentException("Id inválido: " + id);
	}
	
	
}
