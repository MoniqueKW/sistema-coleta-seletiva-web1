package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.MoradorRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.MoradorResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Morador;
import br.com.brumon.siscoletaseletiva.Exception.RecursoDuplicadoException;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.Repository.MoradorRepository;
import br.com.brumon.siscoletaseletiva.Repository.SolicitacaoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoradorService {
    private final MoradorRepository rep;
    private final SolicitacaoRepository repSolicitacao;
    public MoradorService(MoradorRepository rep, SolicitacaoRepository repSolicitacao) {
        this.rep = rep;
        this.repSolicitacao = repSolicitacao;
    }
    @Transactional(readOnly = true)
    public List<MoradorResponseDTO> findAll() {
        List<Morador> lista = rep.findAll();
        return lista.stream()
                .map(morador -> new MoradorResponseDTO(
                    morador.getId(),
                    morador.getNome(),
                    morador.getEmail(),
                    morador.getTelefone()
                ))
                .toList();
    }
    @Transactional(readOnly = true)
    public MoradorResponseDTO findById(Integer id) {
        Morador morador = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getEmail(),
                morador.getTelefone()
        );
    }
    @Transactional(readOnly = true)
    public List<MoradorResponseDTO> findByNome(String nome) {
        List<Morador> lista = rep.findByNomeContainingIgnoreCase(nome);
        return lista.stream()
                .map(morador -> new MoradorResponseDTO(
                        morador.getId(),
                        morador.getNome(),
                        morador.getEmail(),
                        morador.getTelefone()
                ))
                .toList();
    }
    @Transactional(readOnly = true)
    public MoradorResponseDTO findByEmail(String email) {
        Morador morador = rep.findByEmail(email).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getEmail(),
                morador.getTelefone()
        );
    }
    @Transactional(readOnly = true)
    public MoradorResponseDTO findByTelefone(String telefone) {
        Morador morador = rep.findByTelefone(telefone).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getEmail(),
                morador.getTelefone()
        );
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
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getEmail(),
                morador.getTelefone()
        );
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
            return new MoradorResponseDTO(
                    moradorSalvo.getId(),
                    moradorSalvo.getNome(),
                    moradorSalvo.getEmail(),
                    moradorSalvo.getTelefone()
            );
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
}
