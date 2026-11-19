package com.amigofiel.api.controller;

//importacaoes para o arquivo rodar
import com.amigofiel.api.entity.Tutor;
import com.amigofiel.api.repository.TutorRepository;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//@RestController informa ao spring que esta classe é um controlador web
//ele vai receber requisicoes HTTP, e ira retorna em json
@RestController

//@RequestMapping aqui define o endereco base(URL) para acessar as rotas deste controller
//ou seja, qualquer requisicao para tutores vai para "http://localhost:8080/tutores"
@RequestMapping("/tutores")

public class TutorController {

    // @Autowired: Injeção de dependência.
    // O Spring se encarrega de criar e injetar o TutorRepository aqui para usarmos,
    // sem precisarmos usar o operador "new TutorRepository()" manualmente.
    @Autowired
    private TutorRepository tutorRepository;


    //aqui o famoso CRUD (Create = post -> cria/cadastra) (Read = get -> busca/ler) (Update = post -> atualiza/edita) (Delete = delete -> apaga/deleta)

    //@PostMapping define que esse metodo vai atender requisicoes do tipo post (para criar/cadastrar novos dados)
    @PostMapping
    public Tutor cadastrar(@RequestBody @Valid Tutor tutor) {
        // @RequestBody: Pega o JSON enviado no corpo da requisição (ex: pelo Postman) e converte automaticamente em um objeto Java do tipo Tutor.
        // @Valid: Ativa o Bean Validation. O Spring vai olhar as regras (@NotBlank, @Size) que colocamos na entidade Tutor
        // e vai barrar a requisição antes de salvar se algum dado estiver inválido!

        // tutorRepository.save(tutor): Salva o tutor no banco PostgreSQL e retorna o objeto salvo (com o ID gerado).
        return tutorRepository.save(tutor);

    }

    //metodo get de buscar/ler
    @GetMapping
    public List<Tutor> ListarTodos(){
        //chama o repositorio para buscar todas as linhas da tabela no banco e devolve com uma lista JSON
        return tutorRepository.findAll();
    }

    // @PutMapping("/{id}"): Responde a requisições PUT (usadas para atualizar dados existentes)
    @PutMapping("/{id}")
    public Tutor atualizar(@PathVariable long id, @RequestBody @Valid Tutor tutorAtualizado){

        //garante que todo objeto vai manter o mesmo ID que veio da URL
        tutorAtualizado.setId(id);

        // O save() do JpaRepository atualiza no banco porque o objeto já tem um ID!
        //o.save, vai no banco e ve se tem um id se nao tiver ele sabe que é um cadastro novo(insert)
        //mas se ja tem id ele sabe que é uma atualizacao(update)
        return tutorRepository.save(tutorAtualizado);
    }

    // @DeleteMapping("/{id}"): Responde a requisições do tipo DELETE para apagar um registro pelo ID
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        // @PathVariable: Pega o ID que veio na URL (ex: /tutores/1) e joga na variável Long id

        // Manda o repositório apagar do banco o registro correspondente a esse ID
        tutorRepository.deleteById(id);
    }

}