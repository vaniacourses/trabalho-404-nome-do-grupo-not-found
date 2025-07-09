-- Dados de teste específicos para testes Selenium
-- Usando códigos altos para evitar conflitos com dados principais
INSERT INTO pessoa (codigo, nome, cpfcnpj, apelido, data_nascimento, observacao, data_cadastro) VALUES (9999, 'Usuario Teste Selenium', '000.000.000-00', 'TestUser', '1990-01-01', 'Usuario para testes Selenium', '2018-02-14');

INSERT INTO telefone (codigo, fone, tipo, data_cadastro) VALUES (9999, '11999999999', 'CELULAR', '2018-02-14');

INSERT INTO pessoa_telefone (pessoa_codigo, telefone_codigo) VALUES (9999, 9999);

-- Usuário de teste com senha '123'
INSERT INTO usuario (codigo, user, senha, data_cadastro, pessoa_codigo) VALUES (9999, 'testusr', '$2a$10$xuMmyd6tQXff3DbzCvpnMuRqnYhs7IT6OsoZM48tPeclqB2d7FQb.', '2018-02-14', 9999);
INSERT INTO permissoes (codigo, nome, descricao) VALUES (2, 'VISUALIZAR_PESSOA', 'Ver pessoas');

-- Vincula permissões ao grupo
INSERT INTO permissoes_grupo_usuario (grupo_usuario_codigo, permissoes_codigo) VALUES (1, 1);
INSERT INTO permissoes_grupo_usuario (grupo_usuario_codigo, permissoes_codigo) VALUES (1, 2);

-- Dados básicos para funcionamento
INSERT INTO grupo (codigo, descricao, data_cadastro) VALUES (1, 'Padrão', '2018-02-14');
INSERT INTO categoria (codigo, descricao, data_cadastro) VALUES (1, 'Padrão', '2018-02-14');
INSERT INTO pagartipo (codigo, descricao, data_cadastro) VALUES (1, 'Despesa com fornecedor', NOW());
INSERT INTO pagartipo (codigo, descricao, data_cadastro) VALUES (2, 'Despesa com funcionário', NOW());
