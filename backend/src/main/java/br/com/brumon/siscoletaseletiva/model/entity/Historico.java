package br.com.brumon.siscoletaseletiva.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "historico", schema = "coleta_seletiva")
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historico")
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitacao", nullable = false)
    private Solicitacao solicitacao;
    @Column(name = "situacao_anterior", length = 50)
    private String situacaoAnterior;
    @Column(name = "situacao_nova", length = 50, nullable = false)
    private String situacaoNova;
    @Column(name = "data", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime data;
    // Guarda o nome de quem realizou a ação; a relação com operador é opcional.
    @Column(name = "responsavel", nullable = false, length = 150)
    private String responsavel;
    @Column(name = "motivo", columnDefinition = "TEXT")
    private String motivo;
    @Column(name = "info_tentativa", columnDefinition = "TEXT")
    private String infoTentativa;
    //decidimos implementar isso para ver diretamente o id do operador
    //responsavel
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operador")
    private Operador operador;
}
