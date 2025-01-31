<?php

namespace App\Core\Domain\Entities;

class Video
{
    public function __construct(
        public readonly string $id,
        public readonly string $title,
        public readonly string $path,
        public readonly int $size,
        public readonly string $format
    ) {}
}
