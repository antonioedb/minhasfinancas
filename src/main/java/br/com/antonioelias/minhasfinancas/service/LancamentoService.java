package br.com.antonioelias.minhasfinancas.service;

import java.util.List;

import br.com.antonioelias.minhasfinancas.model.entity.Lancamento;
import br.com.antonioelias.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {
	Lancamento Salvar(Lancamento lancamento);
	Lancamento atualizar(Lancamento lancamento);
	void deletar(Lancamento lancamento);
	List<Lancamento> buscar(Lancamento lancamento);
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	void validar(Lancamento lancamento);
}
