package br.com.brumon.siscoletaseletiva.DTO;

public record EnderecoRequestDTO(
Integer idMorador,
String logradouro,
String numero,
 String complemento,
String bairro,
String municipio,
String uf,
String cep
) {
}
