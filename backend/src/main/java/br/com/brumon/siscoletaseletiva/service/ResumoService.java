package br.com.brumon.siscoletaseletiva.service;

import br.com.brumon.siscoletaseletiva.dto.response.QuantidadeRecolhidaDTO;
import br.com.brumon.siscoletaseletiva.dto.response.ResumoSituacaoDTO;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;
import br.com.brumon.siscoletaseletiva.repository.ItemSolicitacaoRepository;
import br.com.brumon.siscoletaseletiva.repository.SolicitacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ResumoService {
    //Quando o período não é informado, usa um intervalo que cobre qualquer data.
    private static final OffsetDateTime INICIO_PADRAO = OffsetDateTime.parse("1900-01-01T00:00:00Z");
    private static final OffsetDateTime FIM_PADRAO = OffsetDateTime.parse("9999-12-31T23:59:59Z");
    private final SolicitacaoRepository repSolicitacao;
    private final ItemSolicitacaoRepository repItem;

    public ResumoService(SolicitacaoRepository repSolicitacao, ItemSolicitacaoRepository repItem) {
        this.repSolicitacao = repSolicitacao;
        this.repItem = repItem;
    }

    //Número de solicitações por situação, pela data de criação. Todas as situações aparecem, mesmo com total zero.
    @Transactional(readOnly = true)
    public List<ResumoSituacaoDTO> resumoPorSituacao(OffsetDateTime dataInicio, OffsetDateTime dataFim) {
        validarPeriodo(dataInicio, dataFim);
        boolean temPeriodo = dataInicio != null && dataFim != null;
        Map<SituacaoSolicitacao, Long> totais = new EnumMap<>(SituacaoSolicitacao.class);
        for (SituacaoSolicitacao situacao : SituacaoSolicitacao.values()) {
            //o L força o numero a ser interpretado como um Long e não um Integer
            totais.put(situacao, 0L);
        }
        for (ResumoSituacaoDTO linha : repSolicitacao.contarPorSituacao(
                temPeriodo ? dataInicio : INICIO_PADRAO, temPeriodo ? dataFim : FIM_PADRAO)) {
            totais.put(linha.situacao(), linha.total());
        }
        return totais.entrySet().stream()
                .map(entrada -> new ResumoSituacaoDTO(entrada.getKey(), entrada.getValue()))
                .toList();
    }

    //Quantidade efetivamente recolhida por material e unidade, só de atendimentos CONCLUIDA,
    //pela data de conclusão.
    @Transactional(readOnly = true)
    public List<QuantidadeRecolhidaDTO> quantidadesRecolhidas(OffsetDateTime dataInicio, OffsetDateTime dataFim) {
        validarPeriodo(dataInicio, dataFim);
        boolean temPeriodo = dataInicio != null && dataFim != null;
        return repItem.somarQuantidadeEfetiva(SituacaoSolicitacao.CONCLUIDA,
                temPeriodo ? dataInicio : INICIO_PADRAO, temPeriodo ? dataFim : FIM_PADRAO);
    }
    //dava pra usar la em cima em resumoPorSituacao tb, mas ia ficar meio poluido
    private void validarPeriodo(OffsetDateTime dataInicio, OffsetDateTime dataFim) {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A data inicial não pode ser posterior à data final.");
        }
    }
}