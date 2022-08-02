package br.com.antonioelias.minhasfinancas.service;

import java.rmi.server.UID;
import java.util.Optional;
import java.util.UUID;

import br.com.antonioelias.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail (String email);

	Optional<Usuario> obterPorId(UUID idUsuario);
	
}
