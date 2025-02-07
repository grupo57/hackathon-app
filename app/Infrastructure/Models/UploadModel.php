<?php

namespace App\Infrastructure\Models;

use App\Core\Gateway\VideoGateway;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class UploadModel extends Model implements VideoGateway
{
    use HasFactory;
    
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