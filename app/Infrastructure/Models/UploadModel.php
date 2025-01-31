<?php

namespace App\Infrastructure\Models;

use Illuminate\Database\Eloquent\Model;

class UploadModel extends Model
{
    protected $table = 'uploads';

    protected $fillable = [
        'user_id',
        'name',
        'path',
        'size',
        'type',
        'mime_type',
        'extension',
        'url',
        'status',
    ];
}