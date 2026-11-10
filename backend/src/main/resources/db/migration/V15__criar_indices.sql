-- Postgres cria índice sozinho para PK e UNIQUE (ex.: morador.email, material.nome, solicitacao.protocolo_unico),
-- mas nao para chaves estrangeiras nem para as colunas usadas nos filtros. Os índices abaixo cobrem esses casos.
-- Todos usam IF NOT EXISTS para a migration poder rodar em banco que já tenha algum deles.

-- SOLICITAÇÃO (RF09, RF15, RN05)
-- Filtro por morador + período (GET /solicitacoes?idMorador=...&dataInicio=...&dataFim=...)
CREATE INDEX IF NOT EXISTS idx_solicitacao_morador_data
    ON coleta_seletiva.solicitacao (id_morador, data_criacao);

-- Filtro por ponto, ponto + situação, ponto + situação + período.
-- Também atende a checagem da RN05 (existem solicitações abertas neste ponto?).
CREATE INDEX IF NOT EXISTS idx_solicitacao_ponto_situacao_data
    ON coleta_seletiva.solicitacao (id_ponto, situacao, data_criacao);

-- Filtro por situação + período e o resumo gerencial "solicitações por situação" (RF15).
CREATE INDEX IF NOT EXISTS idx_solicitacao_situacao_data
    ON coleta_seletiva.solicitacao (situacao, data_criacao);

-- Filtro só por período, sem situação nem ponto.
CREATE INDEX IF NOT EXISTS idx_solicitacao_data_criacao
    ON coleta_seletiva.solicitacao (data_criacao);

-- Resumo gerencial "quantidades recolhidas": situacao = CONCLUIDA e data_conclusao no período (RF15).
CREATE INDEX IF NOT EXISTS idx_solicitacao_situacao_conclusao
    ON coleta_seletiva.solicitacao (situacao, data_conclusao);

--ITEM DA SOLICITAÇÃO
-- A PK (id_solicitacao, id_material) já cobre a busca pelos itens de uma solicitação.
-- Falta o lado do material: junção do resumo (RF15), RN05 e "este material já foi usado em solicitação?" (RN03).
CREATE INDEX IF NOT EXISTS idx_itemsolicitacao_material
    ON coleta_seletiva.itemsolicitacao (id_material);

-- HISTÓRICO (RF09, RN03)
-- Histórico de uma solicitação em ordem cronológica.
CREATE INDEX IF NOT EXISTS idx_historico_solicitacao_data
    ON coleta_seletiva.historico (id_solicitacao, data);

-- Histórico de um operador e a checagem de exclusão de operador (RN03).
CREATE INDEX IF NOT EXISTS idx_historico_operador_data
    ON coleta_seletiva.historico (id_operador, data);

-- MATERIAL ACEITO (RF05, RF06)
-- A PK (id_ponto, id_material) já cobre "materiais de um ponto" e o vínculo exato.
-- Falta o sentido inverso: "quais pontos aceitam o material X" (filtro por material em RF06).
CREATE INDEX IF NOT EXISTS idx_materialaceito_material
    ON coleta_seletiva.materialaceito (id_material);

--  PONTO DE COLETA (RF06)
-- O filtro de município compara lower(municipio) = ?, então o índice precisa ser sobre a mesma expressão.
CREATE INDEX IF NOT EXISTS idx_pontocoleta_municipio_lower
    ON coleta_seletiva.pontocoleta (lower(municipio));

-- O filtro de bairro é igualdade exata.
CREATE INDEX IF NOT EXISTS idx_pontocoleta_bairro
    ON coleta_seletiva.pontocoleta (bairro);

-- OPERADOR (RF07)
-- Operadores de um ponto (GET /pontos/{id}/operadores) e checagens por vínculo com o ponto.
CREATE INDEX IF NOT EXISTS idx_operador_ponto
    ON coleta_seletiva.operador (id_ponto);

-- ENDEREÇO (RF02)
-- Endereços de um morador (listagem, exclusão em cascata manual e existsByMorador).
CREATE INDEX IF NOT EXISTS idx_endereco_morador
    ON coleta_seletiva.endereco (id_morador);

-- O filtro de CEP é igualdade exata.
CREATE INDEX IF NOT EXISTS idx_endereco_cep
    ON coleta_seletiva.endereco (cep);

--  MATERIAL (RF03)
-- existsByNomeIgnoreCase compara lower(nome) = lower(?); a UNIQUE existente (nome) não atende a essa expressão.
CREATE INDEX IF NOT EXISTS idx_material_nome_lower
    ON coleta_seletiva.material (lower(nome));