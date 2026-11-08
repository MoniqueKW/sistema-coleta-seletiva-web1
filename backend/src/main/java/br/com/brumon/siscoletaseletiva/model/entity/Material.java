package br.com.brumon.siscoletaseletiva.model.entity;

import jakarta.persistence.*;
import lombok.*;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoMaterial;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "material", schema = "coleta_seletiva")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nome", length = 100, nullable = false, unique = true)
    private String nome;
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "unidade", length = 20, nullable = false)
    private String unidade;
    @Column(name = "orientacao", columnDefinition = "TEXT")
    private String orientacao;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", length = 20, nullable = false)
    private SituacaoMaterial situacao = SituacaoMaterial.ATIVO;
}