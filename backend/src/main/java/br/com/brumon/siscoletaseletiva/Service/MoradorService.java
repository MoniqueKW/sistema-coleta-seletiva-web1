package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.MoradorRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.MoradorResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Morador;
import br.com.brumon.siscoletaseletiva.Exception.RecursoDuplicadoException;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.Repository.MoradorRepository;
import br.com.brumon.siscoletaseletiva.Repository.SolicitacaoRepository;
import jakarta.transaction.Transactional;
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

    public List<MoradorResponseDTO> findAll() {
        List<Morador> lista = rep.findAll();
        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum morador cadastrado");
        }
        return lista.stream()
                .map(morador -> new MoradorResponseDTO(
                    morador.getId(),
                    morador.getNome(),
                    morador.getEmail(),
                    morador.getTelefone()
                ))
                .toList();
    }

    public MoradorResponseDTO findById(Integer id) {
        Morador morador = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getEmail(),
                morador.getTelefone()
        );
    }

    public List<MoradorResponseDTO> findByNome(String nome) {
        List<Morador> lista = rep.findByNomeContainingIgnoreCase(nome);
        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Morador não encontrado");
        }
        return lista.stream()
                .map(morador -> new MoradorResponseDTO(
                        morador.getId(),
                        morador.getNome(),
                        morador.getEmail(),
                        morador.getTelefone()
                ))
                .toList();
    }
    public MoradorResponseDTO findByEmail(String email) {
        Morador morador = rep.findByEmail(email).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getEmail(),
                morador.getTelefone()
        );
    }
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
        if(rep.existsByEmail(moradordto.email())) {
            throw new RecursoDuplicadoException("Morador com email já cadastrado");
        }
        Morador morador = rep.save(Morador.builder()
                .nome(moradordto.nome())
                .email(moradordto.email())
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
            morador.setNome(moradordto.nome());
            morador.setEmail(moradordto.email());
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
