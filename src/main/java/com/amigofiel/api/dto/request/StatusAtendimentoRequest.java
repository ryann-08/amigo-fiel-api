package com.amigofiel.api.dto.request;

import com.amigofiel.api.enums.StatusAtendimento;
import jakarta.validation.constraints.NotNull;

// Objeto de Transferencia de Dados (DTO) usado exclusivamente na rota de alteracao
// parcial de status, garantindo que o novo status seja enviado.
//aqui usei record ao inves de class pq ele cria uma estrutura imutavel

public record StatusAtendimentoRequest(

        // Valida que o novo status informado nao pode ser nulo
        @NotNull(message = "O novo status e obrigatorio")
        StatusAtendimento status
) {}