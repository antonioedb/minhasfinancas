package br.com.antonioelias.minhasfinancas.api.dto;

import lombok.Builder;


@Builder
public class UsuarioDTO {
	
	private String email;
	private String nome;
	private String senha;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
}
