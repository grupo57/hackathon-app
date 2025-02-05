<?php

namespace App\Infrastructure\Jobs;

use App\Infrastructure\Models\UploadModel;
use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Queue\SerializesModels;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Storage;

class ProcessCompletedVideo implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    /**
     * Execute the job.
     */
    public function fire($job, $data)
    {
        Log::info("Processando mensagem da fila: " . json_encode($data));

        // Verifica o status
        if ($data['status'] == 'completed') {
            // Atualiza o modelo
            $upload = UploadModel::find($data['videoId']);
            if ($upload) {
                /** @disregard */
                $zipLink = Storage::disk('s3Zip')->temporaryUrl($data['zipKey'], now()->addHours(24));

                $upload->status = 'done';
                $upload->url = $zipLink;
                $upload->save();

                Log::info("Status do vídeo atualizado com sucesso: " . $data['videoId'] . " status: " . $upload->status);
            } else {
                Log::warning("Vídeo não encontrado: " . $data['videoId']);
            }
        } else {
            Log::warning("Status do vídeo não é 'completed': " . $data['status']);
        }
    }
}
