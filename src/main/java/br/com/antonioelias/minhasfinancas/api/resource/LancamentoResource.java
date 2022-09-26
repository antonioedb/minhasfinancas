package br.com.antonioelias.minhasfinancas.api.resource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.antonioelias.minhasfinancas.api.dto.AtualizaStatusDTO;
import br.com.antonioelias.minhasfinancas.api.dto.LancamentoDTO;
import br.com.antonioelias.minhasfinancas.exception.RegraNegocioExcepiton;
import br.com.antonioelias.minhasfinancas.model.entity.Lancamento;
import br.com.antonioelias.minhasfinancas.model.entity.Usuario;
import br.com.antonioelias.minhasfinancas.model.enums.StatusLancamento;
import br.com.antonioelias.minhasfinancas.model.enums.TipoLancamento;
import br.com.antonioelias.minhasfinancas.service.LancamentoService;
import br.com.antonioelias.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {
	
	private LancamentoService service;
	private UsuarioService usuarioService;
	
	public LancamentoResource(LancamentoService service, UsuarioService usuarioService ) {
		this.service = service;
		this.usuarioService= usuarioService;
	}
	
	@GetMapping("{id}")
	public ResponseEntity obterLancamento( @PathVariable("id") UUID id) {
		return service.obetrPorId(id)
				.map( lancamento -> new ResponseEntity(converter( lancamento), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
		
	}
	
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
			 
		try {			
			Lancamento entidade = converter(dto);
			service.salvar(entidade);
			return new ResponseEntity(entidade,HttpStatus.CREATED);
			
		} catch (RegraNegocioExcepiton e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") UUID id, @RequestBody LancamentoDTO dto) {
		return service.obetrPorId(id).map(entity -> {
			try {
				
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
				
			} catch (RegraNegocioExcepiton e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}	
		}).orElseGet(()-> 
		new ResponseEntity("Lançamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
	
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") UUID id, @RequestBody AtualizaStatusDTO dto) {
		return service.obetrPorId(id).map( entity ->{
			StatusLancamento statusSececioado = StatusLancamento.valueOf(dto.getStatus());
			if(statusSececioado==null) {
				return ResponseEntity.badRequest().body("Não foi possivel atualizar o status do lançamento.");
			}
			try {
				entity.setStatus(statusSececioado);
				service.atualizar(entity);
				return ResponseEntity.ok(entity);
				
			} catch (RegraNegocioExcepiton e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}	
		}).orElseGet(()-> 
		new ResponseEntity("Lançamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
		
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") UUID id) {
		return service.obetrPorId(id).map( entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() ->
		new ResponseEntity("Lançamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
		
	}
	
	@GetMapping
	public ResponseEntity buscar(
		@RequestParam(value="descricao", required = false) String descicao,
		@RequestParam(value="mes", required = false) Integer mes,
		@RequestParam(value="ano", required = false) Integer ano,
		@RequestParam("usuario") UUID idUsuario 
		) {
		Lancamento lancamentoFiltro = new Lancamento(idUsuario, descicao, mes, ano);
		
		lancamentoFiltro.setDescricao(descicao);
		lancamentoFiltro.setAno(ano);
		lancamentoFiltro.setMes(mes);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. Usuario não encontrtado.");

		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}
	
	private LancamentoDTO converter(Lancamento lancamento) {
		LancamentoDTO lancamentoDTO = new LancamentoDTO();
				lancamentoDTO.setId(lancamento.getId());
				lancamentoDTO.setDescricao(lancamento.getDescricao());
				lancamentoDTO.setValor(lancamento.getValor());
				lancamentoDTO.setMes(lancamento.getMes());
				lancamentoDTO.setAno(lancamento.getAno());
				lancamentoDTO.setStatus(lancamento.getStatus().name());
				lancamentoDTO.setTipo(lancamento.getTipo().name());
				lancamentoDTO.setUsuario(lancamento.getUsuario().getId());
				
		return lancamentoDTO;				
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		Usuario usuario = usuarioService
				.obterPorId(dto.getUsuario())
				.orElseThrow( () -> new RegraNegocioExcepiton("Usuário não encontrado para o Id informado") );
		
        lancamento.setUsuario(usuario);
        if(dto.getTipo()!=null) {
        	lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }
        if(dto.getStatus()!=null) {
        	lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }      
        return lancamento;
		
	}
	

}
