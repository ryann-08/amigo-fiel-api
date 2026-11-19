package com.amigofiel.api.controller;

//importacaoes para o arquivo rodar
import com.amigofiel.api.entity.Tutor;
import com.amigofiel.api.repository.TutorRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.springframework.http.ResponseEntity;

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

    // Método GET para buscar um tutor específico pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Tutor> detalhar(@PathVariable Long id) {
        // Busca no banco pelo ID informado na URL
        Optional<Tutor> tutor = tutorRepository.findById(id);

        // Se encontrar, retorna 200 OK com o tutor; senão, retorna 404 Not Found
        if (tutor.isPresent()) {
            return ResponseEntity.ok(tutor.get());
        }

        return ResponseEntity.notFound().build();
    }

    // @PutMapping("/{id}"): Responde a requisições PUT (usadas para atualizar dados existentes)
    @PutMapping("/{id}")
    public ResponseEntity<Tutor> atualizar(@PathVariable Long id, @RequestBody Tutor dadosAtualizados) {
        // Se o ID não existe, retorna 404 Not Found
        if (!tutorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Se existe, atualiza e retorna 200 OK
        dadosAtualizados.setId(id);
        Tutor tutorSalvo = tutorRepository.save(dadosAtualizados);
        return ResponseEntity.ok(tutorSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        // Se o ID não existe, retorna 404 Not Found
        if (!tutorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Se existe, deleta e retorna 204 No Content (sucesso sem corpo)
        tutorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}