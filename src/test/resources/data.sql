-- Dados de teste específicos para testes Selenium
-- Usando códigos altos para evitar conflitos com dados principais
INSERT INTO pessoa (codigo, nome, cpfcnpj, apelido, data_nascimento, observacao, data_cadastro) VALUES (9999, 'Usuario Teste Selenium', '000.000.000-00', 'TestUser', '1990-01-01', 'Usuario para testes Selenium', '2018-02-14');

INSERT INTO telefone (codigo, fone, tipo, data_cadastro) VALUES (1, '69988887777', 'CELULAR', '2018-02-14');

INSERT INTO pessoa_telefone (pessoa_codigo, telefone_codigo) VALUES (1, 1);

-- Usuário gerente com senha '123' (hash do BCrypt)
INSERT INTO usuario (codigo, user, senha, data_cadastro, pessoa_codigo) VALUES (1, 'gerente', '$2a$10$xuMmyd6tQXff3DbzCvpnMuRqnYhs7IT6OsoZM48tPeclqB2d7FQb.', '2018-02-14', 1);

-- Grupos de usuário
INSERT INTO grupousuario (codigo, nome, descricao) VALUES (1, 'ADMINISTRADOR', 'Admin geral');
INSERT INTO grupousuario (codigo, nome, descricao) VALUES (2, 'VENDEDOR', 'Vendedor');

-- Vincula usuário ao grupo administrador
INSERT INTO usuario_grupousuario (usuario_codigo, grupo_usuario_codigo) VALUES (1, 1);

-- Permissões básicas
INSERT INTO permissoes (codigo, nome, descricao) VALUES (1, 'ENTRAR_NO_SISTEMA', 'Login');
INSERT INTO permissoes (codigo, nome, descricao) VALUES (2, 'VISUALIZAR_PESSOA', 'Ver pessoas');

-- Vincula permissões ao grupo
INSERT INTO permissoes_grupo_usuario (grupo_usuario_codigo, permissoes_codigo) VALUES (1, 1);
INSERT INTO permissoes_grupo_usuario (grupo_usuario_codigo, permissoes_codigo) VALUES (1, 2);

-- Dados básicos para funcionamento
INSERT INTO grupo (codigo, descricao, data_cadastro) VALUES (1, 'Padrão', '2018-02-14');
INSERT INTO categoria (codigo, descricao, data_cadastro) VALUES (1, 'Padrão', '2018-02-14');
INSERT INTO pagartipo (codigo, descricao, data_cadastro) VALUES (1, 'Despesa com fornecedor', NOW());
INSERT INTO pagartipo (codigo, descricao, data_cadastro) VALUES (2, 'Despesa com funcionário', NOW());
