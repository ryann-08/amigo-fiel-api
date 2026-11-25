package com.amigofiel.api.dto.response;

import com.amigofiel.api.entity.Atendimento;
import com.amigofiel.api.enums.StatusAtendimento;
import com.amigofiel.api.enums.TipoAtendimento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Objeto de Transferencia de Dados (DTO) usado para formatar a resposta JSON
// que a API devolve ao cliente apos criar ou buscar um atendimento,
// trazendo os IDs e tambem os nomes por extenso do tutor e do veterinario.
public record AtendimentoResponse(
        Long id,
        Long tutorId,
        String tutorNome,       // Nome por extenso do tutor preenchido automaticamente pela API
        Long veterinarioId,
        String veterinarioNome, // Nome por extenso do veterinario preenchido automaticamente pela API
        LocalDateTime dataHora,
        TipoAtendimento tipo,
        BigDecimal valor,
        String observacao,
        StatusAtendimento status
) {
    // Construtor auxiliar que recebe a Entidade Atendimento vinda do banco de dados
    // e a converte automaticamente para este DTO de resposta.
    public AtendimentoResponse(Atendimento atendimento) {
        this(
                atendimento.getId(),
                atendimento.getTutor().getId(),
                atendimento.getTutor().getNome(),
                atendimento.getVeterinario().getId(),
                atendimento.getVeterinario().getNome(),
                atendimento.getDataHora(),
                atendimento.getTipo(),
                atendimento.getValor(),
                atendimento.getObservacao(),
                atendimento.getStatus()
        );
    }
}