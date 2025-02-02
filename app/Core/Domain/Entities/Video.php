<?php

namespace App\Core\Domain\Entities;

class Video
{
    public function __construct(
        public int $id,
        public readonly string $title,
        public readonly string $path,
        public readonly int $size,
        public readonly string $format,
        public string $status = 'pending',
    ) {}

    public function setId(int $id): void
    {
        $this->id = $id;
    }

    public function setStatus(string $status): void
    {
        $this->status = $status;
    }
}
