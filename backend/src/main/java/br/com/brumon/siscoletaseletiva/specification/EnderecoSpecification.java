package br.com.brumon.siscoletaseletiva.specification;

import br.com.brumon.siscoletaseletiva.model.entity.Endereco;
import org.springframework.data.jpa.domain.Specification;

//Cada metodo abaixo é um filtro independente. Quando o parâmetro não foi enviado (ou veio em branco),
//ele devolve null e o Spring simplesmente ignora esse filtro ao combinar as Specifications.
public class EnderecoSpecification {

    private EnderecoSpecification() {
    }
    public static Specification<Endereco> comMorador(Integer idMorador) {
        return (root, query, criteriaBuilder) ->
                idMorador == null ? null : criteriaBuilder.equal(root.get("morador").get("id"), idMorador);
    }
    //CEP é comparado de forma exata.
    public static Specification<Endereco> comCep(String cep) {
        return (root, query, criteriaBuilder) ->
                (cep == null || cep.isBlank()) ? null : criteriaBuilder.equal(root.get("cep"), cep);
    }
    //esse metodo "contem" que sao chamados varias vezes ta la no final, serve para fazer o
    //ignorecase
    public static Specification<Endereco> comUf(String uf) {
        return contem("uf", uf);
    }
    public static Specification<Endereco> comMunicipio(String municipio) {
        return contem("municipio", municipio);
    }
    public static Specification<Endereco> comBairro(String bairro) {
        return contem("bairro", bairro);
    }
    public static Specification<Endereco> comLogradouro(String logradouro) {
        return contem("logradouro", logradouro);
    }

    //equivale ao antigo "ContainingIgnoreCase", busca parcial sem diferenciar maiúsculas de minúsculas.
    //os caracteres % e _ digitados pelo usuário são escapados para valerem como texto comum.
    private static Specification<Endereco> contem(String campo, String valor) {
        return (root, query, criteriaBuilder) -> {
            if (valor == null || valor.isBlank()) {
                return null;
            }
            String escapado = valor.toLowerCase()
                    .replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(campo)), "%" + escapado + "%", '\\');
        };
    }
}
