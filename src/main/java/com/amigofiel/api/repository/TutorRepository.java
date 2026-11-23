package com.amigofiel.api.repository;

// O TutorRepository é a interface que faz a ponte entre a nossa Entidade e o Banco de Dados.
// Ao estender o JpaRepository, o Spring gera automaticamente os comandos para salvar,
// buscar por ID, listar todos os tutores e deletar registros, poupando muito código SQL.

import com.amigofiel.api.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//aqui falamos ao spring que esta interface é um repositorio responsavel por gerenciar os dados do tutor

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long>{
    //herdando de Jpa Repository, ja ganha metodos prontos como:
    //salvar (save)
    //buscar por ID (findById)
    //listar todos (findAll)
    //Deletar (delete)


    //aqui verifica o sistema se já existe um tutor
    // cadastrado com aquele mesmo e-mail antes de salvar.
    boolean existsByEmail(String email);
}

