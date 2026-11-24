package com.amigofiel.api.dto.filter;

//filter serve, quando que buscar algo em especifico, diferente do que por id etc
//seria tipo buscar pelo cargo, ai tras todas as pessoas daquele cargo, nao uma pessoa so em especifico
import com.amigofiel.api.enums.Especialidade;

public class VeterinarioFilter {

    //aqui guardamos os nomes e a especialidade que o usuario pode filtrar
    //tudo private para manter o encapsulamento
    private String nome;
    private Especialidade especialidade;


    // Getters e Setters]

    //setNome / setEspecialidade) Permitem que o Spring Boot pegue os valores digitados na URL da requisição
    // (ex: ?nome=Carlos) e insira(atribua) dentro do nosso objeto VeterinarioFilter.

    //getNome / getEspecialidade) Permitem que o nosso Service e Repository leiam esses valores guardados
    // para poder montar a consulta correta no banco de dados.
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }
}