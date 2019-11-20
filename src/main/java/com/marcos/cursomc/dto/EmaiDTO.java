package com.marcos.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class EmaiDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Email é requerido")
	@Email(message = "Email inválido")
	private String email;
	
	public EmaiDTO() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
