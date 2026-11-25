package com.amigofiel.api.entity;

import com.amigofiel.api.enums.StatusAtendimento;
import com.amigofiel.api.enums.TipoAtendimento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Entidade JPA que representa a tabela atendimentos no banco de dados PostgreSQL.
// É o centro do sistema, fazendo a ponte entre Tutor e Veterinario.
@Entity
@Table(name = "atendimentos")
public class Atendimento {

    // Identificador único gerado automaticamente pelo banco
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com a entidade Tutor (muitos atendimentos para um tutor)
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    // Relacionamento com a entidade Veterinario (muitos atendimentos para um veterinario)
    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    // Data e hora em que o atendimento vai acontecer (sem fuso horário)
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    // Tipo ou motivo do atendimento (CONSULTA, VACINA, EXAME ou RETORNO)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAtendimento tipo;

    // Valor cobrado pelo atendimento em reais (decimal com 2 casas)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    // Observação livre opcional com limite de até 500 caracteres
    @Column(length = 500)
    private String observacao;

    // Situação atual do atendimento (nasce sempre como AGENDADO por regra de negócio)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAtendimento status = StatusAtendimento.AGENDADO;

    // Getters e Setters para permitir o acesso e modificação dos atributos da classe

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Tutor getTutor() {
        return tutor;
    }
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
    public Veterinario getVeterinario() {
        return veterinario;
    }
    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    public TipoAtendimento getTipo() {
        return tipo;
    }
    public void setTipo(TipoAtendimento tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getValor() {
        return valor;
    }
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    public StatusAtendimento getStatus() {
        return status;
    }
    public void setStatus(StatusAtendimento status) {
        this.status = status;
    }
}