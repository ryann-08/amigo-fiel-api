package com.amigofiel.api.dto.filter;

import com.amigofiel.api.enums.StatusAtendimento;
import com.amigofiel.api.enums.TipoAtendimento;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

// Parâmetros opcionais de busca do GET /atendimentos (ex: ?status=AGENDADO&tipo=VACINA).
// O Spring preenche cada campo com o parâmetro de mesmo nome da URL.
public class AtendimentoFilter {

    private StatusAtendimento status;
    private TipoAtendimento tipo;
    private Long tutorId;
    private Long veterinarioId;

    // @DateTimeFormat diz ao Spring que a data vem no formato ISO, ex: 2026-03-01T00:00:00
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataFim;

    public StatusAtendimento getStatus() {
        return status;
    }

    public void setStatus(StatusAtendimento status) {
        this.status = status;
    }

    public TipoAtendimento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAtendimento tipo) {
        this.tipo = tipo;
    }

    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    public Long getVeterinarioId() {
        return veterinarioId;
    }

    public void setVeterinarioId(Long veterinarioId) {
        this.veterinarioId = veterinarioId;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }
}