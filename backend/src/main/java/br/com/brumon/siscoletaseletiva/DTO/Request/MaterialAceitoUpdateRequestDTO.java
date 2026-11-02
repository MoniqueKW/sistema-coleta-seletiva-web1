package br.com.brumon.siscoletaseletiva.DTO.Request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

//Feito para atualizar a associação Material Aceito entre as
//entidades de Ponto e Material. O outro DTO é usado para criar a
// associação, este é usado para atualizar a associação. Não dá para usar o outro
// pq lá tem o campo de idMaterial, e este não é usado na atualização,
// pois o usuário poderia alterar coisas indevidas, como literalmente mudar o
// material associado ao ponto de coleta, só mudando seu ID.
public record MaterialAceitoUpdateRequestDTO(
    @Size(max = 255)
    String observacoes,
    @Positive
    @Size(max = 255)
    BigDecimal limiteOpcional
    ){ }
