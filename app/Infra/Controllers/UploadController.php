<?php

namespace App\Infra\Controllers;

use App\Infra\Requests\UploadRequest;
use Illuminate\Http\JsonResponse;

class UploadController extends Controller
{
    public function upload(UploadRequest $request): JsonResponse
    {
        $file = $request->file('file');
        $path = $file->store('uploads');

        return response()->json([
            'success' => true,
            'path'    => $path
        ]);
    }
}
