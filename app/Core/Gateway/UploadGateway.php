<?php

namespace App\Core\Gateway;

use Illuminate\Http\UploadedFile;

interface UploadGateway
{
    public function upload(UploadedFile $file): void;
}