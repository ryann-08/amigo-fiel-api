package com.amigofiel.api.repository;

// O TutorRepository é a interface que faz a ponte entre a nossa Entidade e o Banco de Dados.
// Ao estender o JpaRepository, o Spring gera automaticamente os comandos para salvar,
// buscar por ID, listar todos os tutores e deletar registros, poupando muito código SQL.
import com.amigofiel.api.entity.Tutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// o @Repository Avisa o Spring Boot que esta interface gerencia dados.
@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    // Valida se já existe um tutor cadastrado com este e-mail (Regra de Negócio)
    boolean existsByEmail(String email);

    // Consulta personalizada para os filtros opcionais de listagem (nome e cidade) com paginação
    //primeira linha seleciona os tutores da tabela
    //(:nome IS NULL OR... é o filtro de nome, se a pessoa nao passou o nome fica null
    //com isso é ignorado nao vai fazer, mas se colocar o sistema continua para validacao seguinte
    //com a cidade mesma coisa, so que de forma exata, se a cidade for informada
    //ela precisa bater exatamente com o mesmo valor no banco
    @Query("SELECT t FROM Tutor t WHERE " +
            "(:nome IS NULL OR LOWER(t.nome) LIKE LOWER(CONCAT('%', cast(:nome as string), '%'))) AND " +
            "(:cidade IS NULL OR t.cidade = :cidade)")
    Page<Tutor> filtrarTutores(@Param("nome") String nome,
                               @Param("cidade") String cidade,
                               Pageable pageable);
}