package br.com.brumon.siscoletaseletiva.service;

import br.com.brumon.siscoletaseletiva.dto.request.MoradorRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.MoradorResponseDTO;
import br.com.brumon.siscoletaseletiva.model.entity.Morador;
import br.com.brumon.siscoletaseletiva.exception.RecursoDuplicadoException;
import br.com.brumon.siscoletaseletiva.exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.repository.MoradorRepository;
import br.com.brumon.siscoletaseletiva.repository.SolicitacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;


@Service
public class MoradorService {
    private final MoradorRepository rep;
    private final SolicitacaoRepository repSolicitacao;
    public MoradorService(MoradorRepository rep, SolicitacaoRepository repSolicitacao) {
        this.rep = rep;
        this.repSolicitacao = repSolicitacao;
    }
    @Transactional(readOnly = true)
    public Page<MoradorResponseDTO> listar(String nome, Pageable pageable) {
        boolean temNome = nome != null && !nome.isBlank();
        if (temNome) {
            Page<Morador> moradores = rep.findByNomeContainingIgnoreCase(nome, pageable);
            return moradores.map(this::converterParaDTO);
        }

        return rep.findAll(pageable).map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public MoradorResponseDTO findById(Integer id) {
        Morador morador = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return converterParaDTO(morador);
    }

    @Transactional(readOnly = true)
    public MoradorResponseDTO findByEmail(String email) {
        Morador morador = rep.findByEmail(email).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return converterParaDTO(morador);
    }
    @Transactional
    public MoradorResponseDTO salvarMorador(MoradorRequestDTO moradordto) {
        String emailPadrao = moradordto.email().trim().toLowerCase();
        if(rep.existsByEmail(emailPadrao)) {
            throw new RecursoDuplicadoException("Morador com email já cadastrado");
        }
        String telefoneBruto = moradordto.telefone();
        String telefoneLimpo = null;
        if (telefoneBruto != null && !telefoneBruto.trim().isEmpty()) {
            telefoneLimpo = telefoneBruto.replaceAll("[()\\s+\\-]", "");
            if (telefoneLimpo.length() == 10 || telefoneLimpo.length() == 11) {
                telefoneLimpo = "55" + telefoneLimpo;
            }
        }
        Morador morador = rep.save(Morador.builder()
                .nome(moradordto.nome())
                .email(emailPadrao)
                .telefone(telefoneLimpo)
                .build());
        return converterParaDTO(morador);
    }
    @Transactional
        public MoradorResponseDTO atualizarMorador(MoradorRequestDTO moradordto, Integer id) {
            Morador morador = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
            String emailPadrao = moradordto.email().trim().toLowerCase();
            if (!morador.getEmail().equals(emailPadrao) && rep.existsByEmail(emailPadrao)) {
                throw new RecursoDuplicadoException("Já existe um morador cadastrado com este e-mail.");
            }
        String telefoneBruto = moradordto.telefone();
        String telefoneLimpo = null;
        if (telefoneBruto != null && !telefoneBruto.trim().isEmpty()) {
            telefoneLimpo = telefoneBruto.replaceAll("[()\\s+\\-]", "");
            if (telefoneLimpo.length() == 10 || telefoneLimpo.length() == 11) {
                telefoneLimpo = "55" + telefoneLimpo;
            }
        }
            morador.setNome(moradordto.nome());
            morador.setEmail(emailPadrao);
            morador.setTelefone(telefoneLimpo);
            Morador moradorSalvo = rep.save(morador);
            return converterParaDTO(moradorSalvo);
    }
    //so deleta se nao tiver nenhuma solicitacao vinculada
    @Transactional
    public void deleteById(Integer idMorador) {
        Morador morador = rep.findById(idMorador)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Morador não encontrado."
                ));
        if (repSolicitacao.existsByMorador_Id(idMorador)) {
            throw new RegraNegocioException(
                    "Não é possível excluir este morador, pois ele possui solicitações vinculadas."
            );
        }
        rep.delete(morador);
    }

    public MoradorResponseDTO converterParaDTO(Morador morador) {
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getEmail(),
                morador.getTelefone()
        );
    }

}
