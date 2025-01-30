<?php

use App\Infra\Controllers\AuthController;
use App\Infra\Controllers\UploadController;
use Illuminate\Support\Facades\Route;

Route::post('register', [AuthController::class, 'register']);
Route::post('login', [AuthController::class, 'login']);

Route::middleware('auth:sanctum')->group(function () {
    Route::post('/upload', [UploadController::class, 'upload']);
});
