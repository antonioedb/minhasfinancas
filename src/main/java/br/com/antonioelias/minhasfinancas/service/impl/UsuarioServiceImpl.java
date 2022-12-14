package br.com.antonioelias.minhasfinancas.service.impl;

import java.rmi.server.UID;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.antonioelias.minhasfinancas.exception.ErroAutenticacao;
import br.com.antonioelias.minhasfinancas.exception.RegraNegocioExcepiton;
import br.com.antonioelias.minhasfinancas.model.entity.Usuario;
import br.com.antonioelias.minhasfinancas.model.repostitory.UsuarioRepository;
import br.com.antonioelias.minhasfinancas.service.UsuarioService;


@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	@Autowired
	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		
		Optional<Usuario> usuario =repository.findByEmail(email);
		
	    if(!usuario.isPresent()) {
	    	throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
	    	
	    }
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha não encontrado");
		}
		
	return usuario.get();
	
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
          
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		
		if (existe) {
			throw new RegraNegocioExcepiton ("Já existe um usuário cadastrado com este email.");
		}	
		
	}

	@Override
	public Optional<Usuario> obterPorId(UUID id) {
		
		return repository.findById(id);
	}
	

}
