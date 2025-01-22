CREATE DATABASE IF NOT EXISTS hackathon;
USE hackathon;

CREATE TABLE IF NOT EXISTS usuario
(
    id                      BIGINT AUTO_INCREMENT NOT NULL,
    nome                    VARCHAR(100)          NOT NULL,
    email                   VARCHAR(100)          NOT NULL,
    salt                    VARCHAR(36)           NOT NULL,
    senha                   VARCHAR(255)          NOT NULL,
    situacao                VARCHAR(20)           NOT NULL,
    data_criacao            DATETIME              NOT NULL  DEFAULT CURRENT_TIMESTAMP ,
    data_ultima_modificacao DATETIME              NOT NULL,
    data_ultimo_login	    DATETIME,
    data_validado			DATETIME,
    CONSTRAINT pk_usuario PRIMARY KEY (id)
);

CREATE INDEX idx_usuario_email  ON usuario (email);


CREATE TABLE IF NOT EXISTS arquivo_metadados
(
    id                          CHAR(36) 			    NOT NULL,
    id_usuario                  BIGINT                  NOT NULL,
    nome                        VARCHAR(100)            NOT NULL,
    tamanho                     INT                     NOT NULL,
    tipo                        VARCHAR(30)             NOT NULL,
    situacao                    VARCHAR(20)             NOT NULL,
    conteudo_hash               VARCHAR(64)             NOT NULL,
    data_criacao                DATETIME                NOT NULL  DEFAULT CURRENT_TIMESTAMP ,
    data_processamento_inicio   DATETIME,
    data_processamento_termino	DATETIME,
    CONSTRAINT pk_arquivo_metadados PRIMARY KEY (id)
);

CREATE INDEX idx_arquivo_metadados_usuario  ON arquivo_metadados (id_usuario);
CREATE INDEX idx_arquivo_metadados_situacao  ON arquivo_metadados (id_usuario, situacao);
