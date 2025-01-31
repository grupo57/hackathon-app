<?php

namespace App\Infrastructure\Storages;

use Illuminate\Http\UploadedFile;

class S3Storage
{
    public function upload(string $filename, UploadedFile $file): void
    {
        $file->storeAs('', $filename, [
            'disk' => 's3'
        ]);
    }
}