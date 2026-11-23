package com.amigofiel.api.controller;

import com.amigofiel.api.dto.request.TutorRequest;
import com.amigofiel.api.dto.response.TutorResponse;
import com.amigofiel.api.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<TutorResponse>> listarTodos() {
        return ResponseEntity.ok(tutorService.listarTodos()); // Retorna 200 OK com a lista
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorResponse> detalhar(@PathVariable Long id) {
        TutorResponse tutor = tutorService.buscarPorIdDto(id);
        return ResponseEntity.ok(tutor); // Retorna 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorResponse> atualizar(@PathVariable Long id, @RequestBody @Valid TutorRequest request) {
        TutorResponse tutorAtualizado = tutorService.atualizar(id, request);
        return ResponseEntity.ok(tutorAtualizado); // Retorna 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tutorService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}