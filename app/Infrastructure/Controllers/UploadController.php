<?php

namespace App\Infrastructure\Controllers;

use App\Core\UseCase\UploadVideoUseCase;
use App\Infrastructure\Requests\UploadRequest;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Auth;

/**
 * @OA\Info(
 *     version="1.0.0",
 *     title="Video Extract API",
 *     description="API responsável por enviar vídeos para extração de imagens",
 * )
 */
class UploadController extends Controller
{
    public function __construct(private readonly UploadVideoUseCase $uploadVideoUseCase) {}

/**
 * @OA\PathItem(
 *     path="/api/uploads"
 * )
 * @OA\Post(
 *     path="/api/uploads",
 *     summary="Upload de vídeo",
 *     tags={"Uploads"},
 *     @OA\RequestBody(
 *         required=true,
 *         @OA\MediaType(
 *             mediaType="multipart/form-data",
 *             @OA\Schema(
 *                 required={"video","interval"},
 *                 @OA\Property(property="video", type="string", format="binary"),
 *                 @OA\Property(property="interval", type="string", example="10")
 *             )
 *         )
 *     ),
 *     @OA\Response(
 *         response=200,
 *         description="Upload bem-sucedido",
 *         @OA\JsonContent(
 *             type="object",
 *             example={
 *                 "success": true,
 *                 "file": {
 *                     "id": 50,
 *                     "title": "phpWqHimU",
 *                     "path": "67a2b0da64e9a.mp4",
 *                     "size": 1397107,
 *                     "format": "mp4",
 *                     "status": "pending",
 *                     "zipUrl": ""
 *                 }
 *             },
 *             @OA\Property(property="success", type="boolean"),
 *             @OA\Property(
 *                 property="file",
 *                 type="object",
 *                 @OA\Property(property="id", type="integer"),
 *                 @OA\Property(property="title", type="string"),
 *                 @OA\Property(property="path", type="string"),
 *                 @OA\Property(property="size", type="integer"),
 *                 @OA\Property(property="format", type="string"),
 *                 @OA\Property(property="status", type="string"),
 *                 @OA\Property(property="zipUrl", type="string")
 *             )
 *         )
 *     ),
 *     @OA\Response(
 *         response=400,
 *         description="Dados inválidos"
 *     ),
 *     security={{ "bearerAuth": {} }}
 * )
 */


    public function upload(UploadRequest $request): JsonResponse
    {
        return response()->json([
            'success' => true,
            'file'    =>  $this->uploadVideoUseCase->execute(
                $request->file('video')
            )
        ]);
    }

/**
 * @OA\PathItem(
 *     path="/api/uploads/{id}"
 * )
 * @OA\Get(
 *     path="/api/uploads/{id}",
 *     summary="Obter detalhes de upload específico",
 *     tags={"Uploads"},
 *     @OA\Parameter(
 *         name="id",
 *         in="path",
 *         required=true,
 *         @OA\Schema(type="integer"),
 *         description="ID do upload"
 *     ),
 *     @OA\Response(
 *         response=200,
 *         description="Detalhes do upload",
 *         @OA\JsonContent(
 *             type="object",
 *             example={
 *                 "success": true,
 *                 "files": {
 *                     "id": 50,
 *                     "title": "phpWqHimU",
 *                     "path": "67a2b0da64e9a.mp4",
 *                     "size": 1397107,
 *                     "format": "mp4",
 *                     "status": "done",
 *                     "zipUrl": "https://fiap-grupo57-hackathon-zip.s3.amazonaws.com/output/thumbnails-1738715363350.zip?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAQMFMULWT3UDMESXZ%2F20250205%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250205T003124Z&X-Amz-SignedHeaders=host&X-Amz-Expires=86400&X-Amz-Signature=ad3d21d3889ca55cc2977ca356dfce4757b6c7a0e32865d2f18173ed4aab8024"
 *                 }
 *             },
 *             @OA\Property(property="success", type="boolean"),
 *             @OA\Property(
 *                 property="files",
 *                 type="object",
 *                 @OA\Property(property="id", type="integer"),
 *                 @OA\Property(property="title", type="string"),
 *                 @OA\Property(property="path", type="string"),
 *                 @OA\Property(property="size", type="integer"),
 *                 @OA\Property(property="format", type="string"),
 *                 @OA\Property(property="status", type="string"),
 *                 @OA\Property(property="zipUrl", type="string")
 *             )
 *         )
 *     ),
 *     @OA\Response(
 *         response=404,
 *         description="Upload não encontrado"
 *     ),
 *     security={{ "bearerAuth": {} }}
 * )
 */


    public function getById(int $uploadId): JsonResponse
    {
        return response()->json([
            'success' => true,
            'files'   => $this->uploadVideoUseCase->getById($uploadId)
        ]);
    }

/**
 * @OA\PathItem(
 *     path="/api/uploads"
 * )
 * @OA\Get(
 *     path="/api/uploads",
 *     summary="Listar todos os uploads",
 *     tags={"Uploads"},
 *     @OA\Response(
 *         response=200,
 *         description="Lista de uploads",
 *         @OA\JsonContent(
 *             type="object",
 *             example={
 *                 "success": true,
 *                 "files": {
 *                     {
 *                         "id": 47,
 *                         "title": "phpsfPC0t",
 *                         "path": "67a2aef22680a.mp4",
 *                         "size": 1397107,
 *                         "format": "mp4",
 *                         "status": "pending",
 *                         "zipUrl": null
 *                     },
 *                     {
 *                         "id": 46,
 *                         "title": "php8lYq8o",
 *                         "path": "67a2ac422a3f9.mp4",
 *                         "size": 1397107,
 *                         "format": "mp4",
 *                         "status": "pending",
 *                         "zipUrl": null
 *                     }
 *                 }
 *             },
 *          )
 *    ),
 *    security={{ "bearerAuth": {} }}
 * )
 */

    public function index(): JsonResponse
    {
        $userId = Auth::id();

        return response()->json([
            'success' => true,
            'files'   => $this->uploadVideoUseCase->getAll($userId)
        ]);
    }
}
