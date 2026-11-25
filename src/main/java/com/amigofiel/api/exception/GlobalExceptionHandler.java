package com.amigofiel.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Esta classe intercepta qualquer erro que acontece na API antes que ele chegue ao cliente e diz
// ex: se der email duplicado responda 409... assim por diante
// @RestControllerAdvice: Transforma esta classe em um ouvinte global de exceções para todos os Controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. TRATA ERROS DE REGRA DE NEGÓCIO (Ex: E-mail duplicado, CRMV repetido, inativo)
    // Converte a nossa BusinessRuleException em um belo JSON com status 409 Conflict
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Object> handleBusinessRule(BusinessRuleException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value()); // Código 409
        body.put("error", "Conflict");
        body.put("message", ex.getMessage()); // Mensagem que definimos no Service

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 2. TRATA RECURSOS NÃO ENCONTRADOS (Ex: ID que não existe no banco)
    // Retorna o status 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value()); // Código 404
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // 3. TRATA ERROS DE VALIDAÇÃO DE CAMPOS (Bean Validation: @NotBlank, @Email, @Size)
    // Retorna o status 400 Bad Request quando o cliente envia dados inválidos ou incompletos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Pega cada campo que falhou na validação e guarda a mensagem de erro correspondente
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value()); // Código 400
        body.put("error", "Bad Request");
        // Adicionando uma mensagem descritiva em texto para o cliente saber o que houve
        body.put("message", "Erro de validação nos campos enviados. Verifique os dados.");
        body.put("messages", errors); // Detalhes de qual campo falhou

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 4. TRATA ERROS DE DADOS ILEGÍVEIS OU ENUMS INVÁLIDOS (Ex: Mandar uma especialidade que não existe)
    // Intercepta quando o Spring não consegue converter o JSON enviado (como um Enum incorreto ou erro de sintaxe)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        // Cria um mapa (HashMap) para montar a estrutura do JSON de erro que será enviado ao cliente
        Map<String, Object> body = new HashMap<>();

        // Registra o exato momento (data e hora) em que o erro ocorreu
        body.put("timestamp", LocalDateTime.now());

        // Define o código de status HTTP correspondente (400 Bad Request)
        body.put("status", HttpStatus.BAD_REQUEST.value());

        // Define o nome padrão do tipo de erro
        body.put("error", "Bad Request");

        // Insere uma mensagem descritiva para orientar quem está testando ou usando a API
        body.put("message", "Erro no formato dos dados enviados. Verifique se os valores de campos especiais ou enums (como especialidade, tipo ou status) estão corretos.");

        // Monta e devolve a resposta HTTP com status 400 e o JSON estruturado no corpo
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}