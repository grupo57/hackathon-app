<?php

namespace App\Infrastructure\Messaging;

use Illuminate\Support\Facades\Queue;

class SQSService
{
    public function sendMessage(array $data): void
    {
        Queue::connection('sqs')->pushRaw(json_encode($data), env('AWS_SQS_QUEUE_URL'));
    }
}
