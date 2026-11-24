package com.amigofiel.api.service;

import com.amigofiel.api.dto.filter.VeterinarioFilter;
import com.amigofiel.api.dto.request.VeterinarioRequest;
import com.amigofiel.api.dto.response.VeterinarioResponse;
import com.amigofiel.api.entity.Veterinario;
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


    // 1. CADASTRAR (POST)
    // @Transactional garante que se algo der errado no meio do processo,
    // o banco desfaz tudo (rollback) para não salvar dados incompletos.
    @Transactional
    public VeterinarioResponse criar(VeterinarioRequest request) {

        // REGRA DE NEGÓCIO: Verifica se já existe um veterinário cadastrado com este CRMV.
        // Se retornar true, bloqueia a operação e dispara uma exceção de regra (Erro 400/409).
        if (veterinarioRepository.existsByCrmv(request.getCrmv())) {
            throw new BusinessRuleException("Já existe um veterinário cadastrado com este CRMV.");
        }

        // Como o banco só entende a Entidade (e não o DTO), criamos um objeto Veterinario
        // e transferimos os dados limpos que vieram do request do usuário
        Veterinario veterinario = new Veterinario();
        veterinario.setNome(request.getNome());
        veterinario.setCrmv(request.getCrmv());
        veterinario.setEspecialidade(request.getEspecialidade());

        // Regra padrão do negócio: todo veterinario recem cadastrado nasce ativo no sistema
        veterinario.setAtivo(true);

        // Manda o repositório salvar o objeto no banco PostgreSQL.
        // O metodo save() devolve a entidade já com o ID gerado pelo banco
        Veterinario salvo = veterinarioRepository.save(veterinario);

        // Converte a entidade salva em um DTO de resposta (Response) para devolver ao Controller.
        return new VeterinarioResponse(salvo);
    }


    // 2. LISTAR COM PAGINAÇÃO (GET)
    public Page<VeterinarioResponse> listar(VeterinarioFilter filter, Pageable pageable) {

        // Busca no banco apenas a "fatia" (página) solicitada, evitando sobrecarregar a memória.
        // O map() percorre cada entidade da página e a transforma em VeterinarioResponse
        return veterinarioRepository.findAll(pageable)
                .map(VeterinarioResponse::new);
    }


    // 3. BUSCAR POR ID - Retornando DTO (GET /{id})
    // Metodo público chamado pelo Controller para entregar o JSON formatado ao cliente.
    public VeterinarioResponse buscarPorId(Long id) {
        // Reutiliza o metodo auxiliar abaixo para garantir que o registro existe
        Veterinario veterinario = buscarEntidadePorId(id);

        // Converte a entidade encontrada em Response e a devolve
        return new VeterinarioResponse(veterinario);
    }


    // METODO AUXILIAR INTERNO - Busca a Entidade Pura
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

        // Atualiza os dados permitidos com as novas informações enviadas no request.
        veterinario.setNome(request.getNome());
        veterinario.setCrmv(request.getCrmv());
        veterinario.setEspecialidade(request.getEspecialidade());

        // Como o objeto já possui um id preenchido, o JPA entende que deve atualizar
        // o registro existente (UPDATE) em vez de criar um novo.
        Veterinario atualizado = veterinarioRepository.save(veterinario);

        // Retorna o DTO com os dados atualizados para o Controller.
        return new VeterinarioResponse(atualizado);
    }

    // Metodo para excluir o veterinário pelo ID
    public void excluir(Long id) {
        // Verifica se o veterinário existe antes de excluir (se não existir, já retorna 404 automaticamente pelo buscarPorId)
        buscarPorId(id);

        // Executa a exclusão no banco de dados através do Repository
        veterinarioRepository.deleteById(id);
    }
}