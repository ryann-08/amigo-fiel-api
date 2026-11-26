package com.amigofiel.api.service;

import com.amigofiel.api.dto.filter.AtendimentoFilter;
import com.amigofiel.api.dto.request.AtendimentoRequest;
import com.amigofiel.api.dto.request.StatusAtendimentoRequest;
import com.amigofiel.api.dto.response.AtendimentoResponse;
import com.amigofiel.api.entity.Atendimento;
import com.amigofiel.api.entity.Tutor;
import com.amigofiel.api.entity.Veterinario;
import com.amigofiel.api.enums.StatusAtendimento;
import com.amigofiel.api.exception.BusinessRuleException;
import com.amigofiel.api.exception.ResourceNotFoundException;
import com.amigofiel.api.repository.AtendimentoRepository;
import com.amigofiel.api.repository.TutorRepository;
import com.amigofiel.api.repository.VeterinarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// Regras de negócio dos atendimentos.
@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final TutorRepository tutorRepository;
    private final VeterinarioRepository veterinarioRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                              TutorRepository tutorRepository,
                              VeterinarioRepository veterinarioRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.tutorRepository = tutorRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    // POST /atendimentos
    // @Transactional: se alguma regra falhar no meio, nada é gravado (rollback).
    @Transactional
    public AtendimentoResponse criar(AtendimentoRequest request) {
        // Regra 8: tutor ou veterinário inexistente responde 404
        Tutor tutor = tutorRepository.findById(request.tutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado com o ID: " + request.tutorId()));

        Veterinario veterinario = veterinarioRepository.findById(request.veterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com o ID: " + request.veterinarioId()));

        // Regra 3: pessoa inativa não entra em atendimento novo (409)
        if (!Boolean.TRUE.equals(tutor.getAtivo())) {
            throw new BusinessRuleException("O tutor está inativo e não pode receber novos atendimentos.");
        }
        if (!veterinario.isAtivo()) {
            throw new BusinessRuleException("O veterinário está inativo e não pode receber novos atendimentos.");
        }

        // Regra 4: só atendimentos AGENDADOS ocupam o horário (concluído e cancelado não)
        boolean horarioOcupado = atendimentoRepository.existsByVeterinarioIdAndDataHoraAndStatus(
                veterinario.getId(), request.dataHora(), StatusAtendimento.AGENDADO);
        if (horarioOcupado) {
            throw new BusinessRuleException("O veterinário já possui um atendimento agendado para este horário.");
        }

        Atendimento atendimento = new Atendimento();
        atendimento.setTutor(tutor);
        atendimento.setVeterinario(veterinario);
        atendimento.setDataHora(request.dataHora());
        atendimento.setTipo(request.tipo());
        atendimento.setValor(request.valor());
        atendimento.setObservacao(request.observacao());
        // o status nasce AGENDADO por padrão na entidade

        return new AtendimentoResponse(atendimentoRepository.save(atendimento));
    }

    // GET /atendimentos (filtros opcionais + paginação)
    @Transactional(readOnly = true)
    public Page<AtendimentoResponse> listar(AtendimentoFilter filter, Pageable pageable) {
        // Começa sem restrição e vai acrescentando um filtro para cada parâmetro informado
        Specification<Atendimento> spec = (root, query, cb) -> cb.conjunction();

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filter.getStatus()));
        }
        if (filter.getTipo() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("tipo"), filter.getTipo()));
        }
        if (filter.getTutorId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("tutor").get("id"), filter.getTutorId()));
        }
        if (filter.getVeterinarioId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("veterinario").get("id"), filter.getVeterinarioId()));
        }
        // Os dois limites do intervalo são inclusivos
        if (filter.getDataInicio() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.<LocalDateTime>get("dataHora"), filter.getDataInicio()));
        }
        if (filter.getDataFim() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.<LocalDateTime>get("dataHora"), filter.getDataFim()));
        }

        return atendimentoRepository.findAll(spec, pageable).map(AtendimentoResponse::new);
    }

    // GET /atendimentos/{id}
    @Transactional(readOnly = true)
    public AtendimentoResponse buscarPorId(Long id) {
        return new AtendimentoResponse(buscarEntidadePorId(id));
    }

    // PATCH /atendimentos/{id}/status
    // Regras 6 e 7: só AGENDADO pode virar CONCLUIDO ou CANCELADO. Qualquer outra troca responde 409.
    @Transactional
    public AtendimentoResponse atualizarStatus(Long id, StatusAtendimentoRequest request) {
        Atendimento atendimento = buscarEntidadePorId(id);
        StatusAtendimento atual = atendimento.getStatus();
        StatusAtendimento novo = request.status();

        if (atual != StatusAtendimento.AGENDADO) {
            throw new BusinessRuleException("Atendimento " + atual + " não pode mais mudar de status.");
        }
        if (novo == StatusAtendimento.AGENDADO) {
            throw new BusinessRuleException("Um atendimento AGENDADO só pode mudar para CONCLUIDO ou CANCELADO.");
        }

        atendimento.setStatus(novo);
        return new AtendimentoResponse(atendimentoRepository.save(atendimento));
    }

    // Busca a entidade ou responde 404
    private Atendimento buscarEntidadePorId(Long id) {
        return atendimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atendimento não encontrado com o ID: " + id));
    }
}