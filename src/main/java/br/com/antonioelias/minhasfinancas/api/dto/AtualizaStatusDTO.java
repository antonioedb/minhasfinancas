package br.com.antonioelias.minhasfinancas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizaStatusDTO {
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AtualizaStatusDTO(String status) {
		super();
		this.status = status;
	}


	public AtualizaStatusDTO() {
		super();
	}


	
	

}
