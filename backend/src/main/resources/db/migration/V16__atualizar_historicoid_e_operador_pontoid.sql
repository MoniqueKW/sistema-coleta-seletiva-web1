-- a PK de historico passa a se chamar "id", estava como id_historico
ALTER TABLE coleta_seletiva.historico
    RENAME COLUMN id_historico TO id;

-- acompanha o padrão <tabela>_id_seq das outras tabelas.
-- O DEFAULT da coluna referencia a sequência pelo OID, então o renome não quebra nada.
ALTER SEQUENCE IF EXISTS coleta_seletiva.historico_id_historico_seq
    RENAME TO historico_id_seq;

-- Operador não pode existir sem ponto de coleta (a entidade já exige nullable = false),
-- mas isso ainda não estava no banco.
ALTER TABLE coleta_seletiva.operador
    ALTER COLUMN id_ponto SET NOT NULL;