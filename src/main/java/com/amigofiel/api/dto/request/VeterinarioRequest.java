package com.amigofiel.api.dto.request;

//o cliente que atuaizar/cadastrar, essa classe garante que os dados chguem organizados
//oass por um 'controle de qualidade' automatico antes mesmo do codigo do (service) rodar
import com.amigofiel.api.enums.Especialidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VeterinarioRequest {

    //aqui definimos os campos que o usuario precisa mandar e as regras que ele devem seguir
    //@NotBlank devine que tem que ser obtigatorio o campo nao pode ser vazio
    //@Size define o tamanho minimo e maximo permitido
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
    private String nome;

    @NotBlank(message = "O CRMV é obrigatório")
    @Size(min = 4, max = 20, message = "O CRMV deve ter entre 4 e 20 caracteres")
    private String crmv;

    //@Notnull garante que o usuario nao mande a especialidade em branco
    //detalhe @notblank so aceita String, e com o enum da erro de compilacao
    //por isso etou usando notnull, que anda junto com enum que cadastrei
    @NotNull(message = "A especialidade é obrigatória")
    private Especialidade especialidade;

    private Boolean ativo; // Usado no PUT

    // GETTERS (Essenciais para o Service conseguir ler os dados)
    public String getNome() {
        return nome;
    }

    public String getCrmv() {
        return crmv;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}