package br.com.brumon.siscoletaseletiva.exception;

import br.com.brumon.siscoletaseletiva.dto.response.ApiErroResponse;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiErroResponse> tratarNaoEncontrado(RecursoNaoEncontradoException exception) {
        return criarResposta(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", exception.getMessage(), Map.of());
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ApiErroResponse> tratarDuplicado(RecursoDuplicadoException exception) {
        return criarResposta(HttpStatus.CONFLICT, "DUPLICATE_RESOURCE", exception.getMessage(), Map.of());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ApiErroResponse> tratarRegraNegocio(RegraNegocioException exception) {
        return criarResposta(HttpStatus.CONFLICT, "BUSINESS_CONFLICT", exception.getMessage(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErroResponse> tratarArgumentoNaoValido(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            adicionarErro(fieldErrors, fieldError.getField(), fieldError.getDefaultMessage());
        }
        exception.getBindingResult().getGlobalErrors().forEach(error ->
                adicionarErro(fieldErrors, error.getObjectName(), error.getDefaultMessage()));
        return criarResposta(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Entrada inválida.", fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErroResponse> tratarViolacaoDeConstraint(ConstraintViolationException exception) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getConstraintViolations().forEach(violation ->
                adicionarErro(fieldErrors, violation.getPropertyPath().toString(), violation.getMessage()));
        return criarResposta(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Parâmetros inválidos.", fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErroResponse> tratarCorpoNaoLegivel(HttpMessageNotReadableException exception) {
        return criarResposta(
                HttpStatus.BAD_REQUEST,
                "MALFORMED_REQUEST",
                "JSON inválido ou incompatível com o formato esperado.",
                Map.of());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ApiErroResponse> tratarParametroInvalido(Exception exception) {
        String field = exception instanceof MethodArgumentTypeMismatchException mismatch
                ? mismatch.getName()
                : ((MissingServletRequestParameterException) exception).getParameterName();
        return criarResposta(
                HttpStatus.BAD_REQUEST,
                "INVALID_PARAMETER",
                "Um parâmetro da requisição está ausente ou possui formato inválido.",
                Map.of(field, "Verifique o valor informado."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErroResponse> tratarViolacaoDeIntegridade(DataIntegrityViolationException exception) {
        return criarResposta(
                HttpStatus.CONFLICT,
                "DATA_INTEGRITY_CONFLICT",
                "A operação conflita com os dados existentes ou viola uma regra de integridade.",
                Map.of());
    }

    @ExceptionHandler({
            ObjectOptimisticLockingFailureException.class,
            OptimisticLockException.class
    })
    public ResponseEntity<ApiErroResponse> tratarConflitoDeVersao(Exception exception) {
        return criarResposta(
                HttpStatus.CONFLICT,
                "CONCURRENT_MODIFICATION",
                "O registro foi alterado por outra operação. Recarregue os dados e tente novamente.",
                Map.of());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErroResponse> tratarStatusDaRequisicao(ResponseStatusException exception) {
        var status = exception.getStatusCode();
        String message = exception.getReason() != null ? exception.getReason() : "Requisição não aceita.";
        return ResponseEntity.status(status).body(
                new ApiErroResponse(status.value(), "REQUEST_ERROR", message, Map.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErroResponse> tratarErroInesperado(Exception exception) {
        logger.error("Erro inesperado ao processar a requisição", exception);
        return criarResposta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "Ocorreu um erro interno inesperado.",
                Map.of());
    }

    private ResponseEntity<ApiErroResponse> criarResposta(
            HttpStatus status,
            String code,
            String message,
            Map<String, String> fieldErrors) {
        return ResponseEntity.status(status)
                .body(new ApiErroResponse(status.value(), code, message, fieldErrors));
    }

    private void adicionarErro(Map<String, String> errors, String field, String message) {
        errors.merge(field, message, (existing, next) -> existing + "; " + next);
    }
}