package br.com.antonioelias.minhasfinancas.model.repostitory;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.antonioelias.minhasfinancas.model.entity.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, UUID> {

}
