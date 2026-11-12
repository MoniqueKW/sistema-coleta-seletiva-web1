-- Dados fictícios para demonstração

INSERT INTO coleta_seletiva.material (nome, descricao, unidade, orientacao, situacao) VALUES
('Papel','Jornais, revistas e folhas de papel','kg','Separar seco e sem restos de comida.', 'ATIVO'),
('Plástico','Garrafas PET, embalagens e sacolas','kg', 'Lavar e esvaziar as embalagens.','ATIVO'),
('Vidro','Garrafas e potes de vidro','kg', 'Enxaguar e entregar sem tampas.','ATIVO'),
('Metal','Latas de alumínio e de aço','kg', 'Enxaguar e amassar as latas.','ATIVO')
ON CONFLICT (nome) DO NOTHING;

INSERT INTO coleta_seletiva.pontocoleta
(nome, logradouro, numero, bairro, municipio, uf, cep, complemento, contato, horarios, situacao, recolhimento_domiciliar) VALUES
('Cooperativa Recicla Aracaju', 'Rua das Flores', '100', 'Centro', 'Aracaju','SE', '49010-000', NULL,
 '79999990001', 'Seg a Sex, 8h às 17h', 'ATIVO', TRUE),
('Ecoponto Bairro Industrial',  'Avenida Industrial', '500', 'Industrial', 'Aracaju', 'SE', '49030-000', 'Galpão 2',
 '79999990002', 'Seg a Sáb, 7h às 12h', 'ATIVO', FALSE);

INSERT INTO coleta_seletiva.materialaceito (id_ponto, id_material, observacoes, limite_opcional)
SELECT p.id, m.id, v.observacoes, v.limite
FROM (VALUES
    ('Cooperativa Recicla Aracaju', 'Papel', 'Aceita papelão desmontado.', NULL::numeric),
    ('Cooperativa Recicla Aracaju', 'Plástico', 'Limite por solicitação.', 50),
    ('Ecoponto Bairro Industrial', 'Papel', NULL, NULL),
    ('Ecoponto Bairro Industrial',  'Vidro', 'Não aceita vidro quebrado.', NULL),
    ('Ecoponto Bairro Industrial',  'Metal', NULL, NULL)
) AS v(ponto, material, observacoes, limite)
JOIN coleta_seletiva.pontocoleta p ON p.nome = v.ponto
JOIN coleta_seletiva.material m ON m.nome = v.material;

INSERT INTO coleta_seletiva.morador (nome, email, telefone) VALUES
('Maria Silva', 'maria.silva@example.com', '5579999991111'),
('João Santos', 'joao.santos@example.com', '5579999992222')
ON CONFLICT (email) DO NOTHING;

INSERT INTO coleta_seletiva.endereco (id_morador, logradouro, numero, bairro, municipio, uf, cep, complemento)
SELECT mo.id, v.logradouro, v.numero, v.bairro, v.municipio, v.uf, v.cep, v.complemento
FROM (VALUES
    ('maria.silva@example.com', 'Rua Laranjeiras', '25',  'Jardins',      'Aracaju', 'SE', '49025-000', 'Apto 101'),
    ('joao.santos@example.com', 'Rua das Palmeiras', 'S/N', 'Farolândia', 'Aracaju', 'SE', '49032-000', NULL::varchar)
) AS v(email, logradouro, numero, bairro, municipio, uf, cep, complemento)
JOIN coleta_seletiva.morador mo ON mo.email = v.email;

INSERT INTO coleta_seletiva.operador (id_ponto, nome, email)
SELECT p.id, v.nome, v.email
FROM (VALUES
    ('Cooperativa Recicla Aracaju', 'Carlos Oliveira', 'carlos.oliveira@example.com'),
    ('Ecoponto Bairro Industrial',  'Ana Souza', 'ana.souza@example.com')
) AS v(ponto, nome, email)
JOIN coleta_seletiva.pontocoleta p ON p.nome = v.ponto
ON CONFLICT (email) DO NOTHING;

INSERT INTO coleta_seletiva.solicitacao
    (id_ponto, id_morador, protocolo_unico, copia_logradouro, copia_numero, copia_bairro, copia_municipio, copia_uf,
     copia_cep, copia_complemento, situacao, data_criacao, observacoes, versao)
SELECT p.id, mo.id, 'SC-DEMO0001', e.logradouro, e.numero, e.bairro, e.municipio, e.uf, e.cep, e.complemento,
       'SOLICITADA', now() - interval '1 day', 'Material separado na garagem.', 0
FROM coleta_seletiva.pontocoleta p, coleta_seletiva.morador mo
JOIN coleta_seletiva.endereco e ON e.id_morador = mo.id
WHERE p.nome = 'Cooperativa Recicla Aracaju' AND mo.email = 'maria.silva@example.com';

INSERT INTO coleta_seletiva.solicitacao
    (id_ponto, id_morador, protocolo_unico, copia_logradouro, copia_numero, copia_bairro, copia_municipio, copia_uf,
     copia_cep, copia_complemento, situacao, data_criacao, data_agendamento, observacoes, versao)
SELECT p.id, mo.id, 'SC-DEMO0002', e.logradouro, e.numero, e.bairro, e.municipio, e.uf, e.cep, e.complemento,
       'AGENDADA', now() - interval '2 days', now() + interval '3 days', NULL, 1
FROM coleta_seletiva.pontocoleta p, coleta_seletiva.morador mo
JOIN coleta_seletiva.endereco e ON e.id_morador = mo.id
WHERE p.nome = 'Cooperativa Recicla Aracaju' AND mo.email = 'joao.santos@example.com';

INSERT INTO coleta_seletiva.solicitacao
    (id_ponto, id_morador, protocolo_unico, copia_logradouro, copia_numero, copia_bairro, copia_municipio, copia_uf,
     copia_cep, copia_complemento, situacao, data_criacao, data_agendamento, data_conclusao, observacoes, versao)
SELECT p.id, mo.id, 'SC-DEMO0003', e.logradouro, e.numero, e.bairro, e.municipio, e.uf, e.cep, e.complemento,
       'CONCLUIDA', now() - interval '7 days', now() - interval '5 days', now() - interval '5 days', NULL, 3
FROM coleta_seletiva.pontocoleta p, coleta_seletiva.morador mo
JOIN coleta_seletiva.endereco e ON e.id_morador = mo.id
WHERE p.nome = 'Cooperativa Recicla Aracaju' AND mo.email = 'maria.silva@example.com';

INSERT INTO coleta_seletiva.itemsolicitacao (id_solicitacao, id_material, copia_unidade, qtd_estimada, qtd_efetiva)
SELECT s.id, m.id, m.unidade, v.estimada, v.efetiva
FROM (VALUES
    ('SC-DEMO0001', 'Papel',    10,  NULL::numeric),
    ('SC-DEMO0001', 'Plástico', 5,   NULL),
    ('SC-DEMO0002', 'Papel',    8,   NULL),
    ('SC-DEMO0003', 'Papel',    12,  11.5),
    ('SC-DEMO0003', 'Plástico', 6,   6)
) AS v(protocolo, material, estimada, efetiva)
JOIN coleta_seletiva.solicitacao s ON s.protocolo_unico = v.protocolo
JOIN coleta_seletiva.material m ON m.nome = v.material;

INSERT INTO coleta_seletiva.historico
    (id_solicitacao, situacao_anterior, situacao_nova, data, responsavel, motivo, info_tentativa, id_operador)
SELECT s.id, v.anterior, v.nova, v.data, COALESCE(op.nome, mo.nome), NULL, NULL, op.id
FROM (VALUES
    ('SC-DEMO0001', NULL,              'SOLICITADA',      now() - interval '1 day',  NULL),
    ('SC-DEMO0002', NULL,              'SOLICITADA',      now() - interval '2 days', NULL),
    ('SC-DEMO0002', 'SOLICITADA',      'AGENDADA',        now() - interval '2 days' + interval '1 hour', 'carlos.oliveira@example.com'),
    ('SC-DEMO0003', NULL,              'SOLICITADA',      now() - interval '7 days', NULL),
    ('SC-DEMO0003', 'SOLICITADA',      'AGENDADA',        now() - interval '6 days', 'carlos.oliveira@example.com'),
    ('SC-DEMO0003', 'AGENDADA',        'EM_RECOLHIMENTO', now() - interval '5 days', 'carlos.oliveira@example.com'),
    ('SC-DEMO0003', 'EM_RECOLHIMENTO', 'CONCLUIDA',       now() - interval '5 days' + interval '2 hours', 'carlos.oliveira@example.com')
) AS v(protocolo, anterior, nova, data, email_operador)
JOIN coleta_seletiva.solicitacao s ON s.protocolo_unico = v.protocolo
JOIN coleta_seletiva.morador mo ON mo.id = s.id_morador
LEFT JOIN coleta_seletiva.operador op ON op.email = v.email_operador;