<?php

namespace App\Infrastructure\Controllers;

use App\Core\UseCase\UploadVideoUseCase;
use App\Infrastructure\Requests\UploadRequest;
use Illuminate\Http\JsonResponse;

class UploadController extends Controller
{
    public function __construct(private readonly UploadVideoUseCase $uploadVideoUseCase) {}
    
    public function upload(UploadRequest $request): JsonResponse
    {
        return response()->json([
            'success' => true,
            'file'    =>  $this->uploadVideoUseCase->execute(
                $request->file('video')
            )
        ]);
    }
}
