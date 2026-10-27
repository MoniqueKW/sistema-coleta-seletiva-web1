package br.com.brumon.siscoletaseletiva.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "operador", schema = "coleta_seletiva")
public class Operador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    //(ESPERAR A ENTIDADE PONTO SER FEITA) |
    //                                    \/
    //@ManyToOne
    //@JoinColumn(name = "id_ponto", nullable = false)
    //private PontoColeta pontoColeta;
    @Column(name = "nome", length = 150, nullable = false)
    private String nome;
    @Column(name = "email", nullable = false)
    private String email;
}
