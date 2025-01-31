<?php

namespace App\Core\Domain\Repositories;

use App\Core\Domain\Entities\Video;

interface VideoRepositoryInterface
{
    public function save(Video $video): void;
    public function findById(string $id): ?Video;
    public function delete(string $id): void;
}