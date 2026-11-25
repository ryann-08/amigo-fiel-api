package com.amigofiel.api.repository;

import com.amigofiel.api.entity.Atendimento;
import com.amigofiel.api.enums.StatusAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDateTime;
import java.util.Optional;

// Interface de repositorio que gerencia as operacoes de banco de dados para a entidade Atendimento.
// Herda JpaRepository para operacoes padrao de CRUD e JpaSpecificationExecutor para buscas avancadas.
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>, JpaSpecificationExecutor<Atendimento> {

    // Regra de Negocio: O mesmo veterinario nao pode ter dois atendimentos AGENDADOS no mesmo horario exato.
    // Este metodo cria uma consulta automatica no banco buscando se ja existe um atendimento
    // para aquele veterinario, naquela data/hora especifica, e que esteja com o status AGENDADO.
    Optional<Atendimento> findByVeterinarioIdAndDataHoraAndStatus(
            Long veterinarioId, LocalDateTime dataHora, StatusAtendimento status
    );
}