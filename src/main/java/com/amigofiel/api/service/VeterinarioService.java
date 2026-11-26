package com.amigofiel.api.service;

import com.amigofiel.api.dto.filter.VeterinarioFilter;
import com.amigofiel.api.dto.request.VeterinarioRequest;
import com.amigofiel.api.dto.response.VeterinarioResponse;
import com.amigofiel.api.entity.Veterinario;
import com.amigofiel.api.enums.Especialidade;
import com.amigofiel.api.exception.BusinessRuleException;
import com.amigofiel.api.exception.ResourceNotFoundException;
import com.amigofiel.api.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Service avisa o Spring Boot que esta classe é um componente de Serviço
// É aqui que a "cozinha" funciona, processando as regras de negócio antes de tocar no banco.
@Service
public class VeterinarioService {

    // Injeta a "dispensa" (Repository). É através dele que conversamos com o banco de dados.
    @Autowired
    private VeterinarioRepository veterinarioRepository;


    // CADASTRAR (POST)
    // @Transactional garante que se algo der errado no meio do processo,
    // o banco desfaz tudo (rollback) para não salvar dados incompletos.
    @Transactional
    public VeterinarioResponse criar(VeterinarioRequest request) {

        // REGRA DE NEGÓCIO: Verifica se já existe um veterinário cadastrado com este CRMV.
        // Se retornar true, bloqueia a operação e dispara uma exceção de regra (Erro 409).
        if (veterinarioRepository.existsByCrmv(request.getCrmv())) {
            throw new BusinessRuleException("Já existe um veterinário cadastrado com este CRMV.");
        }

        // Como o banco só entende a Entidade (e não o DTO), criamos um objeto Veterinario
        // e transferimos os dados limpos que vieram do request do usuário
        Veterinario veterinario = new Veterinario();
        veterinario.setNome(request.getNome());
        veterinario.setCrmv(request.getCrmv());
        veterinario.setEspecialidade(request.getEspecialidade());

        // Regra padrão do negócio: todo veterinário recém cadastrado nasce ativo no sistema
        veterinario.setAtivo(true);

        // Manda o repositório salvar o objeto no banco PostgreSQL.
        // O método save() devolve a entidade já com o ID gerado pelo banco
        Veterinario salvo = veterinarioRepository.save(veterinario);

        // Converte a entidade salva em um DTO de resposta (Response) para devolver ao Controller.
        return new VeterinarioResponse(salvo);
    }


    // LISTAR COM FILTROS E PAGINAÇÃO (GET)
    public Page<VeterinarioResponse> listar(VeterinarioFilter filter, Pageable pageable) {
        Page<Veterinario> pagina;

        // Extrai os valores do filtro (protegendo contra null caso o filter venha vazio)
        String nome = (filter != null) ? filter.getNome() : null;
        Especialidade especialidade = (filter != null) ? filter.getEspecialidade() : null;

        boolean temNome = (nome != null && !nome.isBlank());
        boolean temEspecialidade = (especialidade != null);

        // Escolhe a busca de acordo com os filtros informados:
        // - nome e especialidade: busca pelos dois juntos.
        // - só nome: busca contendo o texto (ignorando maiúsculas e minúsculas).
        // - só especialidade: busca exata.
        // - nenhum filtro: traz a listagem completa (findAll).
        if (temNome && temEspecialidade) {
            pagina = veterinarioRepository.findByNomeContainingIgnoreCaseAndEspecialidade(nome, especialidade, pageable);
        } else if (temNome) {
            pagina = veterinarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (temEspecialidade) {
            pagina = veterinarioRepository.findByEspecialidade(especialidade, pageable);
        } else {
            pagina = veterinarioRepository.findAll(pageable);
        }

        // Converte a página de Entidades para DTO de resposta
        return pagina.map(VeterinarioResponse::new);
    }


    // BUSCAR POR ID - Retornando DTO (GET /{id})
    // Método público chamado pelo Controller para entregar o JSON formatado ao cliente.
    public VeterinarioResponse buscarPorId(Long id) {
        // Reutiliza o método auxiliar abaixo para garantir que o registro existe
        Veterinario veterinario = buscarEntidadePorId(id);

        // Converte a entidade encontrada em Response e a devolve
        return new VeterinarioResponse(veterinario);
    }


    // MÉTODO AUXILIAR INTERNO - Busca a Entidade Pura
    // Usado internamente pelo Service (em atualizações ou validações).
    // O findById retorna um Optional. Se achar, entrega a entidade; se não,
    // dispara a exceção ResourceNotFoundException que resulta em um Erro 404
    public Veterinario buscarEntidadePorId(Long id) {
        return veterinarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com o ID: " + id));
    }


    // ATUALIZAR (PUT /{id})
    @Transactional
    public VeterinarioResponse atualizar(Long id, VeterinarioRequest request) {

        // Primeiro, garante que o veterinário realmente existe no banco (lança 404 se não achar).
        Veterinario veterinario = buscarEntidadePorId(id);

        // REGRA DE NEGÓCIO: o CRMV não pode repetir (Erro 409).
        // Só conferimos no banco se o CRMV enviado é diferente do atual; se fosse o mesmo,
        // o próprio veterinário seria acusado de duplicidade.
        if (!veterinario.getCrmv().equals(request.getCrmv())
                && veterinarioRepository.existsByCrmv(request.getCrmv())) {
            throw new BusinessRuleException("Já existe um veterinário cadastrado com este CRMV.");
        }

        // Atualiza os dados permitidos com as novas informações enviadas no request.
        veterinario.setNome(request.getNome());
        veterinario.setCrmv(request.getCrmv());
        veterinario.setEspecialidade(request.getEspecialidade());

        // O campo ativo é opcional no JSON: se não vier, mantém o valor que já estava no banco.
        // É por aqui que se inativa ou reativa um veterinário (ele nunca é apagado).
        if (request.getAtivo() != null) {
            veterinario.setAtivo(request.getAtivo());
        }

        // Como o objeto já possui um id preenchido, o JPA entende que deve atualizar
        // o registro existente (UPDATE) em vez de criar um novo.
        Veterinario atualizado = veterinarioRepository.save(veterinario);

        // Retorna o DTO com os dados atualizados para o Controller.
        return new VeterinarioResponse(atualizado);
    }
}