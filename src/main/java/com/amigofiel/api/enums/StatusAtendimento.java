package com.amigofiel.api.enums;

public enum StatusAtendimento {

    //Enum que controla o ciclo de vida da situação de um atendimento.
    //Regra: Todo atendimento nasce automaticamente como AGENDADO e
    //só pode mudar para CONCLUIDO ou CANCELADO seguindo as regras de negócio

    AGENDADO,
    CONCLUIDO,
    CANCELADO
}
