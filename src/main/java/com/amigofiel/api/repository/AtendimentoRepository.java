package com.amigofiel.api.repository;

import com.amigofiel.api.entity.Atendimento;
import com.amigofiel.api.enums.StatusAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;

// JpaRepository traz o CRUD básico; JpaSpecificationExecutor permite montar filtros dinâmicos.
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>, JpaSpecificationExecutor<Atendimento> {

    // Regra 4: o mesmo veterinário não pode ter dois atendimentos AGENDADOS no mesmo horário.
    // Devolve apenas true ou false, o que basta para a regra.
    boolean existsByVeterinarioIdAndDataHoraAndStatus(
            Long veterinarioId, LocalDateTime dataHora, StatusAtendimento status
    );
}