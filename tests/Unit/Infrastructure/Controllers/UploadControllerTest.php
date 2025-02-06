<?php

namespace Tests\Unit\Infrastructure\Controllers;

use App\Core\Domain\Entities\Video;
use App\Core\UseCase\UploadVideoUseCase;
use App\Infrastructure\Controllers\UploadController;
use App\Infrastructure\Requests\UploadRequest;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\UploadedFile;
use Illuminate\Support\Facades\Auth;
use Mockery;
use Mockery\MockInterface;
use Tests\TestCase;

class UploadControllerTest extends TestCase
{
    private UploadVideoUseCase|MockInterface $uploadVideoUseCase;
    private UploadController $controller;
    private Video $video;

    protected function setUp(): void
    {
        parent::setUp();

        // Criando um mock do UseCase com a injeção de dependência
        $this->uploadVideoUseCase = $this->mock(UploadVideoUseCase::class);

        /** @var UploadVideoUseCase */
        $uploadVideoUseCase = $this->uploadVideoUseCase;        
        // Criando a instância do controlador
        $this->controller = new UploadController($uploadVideoUseCase);

        // Criando um mock do vídeo
        $this->video = new Video(1, 'video.mp4', 's3/video.mp4', 10, 'mp4');
    }

    public function test_it_should_upload_a_video_successfully(): void
    {
        // Criando um arquivo fake para simular o upload
        $file = UploadedFile::fake()->create('video.mp4', 1024, 'video/mp4');

        // Criando um mock da Request
        $request = $this->mock(UploadRequest::class);
        $request->expects('file')
            ->with('video')
            ->andReturn($file);

        // Definindo o retorno esperado do UseCase
        $this->uploadVideoUseCase
            ->expects('execute')
            ->with($file)
            ->andReturn($this->video);

        /** @var UploadRequest */
        $request = $request;

        // Chamando o método do controller
        $response = $this->controller->upload($request);

        // Convertendo a resposta para array
        $responseData = $response->getData(true);

        // Verificações
        $this->assertInstanceOf(JsonResponse::class, $response);
        $this->assertTrue($responseData['success']);
    }

    public function test_it_should_return_video_by_id(): void
    {
        // ID do vídeo de teste
        $uploadId = 1;

        // Definir retorno esperado do UseCase
        $this->uploadVideoUseCase
            ->expects('getById')
            ->with($uploadId)
            ->andReturn($this->video);

        // Chamando o método do controller
        $response = $this->controller->getById($uploadId);

        // Convertendo a resposta para array
        $responseData = $response->getData(true);

        // Verificações
        $this->assertInstanceOf(JsonResponse::class, $response);
        $this->assertTrue($responseData['success']);
    }

    public function test_it_should_return_all_videos_for_authenticated_user(): void
    {
        // Simular um usuário autenticado
        Auth::shouldReceive('id')->once()->andReturn(10);

        // Definir retorno esperado do UseCase
        $this->uploadVideoUseCase
            ->expects('getAll')
            ->with(10)
            ->andReturn(['video1.mp4', 'video2.mp4']);

        // Chamando o método do controller
        $response = $this->controller->index();

        // Convertendo a resposta para array
        $responseData = $response->getData(true);

        // Verificações
        $this->assertInstanceOf(JsonResponse::class, $response);
        $this->assertTrue($responseData['success']);
        $this->assertEquals(['video1.mp4', 'video2.mp4'], $responseData['files']);
    }
}
