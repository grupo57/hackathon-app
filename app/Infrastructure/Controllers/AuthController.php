<?php

namespace App\Infrastructure\Controllers;

use App\Infrastructure\Models\User;
use App\Infrastructure\Requests\AuthRequest;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;

class AuthController extends Controller
{

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


    public function register(AuthRequest $request)
    {
        $password = $request->password;

        $user = User::where('email', $request['email'])->first();

        if ($user) {
            return response()->json([
                'success' => false,
                'message' => 'O e-mail informado já está em uso'
            ], 400);
        }

        $user = User::create([
            'name'     => $request->name,
            'email'    => $request->email,
            'password' => Hash::make($password)
        ]);
        $token = $user->createToken('auth_token')->plainTextToken;

        return response()->json([
            'ssuccess'     => true,
            'access_token' => $token,
            'token_type'   => 'Bearer',
        ]);
    }

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


    public function login(Request $request): JsonResponse
    {
        if (!Auth::attempt($request->only('email', 'password'))) {
            return response()->json([
                'success' => false,
                'message' => 'Os dados informados não são válidos'
            ], 401);
        }

        $user = User::where('email', $request['email'])->first();

        $token = $user->createToken('auth_token')->plainTextToken;

        return response()->json([
            'success'               => true,
            'access_token'          => $token,
            'token_type'            => 'Bearer',
        ]);
    }
}
