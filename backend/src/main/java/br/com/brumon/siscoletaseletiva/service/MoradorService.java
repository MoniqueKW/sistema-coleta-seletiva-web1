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
    public Page<MoradorResponseDTO> findAll(Pageable pageable) {
        Page<Morador> lista = rep.findAll(pageable);
        return lista.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public MoradorResponseDTO findById(Integer id) {
        Morador morador = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return converterParaDTO(morador);
    }
    @Transactional(readOnly = true)
    public Page<MoradorResponseDTO> findByNome(String nome, Pageable pageable) {
        Page<Morador> lista = rep.findByNomeContainingIgnoreCase(nome, pageable);
        return lista.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public MoradorResponseDTO findByEmail(String email) {
        Morador morador = rep.findByEmail(email).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return converterParaDTO(morador);
    }
    @Transactional(readOnly = true)
    public MoradorResponseDTO findByTelefone(String telefone) {
        Morador morador = rep.findByTelefone(telefone).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return converterParaDTO(morador);
    }
    @Transactional
    public MoradorResponseDTO salvarMorador(MoradorRequestDTO moradordto) {
        //ver em relaçao ao RN de "validar nomes certos"
        String emailPadrao = moradordto.email().trim().toLowerCase();
        if(rep.existsByEmail(emailPadrao)) {
            throw new RecursoDuplicadoException("Morador com email já cadastrado");
        }
        Morador morador = rep.save(Morador.builder()
                .nome(moradordto.nome())
                .email(emailPadrao)
                .telefone(moradordto.telefone())
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
            morador.setNome(moradordto.nome());
            morador.setEmail(emailPadrao);
            morador.setTelefone(moradordto.telefone());
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
