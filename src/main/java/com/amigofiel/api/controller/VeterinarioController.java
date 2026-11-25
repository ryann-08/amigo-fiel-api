package com.amigofiel.api.controller;

//essa classe é como se fosse o garcom, ela pega oq vem do front ou postman, e manda para o service
import com.amigofiel.api.dto.filter.VeterinarioFilter;
import com.amigofiel.api.dto.request.VeterinarioRequest;
import com.amigofiel.api.dto.response.VeterinarioResponse;
import com.amigofiel.api.service.VeterinarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


//primeira anotacao @RestController: Avisa o Spring Boot:
//"Ei, esta classe é um Controller REST!". Significa que ela vai receber requisições da web
// (via Postman, navegador, etc.) e vai devolver respostas diretamente em formato JSON.
@RestController

//@RequestMapping é o endero base, significa que todas as rotas ccriadas desta classe vai comecar
//obrigatoriamente com /veterinarios - se quer buscar algo seria /veterinarios/1
@RequestMapping("/veterinarios")
public class VeterinarioController {

    //injetando o service, seguinddo a analogia dao garcom
    //essa anotacao aqui, esta falando com a cozinha que seria o service
    @Autowired
    private VeterinarioService veterinarioService;

    //@PostMapping - é o metodo post ou seja quando a pessoa quiser criar um registro novo a requisicao cai aqui
    //@Requestbody pega o json e tranforma em um objeto e manda para o service, assim o service entende diferente de um json que o service nao entende
    //@valid se op cliente enviar algum dado errado ou faltando ele interrompe a requisicao na hora
    //@Valid mesma analogia do @NotBlank @NotNull
    @PostMapping
    public ResponseEntity<VeterinarioResponse> criar(@RequestBody @Valid VeterinarioRequest request) {

        //aqui na mesma analogia da cozinha, ele pega o objeto do request ja validado e entrega nas maos do service
        VeterinarioResponse response = veterinarioService.criar(request);
        //tudo isso dando certo, precisamos entregar uma numeracao ao cliente de sucesso
        //com isso o esse return, faz com que isso aconteca
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Listar com Filtros e Paginação (GET)
    // @GetMapping indica que quando baterem na URL /veterinarios com GET, cai aqui para listar paginado e filtrado
    @GetMapping
    public ResponseEntity<Page<VeterinarioResponse>> listar(VeterinarioFilter filter, Pageable pageable) {
        Page<VeterinarioResponse> response = veterinarioService.listar(filter, pageable);
        return ResponseEntity.ok(response);
    }

    //  Buscar por ID (GET)
    // Pega o ID passado na URL (ex: /veterinarios/1) e busca o veterinário específico
    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioResponse> buscarPorId(@PathVariable Long id) {
        VeterinarioResponse response = veterinarioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // Atualizar (PUT)
    // Pega o ID da URL e o JSON novo no corpo da requisição para atualizar os dados do veterinário
    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioResponse> atualizar(@PathVariable Long id, @RequestBody @Valid VeterinarioRequest request) {
        VeterinarioResponse response = veterinarioService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }


}