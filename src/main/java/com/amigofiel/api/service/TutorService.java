package com.amigofiel.api.service;

import com.amigofiel.api.dto.request.TutorRequest;
import com.amigofiel.api.dto.response.TutorResponse;
import com.amigofiel.api.entity.Tutor;
import com.amigofiel.api.exception.BusinessRuleException;
import com.amigofiel.api.exception.ResourceNotFoundException;
import com.amigofiel.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service // Marca a classe como um componente de serviço (regras de negócio)
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    // CADASTRAR: Recebe o Request, valida o e-mail, salva e retorna o Response
    // @Transactional: se algo falhar no meio, o banco desfaz tudo (rollback)
    @Transactional
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

    // LISTAR COM FILTROS E PAGINAÇÃO: busca paginada usando a consulta personalizada do repositório
    public Page<TutorResponse> listar(String nome, String cidade, Pageable pageable) {
        return tutorRepository.filtrarTutores(nome, cidade, pageable)
                .map(TutorResponse::new); // Converte cada Tutor da página em TutorResponse
    }

    // BUSCAR POR ID (Retornando Response para o Controller)
    public TutorResponse buscarPorIdDto(Long id) {
        Tutor tutor = buscarPorId(id); // Reutiliza o método abaixo que valida se existe
        return new TutorResponse(tutor);
    }

    // MÉTODO AUXILIAR INTERNO: Busca a entidade pura (usado na atualização)
    public Tutor buscarPorId(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado com o ID: " + id));
    }

    // ATUALIZAR (PUT /{id}): altera os dados permitidos e retorna o Response atualizado
    @Transactional
    public TutorResponse atualizar(Long id, TutorRequest request) {
        Tutor tutor = buscarPorId(id); // Garante que o tutor existe (senão lança 404)

        // Regra de negócio: o e-mail não pode repetir (Erro 409).
        // Só conferimos no banco se o e-mail enviado é diferente do atual; se fosse o mesmo,
        // o próprio tutor seria acusado de duplicidade.
        if (!tutor.getEmail().equals(request.getEmail())
                && tutorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Já existe um tutor cadastrado com este e-mail.");
        }

        tutor.setNome(request.getNome());
        tutor.setEmail(request.getEmail());
        tutor.setTelefone(request.getTelefone());
        tutor.setCidade(request.getCidade());

        // O campo ativo é opcional no JSON: se não vier, mantém o valor que já estava no banco.
        // É por aqui que se inativa ou reativa um tutor (ele nunca é apagado).
        if (request.getAtivo() != null) {
            tutor.setAtivo(request.getAtivo());
        }

        Tutor atualizado = tutorRepository.save(tutor);
        return new TutorResponse(atualizado);
    }
}