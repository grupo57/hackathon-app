<?php

namespace App\Core\Domain\Repositories;

use App\Core\Domain\Entities\Video;
use App\Core\Gateway\VideoGateway;

interface VideoRepositoryInterface
{
    public function save(Video $video): VideoGateway;
    public function findById(string $id): ?Video;
    public function delete(string $id): void;
    public function findAll(int $userId): array;
}