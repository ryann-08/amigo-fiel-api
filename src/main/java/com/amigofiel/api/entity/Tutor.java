package com.amigofiel.api.entity;

//fazendo importacoes para o codigo rodar, sem erro
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

//aqui estamos criando, onde ele vai morar la no banco de dados
@Entity
@Table(name = "tutores")

public class Tutor {
    //aqui onde cria a tabela do tutor, que vai guardar la no banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //vertical slice, garantir que o usuario nao envie dados invalidos ou vazios
    @NotBlank(message = "O nome é obrigatorio")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 a 120 caracteres")

    //criacao das tabelas de acordo com oq foi pedido
    //private como encapsulamento
    private String nome;
    private String email;
    private String cidade;
    //telefone com DDD (criando em string para evitar o estouro numerico e preservar o 0
    private String telefone;
    //momento em que um registro foi criado
    private LocalDateTime dataCadastro;
    //status para saber se o tutor esta ativo no sistema
    private Boolean ativo;


    //metodo que roda automaticamente antes de salvar o tutor no banco de dados
    //sao campos que o usuario nao tera que escrever nada
    @PrePersist
    public void prePersist(){
        this.dataCadastro = LocalDateTime.now(); // pega a data e hora exata no momento do cadastro
        this.ativo = true;
    }
    //criei os metodos getters e setters, para conseguir acessar o atributo que esta no private
    //getters para buscar, setters para inserir

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }



}
