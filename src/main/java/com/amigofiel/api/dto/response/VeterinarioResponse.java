package com.amigofiel.api.dto.response;
//essa classe aqui é como se fosse o inverso de request, o request é oq o usuario manda para nois
//ja o reponse é a nota fiscal ou a caixa de presente que devolvemos para o cliente, apos a operacao da certo

import com.amigofiel.api.enums.Especialidade;

public class VeterinarioResponse {

    //aqui sao atributos que volta para o cliente
    private Long id;
    private String nome;
    private String crmv;
    private Especialidade especialidade;
    private boolean ativo;

    // Construtor que o Service está chamando
    //como funciona o fluco de dados
    //basicamente pega os dados que vieram do service guarda temporariamente dentro de uma caixa
    //o service com o return, manda esse reponse para o controller
    //O Controller recebe o Response das mãos do Service e apenas o repassa para o Spring/Jackson, que o transforma em JSON e entrega para o usuário.
    public VeterinarioResponse(Long id, String nome, String crmv, Especialidade especialidade, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.crmv = crmv;
        this.especialidade = especialidade;
        this.ativo = ativo;
    }

    // Getters do Response
    //usamos metodos getter aqui, pq vamos supor o nome de uma pessoa esta cadastrado no banco de dados atrasves do contrutor
    //mas esta com private nao consigo puxar eses dados para o usuario
    //por isso esse metodo
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCrmv() { return crmv; }
    public Especialidade getEspecialidade() { return especialidade; }
    public boolean isAtivo() { return ativo; }
}
