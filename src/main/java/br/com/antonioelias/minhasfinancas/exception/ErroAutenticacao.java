package br.com.antonioelias.minhasfinancas.exception;

public class ErroAutenticacao extends RuntimeException{
	
	public ErroAutenticacao(String mensagem) {
		super(mensagem);
	}

}
