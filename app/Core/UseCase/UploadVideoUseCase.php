<?php

namespace App\Core\UseCase;

use App\Core\Domain\Entities\Video;
use App\Core\Domain\Repositories\VideoRepositoryInterface;
use App\Infrastructure\Messaging\SQSService;
use App\Infrastructure\Storages\S3Storage;
use Illuminate\Http\UploadedFile;

class UploadVideoUseCase
{
    public function __construct(
        private readonly VideoRepositoryInterface $repository,
        private readonly S3Storage $storage,
        private readonly SQSService $sqsService,
    ) {}

    public function execute(UploadedFile $file): Video
    {
        // Nome do arquivo no S3
        $videoKey = uniqid() . '.' . $file->getClientOriginalExtension();
        
        // Upload para o S3
        $this->storage->upload($videoKey, $file);

        // Criar entidade do vídeo
        $video = new Video(uniqid(), $file->getFilename(), $videoKey, $file->getSize(), $file->getClientOriginalExtension());

        // Salvar no banco de dados
        $this->repository->save($video);

        // Enviar mensagem para a fila SQS
        $this->sqsService->sendMessage([
            'videoKey' => $videoKey,
            'interval' => (int) request()->get('interval'),
            'bucketNameDownload' => env('AWS_BUCKET_DOWNLOAD'),
            'bucketNameUpload' => env('AWS_BUCKET_UPLOAD')
        ]);

        return $video;
    }
}
