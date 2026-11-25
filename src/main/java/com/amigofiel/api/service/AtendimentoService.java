package com.amigofiel.api.service;

import com.amigofiel.api.dto.request.AtendimentoRequest;

import com.amigofiel.api.dto.request.StatusAtendimentoRequest;
import com.amigofiel.api.dto.response.AtendimentoResponse;
import com.amigofiel.api.entity.Atendimento;
import com.amigofiel.api.entity.Tutor;
import com.amigofiel.api.entity.Veterinario;
import com.amigofiel.api.enums.StatusAtendimento;
import com.amigofiel.api.repository.AtendimentoRepository;
import com.amigofiel.api.repository.TutorRepository;
import com.amigofiel.api.repository.VeterinarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Camada de servico que centraliza todas as regras de negocio dos atendimentos.
@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final TutorRepository tutorRepository;
    private final VeterinarioRepository veterinarioRepository;

    // Injecao de dependencias via construtor (boa pratica recomendada pelo Spring)
    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                              TutorRepository tutorRepository,
                              VeterinarioRepository veterinarioRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.tutorRepository = tutorRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    // Cria um novo agendamento validando a existencia do tutor, do veterinario e conflitos de horario
    //@Transctional se tudo ocorrer bem ele faz o commit se ocorrer mal ele faz o rollback
    @Transactional
    public AtendimentoResponse criar(AtendimentoRequest request) {
        //  Busca o tutor no banco; se nao existir, lanca erro
        Tutor tutor = tutorRepository.findById(request.tutorId())
                .orElseThrow(() -> new EntityNotFoundException("Tutor nao encontrado com o ID: " + request.tutorId()));

        //  Busca o veterinario no banco; se nao existir, lanca erro
        Veterinario veterinario = veterinarioRepository.findById(request.veterinarioId())
                .orElseThrow(() -> new EntityNotFoundException("Veterinario nao encontrado com o ID: " + request.veterinarioId()));

        //  Regra de Negocio: Verifica se o veterinario ja tem um atendimento AGENDADO no mesmo horario
        boolean horarioOcupado = atendimentoRepository
                .findByVeterinarioIdAndDataHoraAndStatus(request.veterinarioId(), request.dataHora(), StatusAtendimento.AGENDADO)
                .isPresent();

        if (horarioOcupado) {
            throw new IllegalArgumentException("O veterinario ja possui um atendimento agendado para este horario.");
        }

        //  Monta a entidade Atendimento com os dados validados
        Atendimento atendimento = new Atendimento();
        atendimento.setTutor(tutor);
        atendimento.setVeterinario(veterinario);
        atendimento.setDataHora(request.dataHora());
        atendimento.setTipo(request.tipo());
        atendimento.setValor(request.valor());
        atendimento.setObservacao(request.observacao());
        // O status ja nasce como AGENDADO por padrao na propria entidade

        //  Salva no banco e devolve convertido para o DTO de resposta o (response)
        Atendimento atendimentoSalvo = atendimentoRepository.save(atendimento);
        return new AtendimentoResponse(atendimentoSalvo);
    }

    // Lista todos os atendimentos cadastrados convertendo cada um para DTO
    @Transactional(readOnly = true)
    public List<AtendimentoResponse> listarTodos() {
        return atendimentoRepository.findAll()
                .stream()
                .map(AtendimentoResponse::new)
                .toList();
    }

    // Busca um unico atendimento pelo ID
    @Transactional(readOnly = true)
    public AtendimentoResponse buscarPorId(Long id) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento nao encontrado com o ID: " + id));
        return new AtendimentoResponse(atendimento);
    }

    // Atualiza apenas o status do atendimento (ex: de AGENDADO para CONCLUIDO ou CANCELADO)
    @Transactional
    public AtendimentoResponse atualizarStatus(Long id, StatusAtendimentoRequest request) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento nao encontrado com o ID: " + id));

        atendimento.setStatus(request.status());
        Atendimento atendimentoAtualizado = atendimentoRepository.save(atendimento);

        return new AtendimentoResponse(atendimentoAtualizado);
    }
}
