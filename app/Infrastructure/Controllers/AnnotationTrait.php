<?php

namespace App\Infrastructure\Controllers;

trait AnnotationTrait
{
/**
 * @OA\Info(
 *     version="1.0.0",
 *     title="Video Extract API",
 *     description="API responsável por enviar vídeos para extração de imagens",
 * )
 */

 /**
 * @OA\PathItem(
 *     path="/api/login"
 * )
 * @OA\Post(
 *     path="/api/login",
 *     summary="Login",
 *     tags={"Auth"},
 *     @OA\RequestBody(
 *         required=true,
 *         @OA\JsonContent(
 *             required={"email","password"},
 *             @OA\Property(property="email", type="string", example="teste@teste.com.br"),
 *             @OA\Property(property="password", type="string", example="123456")
 *         ),
 *     ),
 *     @OA\Response(
 *         response=200,
 *         description="Login bem-sucedido",
 *         @OA\JsonContent(
 *             type="object",
 *             example={
 *                 "success": true,
 *                 "access_token": "15|yIWAhxDmfOXBaW1vd5ejRpKLo0dT6LNKjkvVkfJS2174c003",
 *                 "token_type": "Bearer"
 *             },
 *             @OA\Property(property="success", type="boolean"),
 *             @OA\Property(property="access_token", type="string"),
 *             @OA\Property(property="token_type", type="string")
 *         )
 *     ),
 *     @OA\Response(
 *         response=401,
 *         description="Credenciais inválidas"
 *     ),
 *     security={{ "bearerAuth": {} }}
 * )
 */

/**
 * @OA\PathItem(
 *     path="/api/register"
 * )
 * @OA\Post(
 *     path="/api/register",
 *     summary="Registrar",
 *     tags={"Auth"},
 *     @OA\RequestBody(
 *         required=true,
 *         @OA\JsonContent(
 *             required={"name","email","password"},
 *             @OA\Property(property="name", type="string", example="Hackathon Teste"),
 *             @OA\Property(property="email", type="string", example="teste@teste.com.br"),
 *             @OA\Property(property="password", type="string", example="123456")
 *         ),
 *     ),
 *     @OA\Response(
 *         response=201,
 *         description="Registro bem-sucedido",
 *         @OA\JsonContent(
 *             type="object",
 *             example={
 *                 "token": "token_de_autenticacao_aqui",
 *                 "user": {
 *                     "id": 1,
 *                     "name": "Hackathon Teste",
 *                     "email": "teste@teste.com.br"
 *                 }
 *             },
 *             @OA\Property(property="token", type="string"),
 *             @OA\Property(
 *                 property="user",
 *                 type="object",
 *                 @OA\Property(property="id", type="integer"),
 *                 @OA\Property(property="name", type="string"),
 *                 @OA\Property(property="email", type="string")
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
}