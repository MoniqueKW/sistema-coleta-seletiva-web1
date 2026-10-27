package br.com.brumon.siscoletaseletiva.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemSolicitacaoId implements Serializable {

    @Column(name ="id_solicitacao")
    private Integer idSolicitacao;

    @Column(name = "id_material")
    private Integer idMaterial;

}