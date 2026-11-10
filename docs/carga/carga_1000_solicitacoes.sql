-- =====================================================================================================
-- CARGA DE TESTE PARA O RNF04 (desempenho): 1.000 solicitações com itens e histórico.
--
-- NÃO é migration do Flyway: fica em docs/carga/ e roda só quando você quiser medir.
-- Pré-requisito: o banco já foi migrado (V1 a V15) e tem os dados de demonstração da V14
--   (usa os materiais Papel, Plástico, Vidro e Metal).
-- Como rodar (psql, na raiz do projeto):
--   psql -U postgres -d coleta_seletiva -f docs/carga/carga_1000_solicitacoes.sql
-- Para desfazer tudo: limpar_carga.sql (apaga só o que esta carga criou).
--
-- Tudo que é criado aqui tem uma marca para a limpeza achar depois:
--   moradores  -> e-mail  carga<N>@teste.local          pontos    -> nome  'CARGA - Ponto <N>'
--   operadores -> e-mail  operador.carga<N>@teste.local solicitações -> protocolo 'CG-000001' ...
--
-- A carga é determinística (setseed): rodar de novo depois da limpeza gera a mesma distribuição.
-- Roda numa única transação: se algo falhar, nada fica gravado.
-- =====================================================================================================

-- Garante que os acentos (Plástico etc.) cheguem corretos mesmo em terminal Windows.
SET client_encoding TO 'UTF8';

BEGIN;

-- Trava de segurança: não duplica a carga e confere se a demonstração (V14) existe.
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM coleta_seletiva.solicitacao WHERE protocolo_unico LIKE 'CG-%') THEN
        RAISE EXCEPTION 'A carga já existe. Rode limpar_carga.sql antes de carregar de novo.';
    END IF;
    IF (SELECT count(*) FROM coleta_seletiva.material
         WHERE nome IN ('Papel', 'Plástico', 'Vidro', 'Metal')) < 4 THEN
        RAISE EXCEPTION 'Materiais de demonstração (V14) não encontrados: Papel, Plástico, Vidro e Metal.';
    END IF;
END $$;

SELECT setseed(0.42);

-- ---------------------------------------------------------------- 1) cadastros de apoio
-- 200 moradores de teste, cada um com 1 endereço (média de 5 solicitações por morador).
INSERT INTO coleta_seletiva.morador (nome, email, telefone)
SELECT 'Morador Carga ' || g,
       'carga' || g || '@teste.local',
       '5579999' || lpad(g::text, 6, '0')
FROM generate_series(1, 200) AS g
ON CONFLICT (email) DO NOTHING;

INSERT INTO coleta_seletiva.endereco (id_morador, logradouro, numero, bairro, municipio, uf, cep, complemento)
SELECT mo.id,
       'Rua de Teste ' || substring(mo.email FROM 'carga([0-9]+)@'),
       (1 + mo.id % 300)::text,
       (ARRAY['Centro', 'Jardins', 'Farolândia', 'Industrial', 'Atalaia'])[1 + mo.id % 5],
       (ARRAY['Aracaju', 'Nossa Senhora do Socorro', 'São Cristóvão', 'Estância'])[1 + mo.id % 4],
       'SE',
       '49' || lpad((mo.id % 1000)::text, 3, '0') || '-000',
       NULL
FROM coleta_seletiva.morador mo
WHERE mo.email LIKE 'carga%@teste.local'
  AND NOT EXISTS (SELECT 1 FROM coleta_seletiva.endereco e WHERE e.id_morador = mo.id);

-- 10 pontos de teste, todos ativos, com recolhimento domiciliar (~100 solicitações por ponto).
INSERT INTO coleta_seletiva.pontocoleta
    (nome, logradouro, numero, bairro, municipio, uf, cep, complemento, contato, horarios, situacao, recolhimento_domiciliar)
SELECT 'CARGA - Ponto ' || g,
       'Avenida de Teste ' || g,
       (100 + g)::text,
       (ARRAY['Centro', 'Jardins', 'Farolândia', 'Industrial', 'Atalaia'])[1 + g % 5],
       (ARRAY['Aracaju', 'Nossa Senhora do Socorro', 'São Cristóvão', 'Estância'])[1 + g % 4],
       'SE',
       '49' || lpad((g * 10)::text, 3, '0') || '-000',
       NULL,
       '7999999' || lpad(g::text, 4, '0'),
       'Seg a Sex, 8h às 17h',
       'ATIVO',
       TRUE
FROM generate_series(1, 10) AS g
WHERE NOT EXISTS (SELECT 1 FROM coleta_seletiva.pontocoleta WHERE nome = 'CARGA - Ponto ' || g);

-- Cada ponto de teste aceita os 4 materiais, sem limite (a carga não deve esbarrar em regra de limite).
INSERT INTO coleta_seletiva.materialaceito (id_ponto, id_material, observacoes, limite_opcional)
SELECT p.id, m.id, NULL, NULL
FROM coleta_seletiva.pontocoleta p
CROSS JOIN coleta_seletiva.material m
WHERE p.nome LIKE 'CARGA - Ponto %'
  AND m.nome IN ('Papel', 'Plástico', 'Vidro', 'Metal')
ON CONFLICT DO NOTHING;

-- 1 operador por ponto de teste (o histórico guarda quem fez cada mudança).
INSERT INTO coleta_seletiva.operador (id_ponto, nome, email)
SELECT p.id,
       'Operador Carga ' || substring(p.nome FROM '([0-9]+)$'),
       'operador.carga' || substring(p.nome FROM '([0-9]+)$') || '@teste.local'
FROM coleta_seletiva.pontocoleta p
WHERE p.nome LIKE 'CARGA - Ponto %'
ON CONFLICT (email) DO NOTHING;

-- ---------------------------------------------------------------- 2) sorteio das 1.000 solicitações
-- Os números aleatórios ficam numa tabela temporária para cada um ser sorteado UMA vez por linha.
CREATE TEMP TABLE carga_sorteio ON COMMIT DROP AS
SELECT g AS n, random() AS r_morador, random() AS r_ponto, random() AS r_situacao, random() AS r_data
FROM generate_series(1, 1000) AS g;

CREATE TEMP TABLE carga_base ON COMMIT DROP AS
SELECT s.n,
       (SELECT array_agg(id ORDER BY id) FROM coleta_seletiva.morador
         WHERE email LIKE 'carga%@teste.local')
           [1 + floor(s.r_morador * 200)::int] AS id_morador,
       (SELECT array_agg(id ORDER BY id) FROM coleta_seletiva.pontocoleta
         WHERE nome LIKE 'CARGA - Ponto %')
           [1 + floor(s.r_ponto * 10)::int] AS id_ponto,
       -- distribuição: 25% SOLICITADA, 20% AGENDADA, 10% EM_RECOLHIMENTO, 30% CONCLUIDA, 5% RECUSADA, 10% CANCELADA
       CASE WHEN s.r_situacao < 0.25 THEN 'SOLICITADA'
            WHEN s.r_situacao < 0.45 THEN 'AGENDADA'
            WHEN s.r_situacao < 0.55 THEN 'EM_RECOLHIMENTO'
            WHEN s.r_situacao < 0.85 THEN 'CONCLUIDA'
            WHEN s.r_situacao < 0.90 THEN 'RECUSADA'
            ELSE 'CANCELADA' END AS situacao,
       -- datas espalhadas entre 90 e 4 dias atrás (os 4 dias de folga evitam data de conclusão no futuro)
       now() - interval '4 days' - s.r_data * interval '86 days' AS criado
FROM carga_sorteio s;

-- ---------------------------------------------------------------- 3) solicitações
-- Copia o endereço do morador (RN04) e usa a "versão" coerente com o número de mudanças de situação.
INSERT INTO coleta_seletiva.solicitacao
    (id_ponto, id_morador, protocolo_unico,
     copia_logradouro, copia_numero, copia_bairro, copia_municipio, copia_uf, copia_cep, copia_complemento,
     situacao, data_criacao, data_agendamento, data_conclusao, observacoes, versao, recolhimento_domiciliar)
SELECT b.id_ponto, b.id_morador, 'CG-' || lpad(b.n::text, 6, '0'),
       e.logradouro, e.numero, e.bairro, e.municipio, e.uf, e.cep, e.complemento,
       b.situacao,
       b.criado,
       CASE WHEN b.situacao IN ('AGENDADA', 'EM_RECOLHIMENTO', 'CONCLUIDA') THEN b.criado + interval '1 hour' END,
       CASE WHEN b.situacao = 'CONCLUIDA' THEN b.criado + interval '3 days' END,
       'Solicitação gerada para teste de carga',
       CASE b.situacao WHEN 'SOLICITADA' THEN 0
                       WHEN 'EM_RECOLHIMENTO' THEN 2
                       WHEN 'CONCLUIDA' THEN 3
                       ELSE 1 END,
       TRUE
FROM carga_base b
JOIN LATERAL (SELECT * FROM coleta_seletiva.endereco
               WHERE id_morador = b.id_morador ORDER BY id LIMIT 1) e ON TRUE;

-- ---------------------------------------------------------------- 4) itens (1 a 3 por solicitação)
-- Materiais distintos dentro da mesma solicitação. Só as CONCLUIDA têm quantidade efetiva (RF15):
-- o primeiro item sempre é positivo (regra do RF13) e os demais podem ser zero.
INSERT INTO coleta_seletiva.itemsolicitacao (id_solicitacao, id_material, copia_unidade, qtd_estimada, qtd_efetiva)
SELECT s.id, mat.id, mat.unidade,
       1 + ((b.n * 7 + j * 3) % 40) * 0.5,
       CASE WHEN b.situacao <> 'CONCLUIDA' THEN NULL
            WHEN j = 0 THEN round((1 + ((b.n * 7 + j * 3) % 40) * 0.5) * 0.9, 2)
            ELSE (1 + ((b.n * 7 + j * 3) % 40) * 0.5) * (j % 2) END
FROM carga_base b
JOIN coleta_seletiva.solicitacao s ON s.protocolo_unico = 'CG-' || lpad(b.n::text, 6, '0')
CROSS JOIN LATERAL generate_series(0, b.n % 3) AS j
JOIN (SELECT id, unidade, row_number() OVER (ORDER BY id) - 1 AS pos
        FROM coleta_seletiva.material
       WHERE nome IN ('Papel', 'Plástico', 'Vidro', 'Metal')) mat
  ON mat.pos = (b.n + j) % 4;

-- ---------------------------------------------------------------- 5) histórico
-- Criação (quem fez foi o morador) ...
INSERT INTO coleta_seletiva.historico
    (id_solicitacao, situacao_anterior, situacao_nova, data, responsavel, motivo, id_operador)
SELECT s.id, NULL, 'SOLICITADA', s.data_criacao, mo.nome, 'Solicitação criada', NULL
FROM coleta_seletiva.solicitacao s
JOIN coleta_seletiva.morador mo ON mo.id = s.id_morador
WHERE s.protocolo_unico LIKE 'CG-%';

-- ... e as mudanças seguintes, de acordo com a situação final (quem fez foi o operador do ponto).
INSERT INTO coleta_seletiva.historico
    (id_solicitacao, situacao_anterior, situacao_nova, data, responsavel, motivo, id_operador)
SELECT s.id, t.anterior, t.nova, s.data_criacao + t.deslocamento, op.nome, t.motivo, op.id
FROM coleta_seletiva.solicitacao s
JOIN coleta_seletiva.operador op
  ON op.id_ponto = s.id_ponto AND op.email LIKE 'operador.carga%@teste.local'
JOIN (VALUES
        ('SOLICITADA',      'AGENDADA',        interval '1 hour', 'Solicitação agendada'),
        ('AGENDADA',        'EM_RECOLHIMENTO', interval '1 day',  'Solicitação iniciada (Em recolhimento)'),
        ('EM_RECOLHIMENTO', 'CONCLUIDA',       interval '3 days', 'Solicitação concluída'),
        ('SOLICITADA',      'RECUSADA',        interval '1 hour', 'Fora da área de atendimento'),
        ('SOLICITADA',      'CANCELADA',       interval '2 hours', 'Morador desistiu')
     ) AS t(anterior, nova, deslocamento, motivo)
  ON (s.situacao = 'AGENDADA'        AND t.nova = 'AGENDADA')
  OR (s.situacao = 'EM_RECOLHIMENTO' AND t.nova IN ('AGENDADA', 'EM_RECOLHIMENTO'))
  OR (s.situacao = 'CONCLUIDA'       AND t.nova IN ('AGENDADA', 'EM_RECOLHIMENTO', 'CONCLUIDA'))
  OR (s.situacao = 'RECUSADA'        AND t.nova = 'RECUSADA')
  OR (s.situacao = 'CANCELADA'       AND t.nova = 'CANCELADA')
WHERE s.protocolo_unico LIKE 'CG-%';

COMMIT;

-- Atualiza as estatísticas para o otimizador do Postgres enxergar o novo volume antes de você medir.
ANALYZE coleta_seletiva.solicitacao;
ANALYZE coleta_seletiva.itemsolicitacao;
ANALYZE coleta_seletiva.historico;
ANALYZE coleta_seletiva.pontocoleta;
ANALYZE coleta_seletiva.morador;
ANALYZE coleta_seletiva.endereco;

-- ---------------------------------------------------------------- conferência (o resultado deve bater com isto)
--   solicitações da carga ............ 1000
--   itens ............................ ~2000 (1 a 3 por solicitação)
--   histórico ........................ 1000 + uma linha por mudança de situação
SELECT 'solicitações da carga' AS tabela, count(*) AS total
  FROM coleta_seletiva.solicitacao WHERE protocolo_unico LIKE 'CG-%'
UNION ALL
SELECT 'itens da carga', count(*)
  FROM coleta_seletiva.itemsolicitacao i
  JOIN coleta_seletiva.solicitacao s ON s.id = i.id_solicitacao WHERE s.protocolo_unico LIKE 'CG-%'
UNION ALL
SELECT 'histórico da carga', count(*)
  FROM coleta_seletiva.historico h
  JOIN coleta_seletiva.solicitacao s ON s.id = h.id_solicitacao WHERE s.protocolo_unico LIKE 'CG-%';

SELECT situacao, count(*) AS total
  FROM coleta_seletiva.solicitacao WHERE protocolo_unico LIKE 'CG-%'
 GROUP BY situacao ORDER BY situacao;

-- Para conferir se o banco usa os índices (rode depois da carga; troque o id_ponto por um existente):
--   EXPLAIN (ANALYZE, BUFFERS)
--   SELECT * FROM coleta_seletiva.solicitacao
--    WHERE id_ponto = <id> AND situacao = 'AGENDADA' ORDER BY data_criacao DESC LIMIT 20;
