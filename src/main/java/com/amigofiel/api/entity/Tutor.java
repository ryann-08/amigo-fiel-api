package com.amigofiel.api.entity;

//fazendo importacoes para o codigo rodar, sem erro
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

//aqui estamos criando, onde ele vai morar la no banco de dados
@Entity
@Table(name = "tutores")

public class Tutor {
    //aqui onde cria a tabela do tutor, que vai guardar la no banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //criacao das tabelas de acordo com oq foi pedido
    //private como encapsulamento
    //vertical slice, garantir que o usuario nao envie dados invalidos ou vazios
    //@NotBlank esta criando a regra de negocio onde obriga, a fazer determinadas coisas para nao dar erro
    @NotBlank(message = "O nome é obrigatorio")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 a 120 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Column(unique = true) // Garante que é único no banco
    private String email;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(min = 2, max = 80, message = "A cidade deve ter entre 2 e 80 caracteres")
    private String cidade;

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "^\\d{10,11}$", message = "O telefone deve conter apenas dígitos, com 10 ou 11 caracteres")
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
