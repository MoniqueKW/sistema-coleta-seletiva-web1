package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.AgendamentoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.ConclusaoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.SolicitacaoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.SolicitacaoUpdateRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.ItemSolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.dto.response.SolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;
import br.com.brumon.siscoletaseletiva.service.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {
    private final SolicitacaoService solicitacaoServ;
    public SolicitacaoController(SolicitacaoService solicitacaoServ) {
        this.solicitacaoServ = solicitacaoServ;
    }

    @GetMapping
    public ResponseEntity<Page<SolicitacaoResponseDTO>> findAll(
            @RequestParam(required = false) Integer idMorador,
            @RequestParam(required = false) Integer idPonto,
            @RequestParam(required = false) SituacaoSolicitacao situacao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            Pageable pageable) {

        if (idMorador != null && idPonto != null) {
            return ResponseEntity.ok(solicitacaoServ.findByPontoColetaIdAndMoradorId(idPonto, idMorador, pageable));
        }
        if (idPonto != null && situacao != null && dataInicio != null && dataFim != null) {
            return ResponseEntity.ok(solicitacaoServ.findByPontoColetaIdAndSituacaoAndDataCriacaoBetween(idPonto, situacao, dataInicio, dataFim, pageable));
        }
        if (idPonto != null && dataInicio != null && dataFim != null) {
            return ResponseEntity.ok(solicitacaoServ.findByPontoColetaIdAndDataCriacaoBetween(idPonto, dataInicio, dataFim, pageable));
        }
        if (idPonto != null && situacao != null) {
            return ResponseEntity.ok(solicitacaoServ.findByPontoColetaIdAndSituacao(idPonto, situacao, pageable));
        }
        if (idPonto != null) {
            return ResponseEntity.ok(solicitacaoServ.findByPontoColetaId(idPonto, pageable));
        }
        if (idMorador != null && situacao != null && dataInicio != null && dataFim != null) {
            return ResponseEntity.ok(solicitacaoServ.findByMoradorIdAndSituacaoAndDataCriacaoBetween(idMorador, situacao, dataInicio, dataFim, pageable));
        }
        if (idMorador != null && dataInicio != null && dataFim != null) {
            return ResponseEntity.ok(solicitacaoServ.findByMoradorIdAndDataCriacaoBetween(idMorador, dataInicio, dataFim, pageable));
        }
        if (idMorador != null && situacao != null) {
            return ResponseEntity.ok(solicitacaoServ.findByMoradorIdAndSituacao(idMorador, situacao, pageable));
        }
        if (idMorador != null) {
            return ResponseEntity.ok(solicitacaoServ.findByMoradorId(idMorador, pageable));
        }
        if (situacao != null) {
            return ResponseEntity.ok(solicitacaoServ.findBySituacao(situacao, pageable));
        }
        if (dataInicio != null && dataFim != null) {
            return ResponseEntity.ok(solicitacaoServ.findByDataCriacaoBetween(dataInicio, dataFim, pageable));
        }

        return ResponseEntity.ok(solicitacaoServ.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> findById(@PathVariable Integer id) {
        SolicitacaoResponseDTO solicitacao = solicitacaoServ.findById(id);
        return ResponseEntity.ok(solicitacao);
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemSolicitacaoResponseDTO>> findItensBySolicitacaoId(@PathVariable Integer id) {
        List<ItemSolicitacaoResponseDTO> itens = solicitacaoServ.findItensBySolicitacaoId(id);
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<SolicitacaoResponseDTO> findByProtocolo(@PathVariable String protocolo) {
        SolicitacaoResponseDTO solicitacao = solicitacaoServ.findByProtocoloUnico(protocolo);
        return ResponseEntity.ok(solicitacao);
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criarSolicitacao(
            @RequestBody @Valid SolicitacaoRequestDTO dto,
            @RequestParam(required = false) Integer idOperador) {
        SolicitacaoResponseDTO solicitacaoCriada = solicitacaoServ.salvarSolicitacao(dto, idOperador);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacaoCriada);
    }

    //esse recebe um dto so pra ele, pois nem os ids e a solicitacao podem ser alterados,
    //ent fiz um dto so pra update da solicitacao, sem esses dados
    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> atualizarSolicitacao(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer idOperador,
            @RequestBody @Valid SolicitacaoUpdateRequestDTO dto) {
        SolicitacaoResponseDTO solicitacaoAtualizada = solicitacaoServ.atualizarSolicitacao(id, dto, idOperador);
        return ResponseEntity.ok(solicitacaoAtualizada);
    }

    @PatchMapping("/{id}/agendar")
    public ResponseEntity<SolicitacaoResponseDTO> agendarSolicitacao(
            @PathVariable Integer id,
            @RequestParam Integer idOperador,
            @RequestBody @Valid AgendamentoRequestDTO dto) {
        SolicitacaoResponseDTO solicitacaoAgendada = solicitacaoServ.agendarSolicitacao(id, dto, idOperador);
        return ResponseEntity.ok(solicitacaoAgendada);
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<SolicitacaoResponseDTO> recusarSolicitacao(
            @PathVariable Integer id,
            @RequestParam Integer idOperador,
            @RequestParam String motivo) {
        SolicitacaoResponseDTO solicitacaoRecusada = solicitacaoServ.recusarSolicitacao(id, motivo, idOperador);
        return ResponseEntity.ok(solicitacaoRecusada);
    }

    @PatchMapping("/{id}/iniciar-recolhimento")
    public ResponseEntity<SolicitacaoResponseDTO> iniciarRecolhimento(
            @PathVariable Integer id,
            @RequestParam Integer idOperador) {
        SolicitacaoResponseDTO solicitacao = solicitacaoServ.iniciarRecolhimento(id, idOperador);
        return ResponseEntity.ok(solicitacao);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<SolicitacaoResponseDTO> cancelarSolicitacao(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer idOperador,
            @RequestParam String motivo) {
        SolicitacaoResponseDTO solicitacaoCancelada = solicitacaoServ.cancelarSolicitacao(id, motivo, idOperador);
        return ResponseEntity.ok(solicitacaoCancelada);
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<SolicitacaoResponseDTO> concluirSolicitacao(
            @PathVariable Integer id,
            @RequestParam Integer idOperador,
            @RequestBody @Valid ConclusaoRequestDTO dto) {
        SolicitacaoResponseDTO solicitacaoConcluida = solicitacaoServ.concluirSolicitacao(id, dto, idOperador);
        return ResponseEntity.ok(solicitacaoConcluida);
    }

    @PatchMapping("/{id}/reagendar")
    public ResponseEntity<SolicitacaoResponseDTO> reagendarSolicitacao(
            @PathVariable Integer id,
            @RequestParam Integer idOperador,
            @RequestParam String motivo,
            @RequestParam String infoTentativa,
            @RequestBody @Valid AgendamentoRequestDTO dto) {
        SolicitacaoResponseDTO solicitacaoReagendada = solicitacaoServ.reagendarSolicitacao(id, motivo, infoTentativa, dto, idOperador);
        return ResponseEntity.ok(solicitacaoReagendada);
    }
}
