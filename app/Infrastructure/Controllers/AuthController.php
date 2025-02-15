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
