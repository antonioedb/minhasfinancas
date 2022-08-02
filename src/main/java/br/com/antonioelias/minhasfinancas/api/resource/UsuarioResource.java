package br.com.antonioelias.minhasfinancas.api.resource;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.antonioelias.minhasfinancas.api.dto.UsuarioDTO;
import br.com.antonioelias.minhasfinancas.exception.ErroAutenticacao;
import br.com.antonioelias.minhasfinancas.exception.RegraNegocioExcepiton;
import br.com.antonioelias.minhasfinancas.model.entity.Usuario;
import br.com.antonioelias.minhasfinancas.service.LancamentoService;
import br.com.antonioelias.minhasfinancas.service.UsuarioService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	private UsuarioService service;
	private LancamentoService lancamentoService;
	
	public UsuarioResource(UsuarioService service, LancamentoService lancamentoService ) {
		this.service = service;
		this.lancamentoService = lancamentoService;
	}
	
	@GetMapping
	public String helloword() {
		return "Ola Mundo";
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		
		try {
			Usuario usuarioAutenteicado = service.autenticar(dto.getEmail(),dto.getSenha());
			return ResponseEntity.ok(usuarioAutenteicado);
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		
		Usuario usuario = new Usuario() ;
		usuario.setNome(dto.getNome());
		usuario.setEmail(dto.getEmail());
		usuario.setSenha(dto.getSenha());
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioExcepiton e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
       
    }
	
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") UUID id) {
		Optional<Usuario> usuario = service.obterPorId(id);
		
		if(!usuario.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}
	
}
