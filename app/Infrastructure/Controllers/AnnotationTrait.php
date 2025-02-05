<?php

namespace App\Infrastructure\Controllers;

trait AnnotationTrait
{
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
 *             @OA\Property(property="token", type="string", example="token_de_autenticacao_aqui")
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
 *             @OA\Property(property="token", type="string", example="token_de_autenticacao_aqui")
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
 *             @OA\Property(property="id", type="integer", example=50),
 *             @OA\Property(property="status", type="string", example="completed"),
 *             @OA\Property(property="video_url", type="string", example="http://example.com/video.mp4")
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
 *             type="array",
 *             @OA\Items(
 *                 @OA\Property(property="id", type="integer", example=1),
 *                 @OA\Property(property="status", type="string", example="completed"),
 *                 @OA\Property(property="video_url", type="string", example="http://example.com/video.mp4")
 *             )
 *         )
 *     ),
 *     security={{ "bearerAuth": {} }}
 * )
 */
}