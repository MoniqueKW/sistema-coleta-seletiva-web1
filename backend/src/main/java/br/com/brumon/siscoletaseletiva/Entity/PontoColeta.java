package br.com.brumon.siscoletaseletiva.Entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "nome", length = 255, nullable = false)
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
    @Column(name = "complemento", length = 255)
    private String complemento;
    @Column(name = "contato", length = 255)
    private String contato;
    @Column(name = "horarios", length = 255)
    private String horarios;
    @Column(name = "situacao", length = 20)
    private String situacao;
    @Column(name = "recolhimento_domiciliar")
    private Boolean recolhimentoDomiciliar;
}