package br.com.brumon.siscoletaseletiva.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MaterialAceitoId implements Serializable {

    @Column(name = "id_ponto")
    private Integer idPonto;

    @Column(name = "id_material")
    private Integer idMaterial;
}