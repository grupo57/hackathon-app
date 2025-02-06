<?php

namespace Tests\Unit\Core\UseCase;

use App\Core\Domain\Entities\Video;
use App\Core\Domain\Repositories\VideoRepositoryInterface;
use App\Core\Gateway\VideoGateway;
use App\Core\UseCase\UploadVideoUseCase;
use App\Infrastructure\Messaging\SQSService;
use App\Infrastructure\Models\UploadModel;
use App\Infrastructure\Storages\S3Storage;
use Illuminate\Http\Request;
use Illuminate\Http\UploadedFile;
use Mockery;
use PHPUnit\Framework\TestCase;

class UploadVideoUseCaseTest extends TestCase
{
    private VideoRepositoryInterface $repository;
    private S3Storage $storage;
    private SQSService $sqsService;
    private UploadVideoUseCase $useCase;
    private UploadModel $videoGateway;

    protected function setUp(): void
    {
        parent::setUp();

        $this->repository = Mockery::mock(VideoRepositoryInterface::class);
        $this->storage = Mockery::mock(S3Storage::class);
        $this->sqsService = Mockery::mock(SQSService::class);
        $this->videoGateway = new UploadModel();
        $this->videoGateway->id = 9999;

        $this->useCase = new UploadVideoUseCase(
            $this->repository,
            $this->storage,
            $this->sqsService
        );
    }

    protected function tearDown(): void
    {
        Mockery::close();
        parent::tearDown();
    }

    public function test_get_by_id_returns_video()
    {
        $videoId = 9999;
        $expectedVideo = new Video($videoId, 'video.mp4', '12345.mp4', 5000, 'mp4');

        $this->repository->expects('findById')
            ->once()
            ->with($videoId)
            ->andReturn($expectedVideo);

        $video = $this->useCase->getById($videoId);

        $this->assertInstanceOf(Video::class, $video);
        $this->assertEquals($videoId, $video->id);
    }

    public function test_get_all_returns_videos()
    {
        $userId = 1;
        $videos = [
            new Video(1, 'video1.mp4', 'key1.mp4', 1000, 'mp4'),
            new Video(2, 'video2.mp4', 'key2.mp4', 2000, 'mp4'),
        ];

        $this->repository->expects('findAll')
            ->once()
            ->with($userId)
            ->andReturn($videos);

        $result = $this->useCase->getAll($userId);

        $this->assertCount(2, $result);
        $this->assertInstanceOf(Video::class, $result[0]);
        $this->assertInstanceOf(Video::class, $result[1]);
    }
}
