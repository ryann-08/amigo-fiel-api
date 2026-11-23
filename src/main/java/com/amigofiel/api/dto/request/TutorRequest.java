// Define o pacote onde este DTO de requisição está localizado
package com.amigofiel.api.dto.request;

// Importações das anotações de validação (Bean Validation)
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TutorRequest {

    // @NotBlank: Impede que o campo venha nulo, vazio ("") ou só com espaços ("   ").
    // @Size: Garante a regra do enunciado (mínimo 3 e máximo 120 caracteres).
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
    private String nome;

    // @Email: Verifica se o texto enviado tem formato válido de e-mail (ex: texto@dominio.com).
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    // @Pattern: Usa expressão regular (Regex) para exigir exatamente 10 ou 11 números seguidos, sem traços ou parênteses.
    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve ter 10 ou 11 dígitos numéricos")
    private String telefone;

    // Validação de obrigatoriedade e tamanho exigida pelo enunciado para a cidade.
    @NotBlank(message = "A cidade é obrigatória")
    @Size(min = 2, max = 80, message = "A cidade deve ter entre 2 e 80 caracteres")
    private String cidade;

    // =========================================================================
    // GETTERS E SETTERS
    // O Spring (via biblioteca Jackson) usa os Setters para preencher este objeto
    // com o JSON vindo do Postman, e o Service usa os Getters para ler os valores.
    // =========================================================================

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
}