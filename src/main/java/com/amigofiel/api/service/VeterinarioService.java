package com.amigofiel.api.service;

import com.amigofiel.api.dto.request.VeterinarioRequest;
import com.amigofiel.api.dto.response.VeterinarioResponse;
import com.amigofiel.api.entity.Veterinario;
import com.amigofiel.api.exception.BusinessRuleException;
import com.amigofiel.api.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//aqui dentro que moram as regras de negócio da nossa aplicação
//@Service ja declara isso
@Service
public class VeterinarioService {

    //aqui é a injecao de dependencia
    //o spring pega o veterinarioRepsitory que conversa com o banco de dados e injeta(coloca) aqui
    //coloca aqui dentro do service sem precisar dar um new
    @Autowired
    private VeterinarioRepository veterinarioRepository;

    public VeterinarioResponse criar(VeterinarioRequest request) {
        // Regra de negócio: Valida se já existe veterinário com o mesmo CRMV (Erro 409)
        //Antes de salvar qualquer coisa, ele vai ao banco via repository
        // usando aquele metodo findByCrmv
        //Se ele achar (isPresent()) algum registro com o mesmo CRMV
        // ele interrompe tudo e dispara a nossa exceção personalizada
        if (veterinarioRepository.findByCrmv(request.getCrmv()).isPresent()) {
            throw new BusinessRuleException("Já existe um veterinário cadastrado com o CRMV: " + request.getCrmv());
        }

        // Criando a entidade com os dados que vieram da requisição
        //Como o Request (que veio da web) não pode ir direto para o banco de dados,
        // nós pegamos os dados dele e preenchemos uma nova Entity (Veterinario)
        Veterinario veterinario = new Veterinario();
        veterinario.setNome(request.getNome());
        veterinario.setCrmv(request.getCrmv());
        veterinario.setEspecialidade(request.getEspecialidade());

        //todo veterinario recem cadastrado nasce ativo true por regra de negocio
        veterinario.setAtivo(true);

        // Salvando no banco de dados através do repository
        Veterinario salvo = veterinarioRepository.save(veterinario);

        //O veterinarioRepository.save(veterinario)
        // joga os dados para o PostgreSQL (que gera o ID automático e preenche o objeto salvo

        // Convertendo a entidade salva para o DTO de resposta
        return new VeterinarioResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getCrmv(),
                salvo.getEspecialidade(),
                salvo.isAtivo()
        );
    }
}
