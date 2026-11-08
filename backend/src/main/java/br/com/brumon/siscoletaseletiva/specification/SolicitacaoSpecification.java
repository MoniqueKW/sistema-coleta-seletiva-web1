package br.com.brumon.siscoletaseletiva.specification;

import br.com.brumon.siscoletaseletiva.model.entity.Solicitacao;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

//Cada metodo abaixo é um filtro independente. Quando o parâmetro não foi enviado,
//ele devolve null e o Spring simplesmente ignora esse filtro ao combinar as Specifications.
public class SolicitacaoSpecification {
    private SolicitacaoSpecification() {
    }

    public static Specification<Solicitacao> comMorador(Integer idMorador) {
        return (root, query, criteriaBuilder) ->
                idMorador == null ? null : criteriaBuilder.equal(root.get("morador").get("id"), idMorador);
    }

    public static Specification<Solicitacao> comPontoColeta(Integer idPonto) {
        return (root, query, criteriaBuilder) ->
                idPonto == null ? null : criteriaBuilder.equal(root.get("pontoColeta").get("id"), idPonto);
    }

    public static Specification<Solicitacao> comSituacao(SituacaoSolicitacao situacao) {
        return (root, query, criteriaBuilder) ->
                situacao == null ? null : criteriaBuilder.equal(root.get("situacao"), situacao);
    }

    //O período só é aplicado quando as duas datas são enviadas (mesmo comportamento de antes).
    public static Specification<Solicitacao> criadaEntre(OffsetDateTime dataInicio, OffsetDateTime dataFim) {
        return (root, query, criteriaBuilder) ->
                (dataInicio == null || dataFim == null) ? null : criteriaBuilder.between(root.get("dataCriacao"), dataInicio, dataFim);
    }
}
