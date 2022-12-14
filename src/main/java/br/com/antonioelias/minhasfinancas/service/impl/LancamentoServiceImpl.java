package br.com.antonioelias.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.antonioelias.minhasfinancas.exception.RegraNegocioExcepiton;
import br.com.antonioelias.minhasfinancas.model.entity.Lancamento;
import br.com.antonioelias.minhasfinancas.model.enums.StatusLancamento;
import br.com.antonioelias.minhasfinancas.model.enums.TipoLancamento;
import br.com.antonioelias.minhasfinancas.model.repostitory.LancamentoRepository;
import br.com.antonioelias.minhasfinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService{
	
	private LancamentoRepository repository;

	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		lancamento.setDataCacastro(LocalDateTime.now(ZoneId.of("UTC")));
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		repository.delete(lancamento);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamento) {
		
		Example exemple = Example.of(lancamento, ExampleMatcher.matching()
															   .withIgnoreCase()
															   .withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(exemple);	
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

	@Override
	public void validar(Lancamento lancamento) {
		if (lancamento.getDescricao()==null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioExcepiton("Informe uma Descri????o v??lida");
			
		}
		if(lancamento.getMes() == null || lancamento.getMes() <1 || lancamento.getMes() >12) {
			throw new RegraNegocioExcepiton("Informe um M??s v??lido");
		}
		
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4  ) {
			throw new RegraNegocioExcepiton("Informe um Ano v??lido");
		}
		
		if( lancamento.getUsuario().getId() == null || lancamento.getUsuario() == null) {
			throw new RegraNegocioExcepiton("Informe um Usu??rio");
		}
		
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
			throw new RegraNegocioExcepiton("Informe um Valor v??lido");
		}
		if(lancamento.getTipo() == null) {
			throw new RegraNegocioExcepiton("Informe um Tipo de lan??amento");
		}
		
		
	}


	@Override
	public Optional<Lancamento> obetrPorId(UUID id) {
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	
	public BigDecimal obterSaldoPorUsuario(UUID id) {
		BigDecimal receitas = repository.oberSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO);
		BigDecimal despesas = repository.oberSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.DESPESA,StatusLancamento.EFETIVADO);
		if (receitas == null) {
			receitas = BigDecimal.ZERO;
		}
		if (despesas == null) {
			despesas = BigDecimal.ZERO;
		}
		
		return receitas.subtract(despesas);
	}

}
