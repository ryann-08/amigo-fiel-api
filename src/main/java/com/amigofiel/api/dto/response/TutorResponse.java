// Define o pacote onde este DTO de resposta está localizado
package com.amigofiel.api.dto.response;

// Importa a entidade Tutor para podermos extrair os dados dela
import com.amigofiel.api.entity.Tutor;
import java.time.LocalDateTime;

public class TutorResponse {

    // Atributos exatos (em camelCase) que o enunciado exige na resposta JSON
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cidade;
    private LocalDateTime dataCadastro;
    private boolean ativo;

    // Construtor de conversão:
    // Recebe um objeto da Entidade 'Tutor' (vindo do banco de dados) e copia os valores
    // para os atributos deste DTO de resposta. Facilita a vida no Service.
    public TutorResponse(Tutor tutor) {
        this.id = tutor.getId();
        this.nome = tutor.getNome();
        this.email = tutor.getEmail();
        this.telefone = tutor.getTelefone();
        this.cidade = tutor.getCidade();
        this.dataCadastro = tutor.getDataCadastro();
        this.ativo = tutor.getAtivo();
    }

    // =========================================================================
    // GETTERS
    // O Spring usa apenas os Getters para ler os valores deste objeto e
    // transformá-los no JSON final enviado ao cliente. Não precisamos de Setters aqui.
    // =========================================================================

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getCidade() { return cidade; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public boolean isAtivo() { return ativo; }
}