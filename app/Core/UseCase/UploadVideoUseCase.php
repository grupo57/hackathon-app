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
        $video = new Video(rand(1111,9999), $file->getFilename(), $videoKey, $file->getSize(), $file->getClientOriginalExtension());

        // Salvar no banco de dados
        $videoModel = $this->repository->save($video);
        $video->setId($videoModel->id);

        // Enviar para a fila
        $this->sendToQueue($videoKey, $videoModel);

        return $video;
    }

    private function sendToQueue($videoKey, $videoModel)
    {
        if (class_exists('request')) {
            $interval = request('interval') ?? 10;
        } else {
            $interval = 10;
        }

        // Enviar mensagem para a fila SQS
        $this->sqsService->sendMessage([
            'videoKey' => $videoKey,
            'videoId' => $videoModel->id,
            'interval' => (int) $interval,
            'bucketNameDownload' => env('AWS_BUCKET_DOWNLOAD'),
            'bucketNameUpload' => env('AWS_BUCKET_UPLOAD')
        ]);
    }

    public function getById($uploadId): Video
    {
        return $this->repository->findById($uploadId);
    }

    public function getAll(int $userId)
    {
        return $this->repository->findAll($userId);
    }
}
