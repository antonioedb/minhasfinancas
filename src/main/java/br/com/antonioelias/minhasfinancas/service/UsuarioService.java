package br.com.antonioelias.minhasfinancas.service;

import br.com.antonioelias.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail (String email);
	
}
