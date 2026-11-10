-- =====================================================================================================
-- LIMPEZA DA CARGA DE TESTE (desfaz o carga_1000_solicitacoes.sql).
--
-- Apaga SÓ o que a carga criou: moradores carga<N>@teste.local, pontos 'CARGA - Ponto <N>',
-- operadores operador.carga<N>@teste.local e tudo ligado a eles (inclusive solicitações que você
-- tenha criado pela API usando esses moradores ou pontos durante a medição).
-- Os dados de demonstração da V14 e o seu histórico real não são tocados.
--
-- Como rodar:
--   psql -U postgres -d coleta_seletiva -f docs/carga/limpar_carga.sql
-- Roda numa única transação: se algo falhar, nada é apagado.
-- =====================================================================================================

BEGIN;

CREATE TEMP TABLE carga_solicitacoes ON COMMIT DROP AS
SELECT s.id
FROM coleta_seletiva.solicitacao s
WHERE s.protocolo_unico LIKE 'CG-%'
   OR s.id_morador IN (SELECT id FROM coleta_seletiva.morador WHERE email LIKE 'carga%@teste.local')
   OR s.id_ponto   IN (SELECT id FROM coleta_seletiva.pontocoleta WHERE nome LIKE 'CARGA - Ponto %');

-- Ordem: primeiro quem depende das solicitações, depois as solicitações, depois os cadastros.
DELETE FROM coleta_seletiva.historico
 WHERE id_solicitacao IN (SELECT id FROM carga_solicitacoes)
    OR id_operador IN (SELECT id FROM coleta_seletiva.operador WHERE email LIKE 'operador.carga%@teste.local');

DELETE FROM coleta_seletiva.itemsolicitacao
 WHERE id_solicitacao IN (SELECT id FROM carga_solicitacoes);

DELETE FROM coleta_seletiva.solicitacao
 WHERE id IN (SELECT id FROM carga_solicitacoes);

DELETE FROM coleta_seletiva.materialaceito
 WHERE id_ponto IN (SELECT id FROM coleta_seletiva.pontocoleta WHERE nome LIKE 'CARGA - Ponto %');

DELETE FROM coleta_seletiva.operador
 WHERE email LIKE 'operador.carga%@teste.local';

DELETE FROM coleta_seletiva.pontocoleta
 WHERE nome LIKE 'CARGA - Ponto %';

DELETE FROM coleta_seletiva.endereco
 WHERE id_morador IN (SELECT id FROM coleta_seletiva.morador WHERE email LIKE 'carga%@teste.local');

DELETE FROM coleta_seletiva.morador
 WHERE email LIKE 'carga%@teste.local';

COMMIT;

ANALYZE coleta_seletiva.solicitacao;
ANALYZE coleta_seletiva.itemsolicitacao;
ANALYZE coleta_seletiva.historico;

-- Conferência: tudo abaixo deve dar 0.
SELECT 'solicitações CG-' AS item, count(*) AS restante
  FROM coleta_seletiva.solicitacao WHERE protocolo_unico LIKE 'CG-%'
UNION ALL
SELECT 'moradores da carga', count(*) FROM coleta_seletiva.morador WHERE email LIKE 'carga%@teste.local'
UNION ALL
SELECT 'pontos da carga', count(*) FROM coleta_seletiva.pontocoleta WHERE nome LIKE 'CARGA - Ponto %';
