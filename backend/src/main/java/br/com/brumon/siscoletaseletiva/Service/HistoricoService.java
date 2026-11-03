package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Response.HistoricoResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Historico;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.HistoricoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRep;

    public HistoricoService(HistoricoRepository historicoRep) {
        this.historicoRep = historicoRep;
    }

    public List<HistoricoResponseDTO> findAll() {
        List<Historico> lista = historicoRep.findAll();
        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum histórico encontrado");
        }
        return lista.stream()
                .map(historico -> new HistoricoResponseDTO(
                        historico.getId(),
                        historico.getSolicitacao().getId(),
                        historico.getSituacaoAnterior(),
                        historico.getSituacaoNova(),
                        historico.getData(),
                        historico.getResponsavel(),
                        historico.getMotivo(),
                        historico.getInfoTentativa()
                ))
                .toList();
    }

    public HistoricoResponseDTO findById(Integer id) {
        Historico historico = historicoRep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Histórico não encontrado"));
        return new HistoricoResponseDTO(
                historico.getId(),
                historico.getSolicitacao().getId(),
                historico.getSituacaoAnterior(),
                historico.getSituacaoNova(),
                historico.getData(),
                historico.getResponsavel(),
                historico.getMotivo(),
                historico.getInfoTentativa()
        );
    }

    public Page<HistoricoResponseDTO> findBySolicitacao(Integer idSolicitacao, Pageable pageable) {
        Page<Historico> pagina = historicoRep.findBySolicitacao_IdOrderByDataAsc(idSolicitacao, pageable);
        if (pagina.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum histórico encontrado para esta solicitação");
        }
        return pagina.map(historico -> new HistoricoResponseDTO(
                historico.getId(),
                historico.getSolicitacao().getId(),
                historico.getSituacaoAnterior(),
                historico.getSituacaoNova(),
                historico.getData(),
                historico.getResponsavel(),
                historico.getMotivo(),
                historico.getInfoTentativa()
        ));
    }
}