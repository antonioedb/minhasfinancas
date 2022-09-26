package br.com.antonioelias.minhasfinancas.model.repostitory;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.antonioelias.minhasfinancas.model.entity.Lancamento;
import br.com.antonioelias.minhasfinancas.model.enums.StatusLancamento;
import br.com.antonioelias.minhasfinancas.model.enums.TipoLancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, UUID> {
	
	@Query(value="select sum(l.valor) from Lancamento l join l.usuario u "
			   + "where u.id = :idUsuario and l.tipo=:tipo and l.status=:status group by u")
	BigDecimal oberSaldoPorTipoLancamentoEUsuarioEStatus(
			@Param("idUsuario") UUID idUsuario,
			@Param("tipo") TipoLancamento tipo,
			@Param("status") StatusLancamento status );

}
