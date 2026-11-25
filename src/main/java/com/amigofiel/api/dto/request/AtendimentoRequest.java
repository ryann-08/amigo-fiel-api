package com.amigofiel.api.dto.request;

import com.amigofiel.api.enums.TipoAtendimento;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


 // Objeto de Transferência de Dados (DTO) usado para receber e validar
 // os dados enviados pelo cliente no momento em que ele cadastra um novo atendimento.

public record AtendimentoRequest(

        // Valida que o ID do tutor enviado não pode ser nulo
        @NotNull(message = "O ID do tutor é obrigatório")
        Long tutorId,

        // Valida que o ID do veterinário enviado não pode ser nulo
        @NotNull(message = "O ID do veterinário é obrigatório")
        Long veterinarioId,

        // Valida que a data e a hora do atendimento devem ser informadas
        @NotNull(message = "A data e hora são obrigatórias")
        LocalDateTime dataHora,

        // Valida que o tipo/motivo do atendimento deve ser um dos valores permitidos pelo Enum
        @NotNull(message = "O tipo de atendimento é obrigatório")
        TipoAtendimento tipo,

        // Valida que o valor informado em reais é obrigatório e precisa ser estritamente maior que zero (Regra de Negócio 5)
        @NotNull(message = "O valor é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        BigDecimal valor,

        // Campo opcional para anotações livres, limitando no máximo a 500 caracteres
        @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres")
        String observacao
) {}