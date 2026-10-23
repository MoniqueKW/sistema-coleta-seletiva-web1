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
    @Column(name = "logradouro", length = 150, nullable = false)
    private String logradouro;
    @Column(name = "numero", length = 20, nullable = false)
    private String numero;
    @Column(name = "complemento")
    private String complemento;
    // CONTINUAR
}
