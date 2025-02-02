<?php

namespace App\Infrastructure\Jobs;

use App\Infrastructure\Models\UploadModel;
use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldBeUnique;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Queue\SerializesModels;
use Illuminate\Support\Facades\Log;

class ProcessCompletedVideo implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    public $queue = 'sqs_completed';

    protected $message;

    /**
     * Create a new job instance.
     */
    public function __construct($message)
    {
        $this->message = $message;
    }

    /**
     * Execute the job.
     */
    public function handle()
    {
        Log::info("Processando mensagem da fila: " . json_encode($this->message));

        // Decodifica o JSON
        $data = json_decode($this->message, true);

        // Verifica o status
        if ($data['status'] == 'completed') {
            // Atualiza o modelo
            $upload = UploadModel::find($data['videoId']);
            if ($upload) {
                $upload->status = 'completed';
                $upload->save();
                Log::info("Status do vídeo atualizado com sucesso: " . $data['videoId']);
            } else {
                Log::warning("Vídeo não encontrado: " . $data['videoId']);
            }
        } else {
            Log::warning("Status do vídeo não é 'completed': " . $data['status']);
        }
    }
}
