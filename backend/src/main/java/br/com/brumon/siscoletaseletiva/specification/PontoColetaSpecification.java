package br.com.brumon.siscoletaseletiva.specification;

import br.com.brumon.siscoletaseletiva.model.entity.PontoColeta;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoPonto;
import org.springframework.data.jpa.domain.Specification;

//Cada metodo abaixo é um filtro independente. Quando o parâmetro não foi enviado (ou veio em branco),
//ele devolve null e o Spring simplesmente ignora esse filtro ao combinar as Specifications.
public class PontoColetaSpecification {
    private PontoColetaSpecification() {
    }

    //Busca parcial, sem diferenciar maiúsculas de minúsculas.
    public static Specification<PontoColeta> comNome(String nome) {
        return contem("nome", nome);
    }

    public static Specification<PontoColeta> comSituacao(SituacaoPonto situacao) {
        return (root, query, criteriaBuilder) ->
                situacao == null ? null : criteriaBuilder.equal(root.get("situacao"), situacao);
    }

    public static Specification<PontoColeta> comRecolhimento(Boolean recolhimento) {
        return (root, query, criteriaBuilder) ->
                recolhimento == null ? null : criteriaBuilder.equal(root.get("recolhimentoDomiciliar"), recolhimento);
    }

    //Município exato, sem diferenciar maiúsculas de minúsculas.
    public static Specification<PontoColeta> comMunicipio(String municipio) {
        return (root, query, criteriaBuilder) ->
                (municipio == null || municipio.isBlank()) ? null
                        : criteriaBuilder.equal(criteriaBuilder.lower(root.get("municipio")), municipio.toLowerCase());
    }

    //Bairro exato (mesmo comportamento de antes).
    public static Specification<PontoColeta> comBairro(String bairro) {
        return (root, query, criteriaBuilder) ->
                (bairro == null || bairro.isBlank()) ? null : criteriaBuilder.equal(root.get("bairro"), bairro);
    }

    //equivale ao antigo "ContainingIgnoreCase", busca parcial sem diferenciar maiúsculas de minúsculas.
    //os caracteres % e _ digitados pelo usuário são escapados para valerem como texto comum.
    public static Specification<PontoColeta> contem(String campo, String valor) {
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
