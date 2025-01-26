package br.com.fiap.soat07.hackathon;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            // Inicializa o handler com a classe principal do Spring Boot
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(ProcessadorDeVideosApplication.class);
        } catch (ContainerInitializationException e) {
            // Lidar com o erro de inicialização do container
            throw new RuntimeException("Erro ao inicializar o handler do Spring Boot", e);
        }
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        return handler.proxy(input, context);
    }
}
