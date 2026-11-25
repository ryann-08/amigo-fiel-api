package com.amigofiel.api.config;

// Configuração global para permitir que parâmetros de URL (como filtros de Enums)
// sejam aceitos tanto em letras maiúsculas quanto minúsculas (case-insensitive).
//em poucas palavras essa configuracao faz aceitar tanto minuscula quanto maiuscula
//obs: funciona apenas para todos os enuns
//como (especialidade) (statusAtendimento) (tipoAtendimento)
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        ApplicationConversionService.configure(registry);
    }
}