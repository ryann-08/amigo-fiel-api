package com.amigofiel.api.repository;

import com.amigofiel.api.entity.Veterinario;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.amigofiel.api.enums.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

//  Diz ao Spring Boot que esta interface é um componente de repositório (acesso a dados)
@Repository

//  Herda de JpaRepository, passando a Entidade (Veterinario) e o tipo da chave primária (Long)
//aqui é o famoso jpa, faz todos aqueles metodos salvar buscar deletar etc, usando ID do tipo long
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    // aqui Metodo customizado de busca por CRMV
    //tem uma regra de negocio importante que nao pode ter dois crvm, entao essa classe
    //vai no banco para ver se ja tem um crvm salvo no banco
    // O Spring Data JPA lê o nome "findByCrmv" e gera a consulta SQL automaticamente nos bastidores
    Optional<Veterinario> findByCrmv(String crmv);

    boolean existsByCrmv(String crmv);

    // Importações necessárias que você precisará garantir no topo do arquivo:
    // import org.springframework.data.domain.Page;
    // import org.springframework.data.domain.Pageable;
    // import com.amigofiel.api.entity.Especialidade;

    // 1. Busca por nome (contendo o texto, ignorando maiúsculas/minúsculas) com paginação
    Page<Veterinario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    // 2. Busca por especialidade exata com paginação
    Page<Veterinario> findByEspecialidade(Especialidade especialidade, Pageable pageable);

    // 3. Busca combinando nome E especialidade ao mesmo tempo
    Page<Veterinario> findByNomeContainingIgnoreCaseAndEspecialidade(String nome, Especialidade especialidade, Pageable pageable);
}