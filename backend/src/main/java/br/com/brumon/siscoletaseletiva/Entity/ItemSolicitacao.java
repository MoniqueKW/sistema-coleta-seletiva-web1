package br.com.brumon.siscoletaseletiva.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "itemsolicitacao", schema = "coleta_seletiva")
public class ItemSolicitacao {
    @EmbeddedId
    private ItemSolicitacaoId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idSolicitacao")
    @JoinColumn(name = "id_solicitacao")
    private Solicitacao solicitacao;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idMaterial")
    @JoinColumn(name = "id_material")
    private Material material;
    @Column(name = "copia_unidade", length = 20, nullable = false)
    private String copiaUnidade;
    @Column(name = "qtd_estimada", nullable = false)
    private BigDecimal qtdEstimada;
    @Column(name = "qtd_efetiva")
    private BigDecimal qtdEfetiva;
}