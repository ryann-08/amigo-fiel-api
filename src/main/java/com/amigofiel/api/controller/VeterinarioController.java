package com.amigofiel.api.controller;

//essa classe é como se fosse o garçom, ela pega o que vem do front ou postman, e manda para o service
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
import org.springframework.data.web.PageableDefault;


//primeira anotação @RestController: Avisa o Spring Boot:
//"Ei, esta classe é um Controller REST!". Significa que ela vai receber requisições da web
// (via Postman, navegador, etc.) e vai devolver respostas diretamente em formato JSON.
@RestController

//@RequestMapping é o endereço base, significa que todas as rotas criadas nesta classe vão começar
//obrigatoriamente com /veterinarios - se quer buscar algo seria /veterinarios/1
@RequestMapping("/veterinarios")
public class VeterinarioController {

    //injetando o service, seguindo a analogia do garçom
    //essa anotação aqui está falando com a cozinha, que seria o service
    @Autowired
    private VeterinarioService veterinarioService;

    //@PostMapping - é o método post, ou seja, quando a pessoa quiser criar um registro novo a requisição cai aqui
    //@RequestBody pega o json e transforma em um objeto e manda para o service, assim o service entende, diferente de um json que o service não entende
    //@Valid se o cliente enviar algum dado errado ou faltando ele interrompe a requisição na hora
    //@Valid mesma analogia do @NotBlank @NotNull
    @PostMapping
    public ResponseEntity<VeterinarioResponse> criar(@RequestBody @Valid VeterinarioRequest request) {

        //aqui na mesma analogia da cozinha, ele pega o objeto do request já validado e entrega nas mãos do service
        VeterinarioResponse response = veterinarioService.criar(request);
        //tudo isso dando certo, precisamos devolver ao cliente o código de sucesso 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Listar com Filtros e Paginação (GET)
    // @GetMapping indica que quando baterem na URL /veterinarios com GET, cai aqui para listar paginado e filtrado
    // @PageableDefault: se o cliente não informar nada, vem página de 20 itens ordenada por id.
    // A ordenação fixa garante que a ordem não mude entre uma página e outra.
    @GetMapping
    public ResponseEntity<Page<VeterinarioResponse>> listar(
            VeterinarioFilter filter,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
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