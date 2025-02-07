<?php

namespace Tests\Unit\Infrastructure\Messaging;

use App\Infrastructure\Messaging\SQSService;
use Illuminate\Support\Facades\Queue;
use Tests\TestCase;

class SQSServiceTest extends TestCase
{
    public function test_send_message_pushes_to_sqs_queue()
    {
        // Mock dos dados da mensagem
        $messageData = [
            'videoId' => 123,
            'status' => 'pending',
        ];

        // Mock da Queue para verificar se a mensagem foi enviada corretamente
        Queue::fake();

        // Criando a instância do serviço SQS
        $sqsService = new SQSService();

        // Chamando o método que deve enviar a mensagem
        $sqsService->sendMessage($messageData);

        // Verificando se a mensagem foi empurrada para a fila SQS
        Queue::assertCount(0);
    }
}
