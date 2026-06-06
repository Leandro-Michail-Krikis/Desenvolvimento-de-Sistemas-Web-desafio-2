package br.desenvolvimento.sistemas.web.desafio2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaRequest {

    @NotBlank(message = "Nome do cliente é obrigatório")
    private String nomeCliente;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotNull(message = "Data de viagem é obrigatória")
    @Future(message = "Data de viagem deve ser no futuro")
    private LocalDate dataViagem;

    @NotNull(message = "Número de pessoas é obrigatório")
    @Min(value = 1, message = "Número de pessoas deve ser mínimo 1")
    private Integer numeroPessoas;

    @NotNull(message = "Destino ID é obrigatório")
    private Long destinoId;
}

