package com.amigofiel.api.service;

import com.amigofiel.api.dto.request.TutorRequest;
import com.amigofiel.api.dto.response.TutorResponse;
import com.amigofiel.api.entity.Tutor;
import com.amigofiel.api.exception.BusinessRuleException;
import com.amigofiel.api.exception.ResourceNotFoundException;
import com.amigofiel.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service // Marca a classe como um componente de serviço (regras de negócio)
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    // CADASTRAR: Recebe o Request, valida o e-mail, salva e retorna o Response
    public TutorResponse cadastrar(TutorRequest request) {
        // Regra de negócio: Verifica se já existe um tutor com este e-mail (Erro 409)
        if (tutorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Já existe um tutor cadastrado com este e-mail.");
        }

        // Cria uma nova Entidade Tutor para salvar no PostgreSQL
        Tutor tutor = new Tutor();
        tutor.setNome(request.getNome());
        tutor.setEmail(request.getEmail());
        tutor.setTelefone(request.getTelefone());
        tutor.setCidade(request.getCidade());
        tutor.setDataCadastro(LocalDateTime.now()); // Preenchido automaticamente pela API
        tutor.setAtivo(true); // O enunciado exige que nasça ativo

        Tutor salvo = tutorRepository.save(tutor);

        // Converte a entidade salva em DTO de resposta
        return new TutorResponse(salvo);
    }

    // LISTAR TODOS: Busca do banco e converte cada Tutor em TutorResponse
    public List<TutorResponse> listarTodos() {
        return tutorRepository.findAll().stream()
                .map(TutorResponse::new) // Transforma cada Tutor da lista em TutorResponse
                .collect(Collectors.toList());
    }

    // BUSCAR POR ID (Retornando Response para o Controller)
    public TutorResponse buscarPorIdDto(Long id) {
        Tutor tutor = buscarPorId(id); // Reutiliza o método abaixo que valida se existe
        return new TutorResponse(tutor);
    }

    // MÉTODO AUXILIAR INTERNO: Busca a entidade pura (usado para atualizar e deletar)
    public Tutor buscarPorId(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado com o ID: " + id));
    }

    // ATUALIZAR: Altera os dados permitidos e retorna o Response atualizado
    public TutorResponse atualizar(Long id, TutorRequest request) {
        Tutor tutor = buscarPorId(id); // Garante que o tutor existe (senão lança 404)

        tutor.setNome(request.getNome());
        tutor.setEmail(request.getEmail());
        tutor.setTelefone(request.getTelefone());
        tutor.setCidade(request.getCidade());

        Tutor atualizado = tutorRepository.save(tutor);
        return new TutorResponse(atualizado);
    }

    // DELETAR: Remove o tutor do banco se ele existir
    public void deletar(Long id) {
        Tutor tutor = buscarPorId(id);
        tutorRepository.delete(tutor);
    }
}