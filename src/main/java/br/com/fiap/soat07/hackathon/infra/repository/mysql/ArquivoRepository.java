package br.com.fiap.soat07.hackathon.infra.repository.mysql;

import br.com.fiap.soat07.hackathon.core.domain.entity.FiltroDeArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.SituacaoDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.TipoDoArquivo;
import br.com.fiap.soat07.hackathon.core.exception.ArquivoNaoEncontradoException;
import br.com.fiap.soat07.hackathon.core.exception.ErroAoRecuperarArquivoException;
import br.com.fiap.soat07.hackathon.core.gateway.ArquivoGateway;
import br.com.fiap.soat07.hackathon.infra.repository.mysql.model.MetadadosDoArquivoModel;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Transactional
@Repository
public class ArquivoRepository implements ArquivoGateway {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<MetadadosDoArquivo> find(FiltroDeArquivo filtro) {
        String sql = """
            SELECT a.*
            FROM hackathon.arquivo_metadados a
            WHERE 1 = 1
            """;
        if (filtro.getUsuario().isPresent())
            sql += " AND a.ID_USUARIO = :IDUSUARIO";
        if (!filtro.getSituacoes().isEmpty())
            sql += " AND a.SITUACAO IN (:SITUACOES)";

        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        if (filtro.getUsuario().isPresent())
            query.setParameter("IDUSUARIO", filtro.getUsuario().get().getId());
        if (!filtro.getSituacoes().isEmpty())
            query.setParameter("SITUACOES", filtro.getSituacoes().stream().map(Enum::name).toList());

        Stream<Tuple> stream = query.getResultList().stream();
        return stream.map(this::mapearParaDTO).toList();
    }

    @Override
    public Optional<MetadadosDoArquivo> get(UUID id) {
        String sql = """
            SELECT a.*
            FROM hackathon.arquivo_metadados a
            WHERE a.id = :ID
            """;
        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        query.setParameter("ID", id.toString());
        try {
            return Stream.of(query.getSingleResult())
                    .map(i -> (Tuple)i)
                    .map(this::mapearParaDTO)
                    .findFirst();
        } catch (NoResultException e) {
            throw new ArquivoNaoEncontradoException(id.toString());
        }
    }

    @Override
    public MetadadosDoArquivo incluir(MetadadosDoArquivo metadadosDoArquivo) {
        MetadadosDoArquivoModel model = new MetadadosDoArquivoModel(
                metadadosDoArquivo.getId(),
                metadadosDoArquivo.getIdUsuario(),
                metadadosDoArquivo.getNome(),
                metadadosDoArquivo.getTipo(),
                metadadosDoArquivo.getTamanho(),
                metadadosDoArquivo.getHash(),
                metadadosDoArquivo.getSituacao()
        );

        if (metadadosDoArquivo.getId() == null)
            entityManager.persist(model);
        else
            entityManager.merge(model);
        entityManager.flush();
        return new MetadadosDoArquivo(model.getId(), model.getIdUsuario(), model.getNome(), model.getTamanhoEmBytes(), model.getHash(), model.getTipo(), model.getSituacao());
    }

    @Override
    public MetadadosDoArquivo atualizaSituacao(MetadadosDoArquivo metadadosDoArquivo, SituacaoDoArquivo situacao) {
        String sql = """
            SELECT a.*
            FROM hackathon.arquivo_metadados a
            WHERE a.id = :ID
            """;
        Query query = entityManager.createNativeQuery(sql, MetadadosDoArquivoModel.class);
        query.setParameter("ID", metadadosDoArquivo.getId().toString());
        try {
            MetadadosDoArquivoModel model = Optional.of((MetadadosDoArquivoModel) query.getSingleResult())
                    .orElseThrow(() -> new ErroAoRecuperarArquivoException(metadadosDoArquivo.getId().toString()));
            model.setSituacao(situacao);

            entityManager.merge(model);
            entityManager.flush();

            return new MetadadosDoArquivo(model.getId(), model.getIdUsuario(), model.getNome(), model.getTamanhoEmBytes(), model.getHash(), model.getTipo(), model.getSituacao());
        } catch (NoResultException e) {
            throw new ArquivoNaoEncontradoException(metadadosDoArquivo.getId().toString());
        }
    }

    @Override
    public MetadadosDoArquivo defineQueArquivoFoiProcessado(MetadadosDoArquivo metadadosDoArquivo, LocalDateTime inicio, LocalDateTime termino) {
        String sql = """
            SELECT a.*
            FROM hackathon.arquivo_metadados a
            WHERE a.id = :ID
            """;
        Query query = entityManager.createNativeQuery(sql, MetadadosDoArquivoModel.class);
        query.setParameter("ID", metadadosDoArquivo.getId().toString());
        try {
            MetadadosDoArquivoModel model = Optional.of((MetadadosDoArquivoModel) query.getSingleResult())
                    .orElseThrow(() -> new ErroAoRecuperarArquivoException(metadadosDoArquivo.getId().toString()));
            if (model.getSituacao().isPossoDefinirComoProcessado()) {
                model.setSituacao(SituacaoDoArquivo.PROCESSADO);
                model.setDataProcessamentoInicio(inicio);
                model.setDataProcessamentoTermino(termino);

                entityManager.merge(model);
                entityManager.flush();
            }

            return new MetadadosDoArquivo(model.getId(), model.getIdUsuario(), model.getNome(), model.getTamanhoEmBytes(), model.getHash(), model.getTipo(), model.getSituacao());
        } catch (NoResultException e) {
            throw new ArquivoNaoEncontradoException(metadadosDoArquivo.getId().toString());
        }
    }

    @Override
    public MetadadosDoArquivo defineQueArquivoFoiProcessadoComErro(MetadadosDoArquivo metadadosDoArquivo, LocalDateTime inicio, LocalDateTime termino, String mensagem) {
        String sql = """
            SELECT a.*
            FROM hackathon.arquivo_metadados a
            WHERE a.id = :ID
            """;
        Query query = entityManager.createNativeQuery(sql, MetadadosDoArquivoModel.class);
        query.setParameter("ID", metadadosDoArquivo.getId().toString());
        try {
            MetadadosDoArquivoModel model = Optional.of((MetadadosDoArquivoModel) query.getSingleResult())
                    .orElseThrow(() -> new ErroAoRecuperarArquivoException(metadadosDoArquivo.getId().toString()));
            if (model.getSituacao().isPossoDefinirComoProcessado()) {
                model.setSituacao(SituacaoDoArquivo.ERRO);
                model.setDataProcessamentoInicio(inicio);
                model.setDataProcessamentoTermino(termino);

                entityManager.merge(model);
                entityManager.flush();
            }

            return new MetadadosDoArquivo(model.getId(), model.getIdUsuario(), model.getNome(), model.getTamanhoEmBytes(), model.getHash(), model.getTipo(), model.getSituacao());
        } catch (NoResultException e) {
            throw new ArquivoNaoEncontradoException(metadadosDoArquivo.getId().toString());
        }
    }

    private MetadadosDoArquivo mapearParaDTO(Tuple tuple) {
        UUID id = UUID.fromString(tuple.get("id", String.class));
        Long idUsuario = tuple.get("id_usuario", Long.class);
        String nome = tuple.get("nome", String.class);
        int tamanho = tuple.get("tamanho", Integer.class);
        TipoDoArquivo tipo = TipoDoArquivo.of(tuple.get("tipo", String.class));
        SituacaoDoArquivo situacao = SituacaoDoArquivo.of(tuple.get("situacao", String.class));
        String hash = tuple.get("conteudo_hash", String.class);
        return new MetadadosDoArquivo(id, idUsuario, nome, tamanho, hash, tipo, situacao);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void atualizaSituacaoComNovaTransacao(MetadadosDoArquivo metadadosDoArquivo, SituacaoDoArquivo situacao) {
        atualizaSituacao(metadadosDoArquivo, situacao);
    }

    // Método genérico para executar operações dentro de uma transação
    public <T> T executeInTransaction(Supplier<T> operation) {
        // Inicia a transação manualmente
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            // Começa a transação
            transaction.begin();

            // Executa a operação fornecida no lambda
            T result = operation.get();

            // Comita a transação
            transaction.commit();
            return result;
        } catch (RuntimeException e) {
            // Em caso de erro, faz rollback
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

}
