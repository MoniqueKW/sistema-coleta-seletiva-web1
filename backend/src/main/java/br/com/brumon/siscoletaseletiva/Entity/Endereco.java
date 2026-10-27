package br.com.brumon.siscoletaseletiva.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "endereco", schema = "coleta_seletiva")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_morador", nullable = false)
    private Morador morador;
    @Column(name = "logradouro", length = 150, nullable = false)
    private String logradouro;
    @Column(name = "numero", length = 20, nullable = false)
    private String numero;
    @Column(name = "complemento")
    private String complemento;
    @Column(name = "bairro", length = 100, nullable = false)
    private String bairro;
    @Column(name = "municipio", length = 100, nullable = false)
    private String municipio;
    @Column(name = "uf", length = 2, nullable = false)
    private Character uf;
    @Column(name = "cep", length = 10, nullable = false)
    private String cep;
}
