<?php

namespace App\Infrastructure\Controllers;

abstract class Controller
{
/**
 * @OA\Info(
 *    title="API",
 *   version="1.0.0"
 * )
 * 
 * @OA\Server(
 *    url="http://localhost:4005",
 *    description="Local Server"
 * )
 * 
 * @OA\POST(
 *     path="/api/login",
 *     summary="Login",
 *     description="Realizar a autenticação do usuário",
 *     tags={"Test"},
 *     @OA\Parameter(
 *         name="email",
 *         description="E=mail do usuário",
 *         required=true,
 *     ),
 *     @OA\Parameter(
 *         name="password",
 *         description="Senha do usuário",
 *         required=true,
 *     ),
 *     @OA\Response(
 *        response=200,
 *        description="OK",
 *        @OA\JsonContent(
 *           @OA\Examples(example="application/json", value={"success": true, "access_token": "token", "token_type": "bearer", "expires_in": 3600}),
 *        ),
 *     ),
 * ),
 */
}
