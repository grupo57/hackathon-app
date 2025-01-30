<?php

namespace App\Core\Domain\Entity;

class File {

    private string $name;
    private string $path;
    private string $type;

    public function __construct(string $name, string $path, string $type)
    {
        $this->name = $name;
        $this->path = $path;
        $this->type = $type;
    }

    public function getName(): string
    {
        return $this->name;
    }

    public function getPath(): string
    {
        return $this->path;
    }

    public function getType(): string
    {
        return $this->type;
    }
}