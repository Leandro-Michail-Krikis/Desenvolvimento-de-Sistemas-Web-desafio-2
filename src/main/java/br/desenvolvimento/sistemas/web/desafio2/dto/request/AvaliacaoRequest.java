package br.desenvolvimento.sistemas.web.desafio2.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoRequest {

    @NotNull(message = "Valor é obrigatório")
    @Min(value = 1, message = "Valor deve ser mínimo 1")
    @Max(value = 10, message = "Valor deve ser máximo 10")
    private Integer valor;
}

