package com.amigofiel.api.dto.response;

// Importa a entidade Veterinario para podermos extrair os dados dela
import com.amigofiel.api.entity.Veterinario;

public class VeterinarioResponse {

    private Long id;
    private String nome;
    private String crmv;
    private String especialidade;
    private boolean ativo;

    // Construtor de conversão:
    // Recebe a Entidade Veterinario do banco e copia os valores para o DTO de resposta
    public VeterinarioResponse(Veterinario veterinario) {
        this.id = veterinario.getId();
        this.nome = veterinario.getNome();
        this.crmv = veterinario.getCrmv();
        this.especialidade = veterinario.getEspecialidade() != null ? veterinario.getEspecialidade().toString() : null; // Ajuste conforme seu tipo de especialidade
        this.ativo = veterinario.isAtivo();
    }


    // GETTERS
    // O Spring usa os Getters para transformar os dados deste DTO em JSON para o cliente
    public Long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getCrmv() {
        return crmv;
    }
    public String getEspecialidade() {
        return especialidade;
    }
    public boolean isAtivo() {
        return ativo;
    }
}