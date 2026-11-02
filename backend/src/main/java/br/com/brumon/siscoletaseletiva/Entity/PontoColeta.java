package br.com.brumon.siscoletaseletiva.Entity;

import jakarta.persistence.*;
import lombok.*;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoPonto;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "pontocoleta", schema = "coleta_seletiva")
public class PontoColeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "logradouro", length = 150, nullable = false)
    private String logradouro;
    @Column(name = "numero", length = 20, nullable = false)
    private String numero;
    @Column(name = "bairro", length = 100, nullable = false)
    private String bairro;
    @Column(name = "municipio", length = 100, nullable = false)
    private String municipio;
    @Column(name = "uf", length = 2, nullable = false)
    private String uf;
    @Column(name = "cep", length = 10, nullable = false)
    private String cep;
    @Column(name = "complemento")
    private String complemento;
    @Column(name = "contato")
    private String contato;
    @Column(name = "horarios")
    private String horarios;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", length = 20, nullable = false)
    private SituacaoPonto situacao = SituacaoPonto.ATIVO;
    @Column(name = "recolhimento_domiciliar", nullable = false)
    private Boolean recolhimentoDomiciliar;
}