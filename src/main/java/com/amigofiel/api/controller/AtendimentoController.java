package com.amigofiel.api.controller;

import com.amigofiel.api.dto.filter.AtendimentoFilter;
import com.amigofiel.api.dto.request.AtendimentoRequest;
import com.amigofiel.api.dto.request.StatusAtendimentoRequest;
import com.amigofiel.api.dto.response.AtendimentoResponse;
import com.amigofiel.api.service.AtendimentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Endpoints HTTP dos atendimentos. A lógica fica no service; aqui só entra e sai de dados.
@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    // POST /atendimentos -> agenda um atendimento (201)
    @PostMapping
    public ResponseEntity<AtendimentoResponse> criar(@RequestBody @Valid AtendimentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoService.criar(request));
    }

    // GET /atendimentos -> lista com filtros opcionais e paginação (?page=0&size=20)
    // A ordenação padrão por id mantém a ordem estável entre as páginas.
    @GetMapping
    public ResponseEntity<Page<AtendimentoResponse>> listar(
            AtendimentoFilter filter,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(atendimentoService.listar(filter, pageable));
    }

    // GET /atendimentos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(atendimentoService.buscarPorId(id));
    }

    // PATCH /atendimentos/{id}/status -> muda só o status
    @PatchMapping("/{id}/status")
    public ResponseEntity<AtendimentoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid StatusAtendimentoRequest request) {
        return ResponseEntity.ok(atendimentoService.atualizarStatus(id, request));
    }
}