package com.amigofiel.api.entity;

import jakarta.persistence.*;


// Diz ao Java/Spring que esta classe é uma entidade do banco de dados
//em portugues claro: avisa ao banco que isso é uma tabela
@Entity
//  Define o nome da tabela no PostgreSQL como "veterinarios"
@Table(name = "veterinarios")
public class Veterinario {

    // Chave primária (ID) gerada automaticamente pelo banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome do veterinário (obrigatório)
    @Column(nullable = false)
    private String nome;

    // CRMV do veterinário (obrigatório e único no sistema)
    @Column(nullable = false, unique = true)
    private String crmv;


    // Especialidade (ex: Clínica Geral, Cirurgia, etc.)
    @Column(nullable = false)
    private String especialidade;

    // Status para saber se está ativo (padrão é true)
    @Column(nullable = false)
    private boolean ativo = true;


    // CONSTRUTORES

    // Construtor vazio: Obrigatório para o Hibernate/JPA conseguir criar instâncias da classe por trás dos panos
    public Veterinario() {
    }

    // Construtor com os dados principais para facilitar a criação de objetos se necessário
    public Veterinario(String nome, String crmv, String especialidade) {
        this.nome = nome;
        this.crmv = crmv;
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

}