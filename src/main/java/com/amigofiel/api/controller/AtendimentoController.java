package com.amigofiel.api.controller;

import com.amigofiel.api.dto.request.AtendimentoRequest;
import com.amigofiel.api.dto.request.StatusAtendimentoRequest;
import com.amigofiel.api.dto.response.AtendimentoResponse;
import com.amigofiel.api.service.AtendimentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST que expoe os endpoints HTTP para gerenciar os atendimentos.
@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    // Injecao de dependencia do Service via construtor
    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    // Endpoint POST /atendimentos -> Cria um novo agendamento
    // @Valid ativa as validacoes (@NotNull, @DecimalMin) definidas dentro do AtendimentoRequest
    @PostMapping
    public ResponseEntity<AtendimentoResponse> criar(@RequestBody @Valid AtendimentoRequest request) {
        AtendimentoResponse response = atendimentoService.criar(request);
        // Retorna status 201 (Created) junto com os dados do atendimento criado
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint GET /atendimentos -> Lista todos os atendimentos cadastrados
    @GetMapping
    public ResponseEntity<List<AtendimentoResponse>> listarTodos() {
        List<AtendimentoResponse> lista = atendimentoService.listarTodos();
        // Retorna status 200 (OK) com a lista no corpo da resposta
        return ResponseEntity.ok(lista);
    }

    // Endpoint GET /atendimentos/{id} -> Busca os detalhes de um atendimento especifico
    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponse> buscarPorId(@PathVariable Long id) {
        AtendimentoResponse response = atendimentoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // Endpoint PATCH /atendimentos/{id}/status -> Atualiza parcialmente apenas o status do atendimento
    @PatchMapping("/{id}/status")
    public ResponseEntity<AtendimentoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid StatusAtendimentoRequest request) {
        AtendimentoResponse response = atendimentoService.atualizarStatus(id, request);
        return ResponseEntity.ok(response);
    }
}