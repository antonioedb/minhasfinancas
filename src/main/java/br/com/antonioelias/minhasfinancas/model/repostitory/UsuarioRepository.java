package br.com.antonioelias.minhasfinancas.model.repostitory;


import java.rmi.server.UID;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.antonioelias.minhasfinancas.model.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{
	
	Optional<Usuario> findByEmail(String email);
	
	boolean existsByEmail(String email);

	Optional<Usuario> findById(UID id);

}
