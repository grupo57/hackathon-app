<?php

use App\Infrastructure\Controllers\AuthController;
use App\Infrastructure\Controllers\UploadController;
use Illuminate\Support\Facades\Route;

Route::post('register', [AuthController::class, 'register']);
Route::post('login', [AuthController::class, 'login']);

Route::middleware('auth:sanctum')->group(function () {
    Route::post('/uploads', [UploadController::class, 'upload']);
    Route::get('/uploads/{uploadId}', [UploadController::class, 'getById']);
});
