<?php

namespace App\Core\Domain\Entity;

class FileType {

    public static function isValid(string $filename): bool
    {
        return in_array(self::getExtension($filename), self::getTypes());
    }

    public static function getExtension(string $filename): string
    {
        return pathinfo($filename, PATHINFO_EXTENSION);
    }

    public static function getTypes(): array
    {
        return [
            'mp4',
            'avi',
            'mkv',
        ];;
    }
}