package br.com.brumon.siscoletaseletiva.dto.response;

import java.util.Map;

public record ApiErroResponse(
        int status,
        String code,
        String mensagem,
        Map<String, String> fieldErrors
) {
}
