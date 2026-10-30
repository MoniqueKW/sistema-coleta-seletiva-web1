package br.com.brumon.siscoletaseletiva.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "materialaceito", schema = "coleta_seletiva")
public class MaterialAceito {
    @EmbeddedId
    private MaterialAceitoId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPonto")
    @JoinColumn(name = "id_ponto")
    private PontoColeta pontoColeta;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idMaterial")
    @JoinColumn(name = "id_material")
    private Material material;
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    @Column(name = "limite_opcional")
    private BigDecimal limiteOpcional;
}
