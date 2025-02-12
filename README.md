# Hackathon

Este projeto foi desenvolvido com o Framework Laravel na linguagem PHP e banco de dados Mysql.

A porta 4005 é destinada a API RESTful.

### Objetivo do Projeto

Este projeto de software pretende entregar uma aplicação de processamento de imagens, cujos  usuários poderão enviar um vídeo para a aplicação e fazer download das imagens processadas compactadas em um arquivo zip.

Este projeto é um microsserviço responsável por extrair frames de vídeos enviados ao Amazon S3 e faz parte da solução integrada interagindo com o upload de vídeo através dos eventos do S3 e mensageria (SQS).

### Video de Apresentação

<a href="https://youtu.be/dQ0HfnJHjFc?si=fzePXUXRKCnIfYA_"><img src=".doc/thumbnail-hackathon.jpg" width="600"></a>

Caso tenha problemas para visualizar o vídeo <a href="https://youtu.be/dQ0HfnJHjFc?si=fzePXUXRKCnIfYA_  ">CLIQUE AQUI</a>

### Subindo o ambiente

Certifique-se que a porta 4005 esta disponível para subir o container Docker.

Encapsulamos os comandos para subir o container em um arquivo Makefile

Para subir o container rode o seguinte comando:

`make up`

Após o build e o up dos containers a aplicação estará disponível em:

http://localhost:4005/

Para avaliar vulnerabilidades, qualidade do código e o coverage da aplicação, rode o comando:

`make coverage`

### Configuração

Copie o arquivo .env.example para .env

Configure as Enviroments:

#### SQS

*AWS_ACCESS_KEY_ID*: Key do IAM da AWS
*AWS_SECRET_ACCESS_KEY*: Secret Key do IAM da AWS
*AWS_BUCKET*: Bucket para upload do video
*AWS_SQS_QUEUE*: Nome da fila do SQS 
*AWS_SQS_QUEUE_COMPLETED*: Nome da fila do SQS para receber o evento de Zip concluído
*SQS_PREFIX*: Prefixo da url do SQS

#### S3

*AWS_BUCKET_DOWNLOAD*: Nome do bucket para upload do video (a ser enviado para o lambda)
*AWS_BUCKET_UPLOAD*: Nome do bucket para download do zip (a ser enviado para o lambda)

#### RDS

Configure as Enviroments com prefixo DB_ para conectar ao RDS gerado via Terraform.

### Documentação

A aplicação possuí um Swagger para documentar as endpoints criadas para o projeto.

Você pode acessar o Swagger pelo link:

http://localhost:4005/api/documentation

A Collection do **postman** com todas as endpoints necessárias, esta no diretório .postman na raiz do projeto junto com as enviroments.

Você poderá fazer o download da Collection <a target="_blank" href="/.postman/Hackathon.postman_collection.json" target="blank">aqui</a> e as Environment <a target="_blank" href="/.postman/Hackathon.postman_environment.json">aqui</a>.

### Event Storm

O Event Storm desenvolvido e utilizado para o desenvolvimento deste projeto esta no link abaixo:

https://miro.com/welcomeonboard/ZXdSaEEzSHY2M1NVMmhLT21QeXgwMjIyQlJSUlhIMzZhMExWR1QyczlYUngxbzlMcVdHbWd0TWNjalpuQ0hnVUhWM0VLNWNia1Fnb3ZmamFkQ2E3S2ptakswdzFHQTNBSjAvNEh6MnU1M05oNWx6KzZ1UTBqaDZRc2QyeEhxbnZNakdSWkpBejJWRjJhRnhhb1UwcS9BPT0hdjE=?share_link_id=861689604248

Liberamos o acesso para os Professores através do e-mail: pf2031@fiap.com.br

### Alunos - 7SOAT

- Fabio Henrique Peixoto da Silva - RM354678
- Marcello de Almeida Lima - RM355880
- Matheus Tadeu Moreira da Cunha - RM355524
- Eduardo Fabris - RM356333
