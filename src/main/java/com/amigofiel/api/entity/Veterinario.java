package com.amigofiel.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

// 1 Diz ao Java/Spring que esta classe é uma entidade do banco de dados
//em portugues claro: avisa ao banco que isso é uma tabela
@Entity
// 2 Define o nome da tabela no PostgreSQL como "veterinarios"
@Table(name = "veterinarios")
public class Veterinario {

    // 3 Chave primária (ID) gerada automaticamente pelo banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 4. Nome do veterinário (obrigatório)
    @Column(nullable = false)
    private String nome;

    // CRMV do veterinário (obrigatório e único no sistema)
    @Column(nullable = false, unique = true)
    private String crmv;

    // Telefone de contato (obrigatório)
    @Column(nullable = false)
    private String telefone;

    // Especialidade (ex: Clínica Geral, Cirurgia, etc.)
    @Column(nullable = false)
    private String especialidade;

    // Status para saber se está ativo (padrão é true)
    @Column(nullable = false)
    private boolean ativo = true;

    // Data em que o cadastro foi realizado (preenchida automaticamente)
    private LocalDate dataCadastro = LocalDate.now();



    // CONSTRUTORES

    // Construtor vazio: Obrigatório para o Hibernate/JPA conseguir criar instâncias da classe por trás dos panos
    public Veterinario() {
    }

    // Construtor com os dados principais para facilitar a criação de objetos se necessário
    public Veterinario(String nome, String crmv, String telefone, String especialidade) {
        this.nome = nome;
        this.crmv = crmv;
        this.telefone = telefone;
        this.especialidade = especialidade;
    }

    // GETTERS E SETTERS
    // (O encapsulamento: permitem ler e alterar os atributos privados)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}