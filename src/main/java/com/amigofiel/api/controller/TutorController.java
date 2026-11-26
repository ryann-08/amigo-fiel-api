package com.amigofiel.api.controller;

import com.amigofiel.api.dto.request.TutorRequest;
import com.amigofiel.api.dto.response.TutorResponse;
import com.amigofiel.api.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.amigofiel.api.dto.filter.TutorFilter;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @PostMapping
    public ResponseEntity<TutorResponse> cadastrar(@RequestBody @Valid TutorRequest request) {
        // Recebe o Request validado e repassa para o service
        TutorResponse novoTutor = tutorService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoTutor); // Retorna 201 Created
    }

    // LISTAR COM FILTROS E PAGINAÇÃO:
    // O Spring recebe os parâmetros opcionais da URL (ex: ?nome=Ana&cidade=Campinas)
    // e os preenche automaticamente dentro do objeto TutorFilter.
    // O objeto Pageable controla a paginação (página atual, quantidade por página e ordenação).
    // @PageableDefault: se o cliente não informar nada, vem página de 20 itens ordenada por id.
    // A ordenação fixa garante que a ordem não mude entre uma página e outra.
    @GetMapping
    public ResponseEntity<Page<TutorResponse>> listar(
            TutorFilter filtro,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {

        // Repassa os valores do filtro (nome e cidade) e a paginação para o service processar a busca no banco
        Page<TutorResponse> tutores = tutorService.listar(filtro.getNome(), filtro.getCidade(), pageable);

        // Retorna o status HTTP 200 OK junto com a página contendo os tutores encontrados
        return ResponseEntity.ok(tutores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorResponse> detalhar(@PathVariable Long id) {
        TutorResponse tutor = tutorService.buscarPorIdDto(id);
        return ResponseEntity.ok(tutor); // Retorna 200 OK com o tutor encontrado
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorResponse> atualizar(@PathVariable Long id, @RequestBody @Valid TutorRequest request) {
        TutorResponse tutorAtualizado = tutorService.atualizar(id, request);
        return ResponseEntity.ok(tutorAtualizado); // Retorna 200 OK
    }
}