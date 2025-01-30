package br.com.fiap.soat07.hackathon.infra.aws;

import br.com.fiap.soat07.hackathon.core.exception.SistemaDeArquivosException;
import br.com.fiap.soat07.hackathon.core.gateway.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Duration;

@Component("s3")
public class StorageS3 implements Storage {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageS3.class);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String uploadBucketName;
    private final String downloadBucketName;

    public StorageS3(S3Client s3Client,
                     S3Presigner s3Presigner,
                     @Value("${cloud.aws.s3.bucket-upload-name}") String uploadBucketName,
                     @Value("${cloud.aws.s3.bucket-download-name}") String downloadBucketName
                     ) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.uploadBucketName = uploadBucketName;
        this.downloadBucketName = downloadBucketName;
    }

    @Override
    public void adicionar(String diretorio, String chave, InputStream inputStream) {
        if (chave == null || chave.isEmpty())
            throw new SistemaDeArquivosException("Obrigatório informar a chave");

        // Verifica se o bucket existe
        // Cria o bucket se não existir
        s3Client.createBucket(
                CreateBucketRequest.builder()
                        .bucket(uploadBucketName)
                        .build()
                );

        try {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(uploadBucketName)
                    .key(Path.of(diretorio, chave).toString())
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, inputStream.available()));
            System.err.println(response);
		} catch (S3Exception e) {
			throw new SistemaDeArquivosException("Erro no S3: "+ e.awsErrorDetails().errorMessage()+ " " +e.getMessage()+ " "+e.statusCode(), e);
		} catch (AwsServiceException e) {
			throw new SistemaDeArquivosException("Erro ao processar a requisição a AWS: "+ e.awsErrorDetails().errorMessage(), e);
		} catch (SdkClientException e) {
			throw new SistemaDeArquivosException("Erro ao conectar ao serviço: "+ e.getMessage(), e);
		} catch (IOException e) {
			throw new SistemaDeArquivosException("Erro ao ler o inputStream", e);
		}
    }

    @Override
    public InputStream recuperar(String diretorio, String chave) {
        if (chave == null || chave.isEmpty())
            throw new SistemaDeArquivosException("Obrigatório informar a chave");

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(downloadBucketName)
            .key(Path.of(diretorio, chave).toString())
            .build();
        return s3Client.getObject(getObjectRequest);
    }

    @Override
    public void excluir(String bucketName, String diretorio, String chave) {
        if (chave == null || chave.isEmpty())
            throw new SistemaDeArquivosException("Obrigatório informar a chave");

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(Path.of(diretorio, chave).toString())
            .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    @Override
    public String getUrl(String chave, int duracaoEmMinutos) {
        // Solicitação para gerar a URL assinada
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(downloadBucketName)
                .key(chave)
                .build();

        // Definir o tempo de expiração para a URL (ex: 1 hora)
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
                builder -> builder
                        .getObjectRequest(objectRequest)
                        .signatureDuration(Duration.ofMinutes(duracaoEmMinutos))
        );

        String url = presignedRequest.url().toString();
        return url;
    }

    @Override
    public String toString() {
        return "SistemaDeArquivosS3{" +
                "bucketName='" + uploadBucketName + '\'' +
                '}';
    }
}
