package br.com.brumon.siscoletaseletiva.Entity;

import br.com.brumon.siscoletaseletiva.Enum.SituacaoSolicitacao;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "solicitacao", schema = "coleta_seletiva")
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_morador", nullable = false)
    private Morador morador;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ponto", nullable = false)
    private PontoColeta pontoColeta;
    @Column(name = "protocolo_unico", length = 50, nullable = false, unique = true)
    private String protocoloUnico;
    @Column(name = "copia_logradouro", length = 150, nullable = false)
    private String copiaLogradouro;
    @Column(name = "copia_numero", length = 20, nullable = false)
    private String copiaNumero;
    @Column(name = "copia_complemento")
    private String copiaComplemento;
    @Column(name = "copia_bairro", length = 100, nullable = false)
    private String copiaBairro;
    @Column(name = "copia_municipio", length = 100, nullable = false)
    private String copiaMunicipio;
    @Column(name = "copia_uf", length = 2, nullable = false)
    private String copiaUf;
    @Column(name = "copia_cep", length = 10, nullable = false)
    private String copiaCep;
   @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", length = 20, nullable = false)
    private SituacaoSolicitacao situacao = SituacaoSolicitacao.SOLICITADA;
    @Column(name = "data_criacao", insertable = false, updatable = false, nullable = false)
    private LocalDateTime dataCriacao;
    @Column(name = "data_agendamento")
    private LocalDateTime dataAgendamento;
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    // adicionei para cumprir a RN01 do projeto, onde deixa a entender que um morador
    // pode escolher a modalidade de recolhimento domiciliar a uma solicitação se o ponto
    // atrelado a esta permitir. Com esse atributo, dá pra validar isso.
    @Column(name = "recolhimento_domiciliar", nullable = false)
    private Boolean recolhimentoDomiciliar;
    @Version
    @Column(name = "versao", nullable = false)
    private Integer versao;
    @Builder.Default
    @OneToMany(mappedBy = "solicitacao", fetch = FetchType.LAZY)
    private List<ItemSolicitacao> itens = new ArrayList<>();
}
