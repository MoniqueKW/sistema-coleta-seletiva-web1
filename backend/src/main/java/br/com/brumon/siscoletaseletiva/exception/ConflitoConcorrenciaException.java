package br.com.brumon.siscoletaseletiva.exception;

public class ConflitoConcorrenciaException extends RuntimeException {
    public ConflitoConcorrenciaException(String message) {
        super(message);
    }
}
