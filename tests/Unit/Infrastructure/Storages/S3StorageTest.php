<?php

namespace Tests\Unit\Infrastructure\Storages;

use App\Infrastructure\Storages\S3Storage;
use Illuminate\Http\UploadedFile;
use Illuminate\Support\Facades\Storage;
use Tests\TestCase;

class S3StorageTest extends TestCase
{
    public function test_upload_file_to_s3()
    {
        // Simula o driver S3
        Storage::fake('s3');

        // Mock do arquivo enviado
        $file = UploadedFile::fake()->create('video.mp4', 1024); // 1MB

        // Nome do arquivo no S3
        $filename = 'unique_video.mp4';

        // Instância da classe S3Storage
        $storage = new S3Storage();

        // Executa o upload
        $storage->upload($filename, $file);

        // Verifica se o arquivo foi armazenado no disco 's3' com o nome correto
        Storage::disk('s3')->assertExists($filename);
    }
}
