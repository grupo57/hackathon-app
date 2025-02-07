<?php

namespace Tests\Unit\Infrastructure\Controllers;

use App\Infrastructure\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Support\Facades\Hash;
use Tests\TestCase;

class AuthControllerTest extends TestCase
{
    use RefreshDatabase;

    public function test_it_should_register_a_new_user(): void
    {
        // Simulando dados de cadastro
        $data = [
            'name'     => 'John Doe',
            'email'    => 'johndoe@example.com',
            'password' => 'securepassword',
        ];

        // Fazendo a requisição para a rota de registro
        $response = $this->postJson('/api/register', $data);
        $responseData = $response->json();
        
        // Verificações
        $response->assertStatus(200);
        $this->assertTrue($responseData['ssuccess']);
        $this->assertArrayHasKey('access_token', $responseData);
    }

    public function test_it_should_not_register_if_email_already_exists(): void
    {
        // Criando um usuário previamente
        User::factory()->create(['email' => 'johndoe@example.com']);

        // Simulando dados duplicados
        $data = [
            'name'     => 'John Doe',
            'email'    => 'johndoe@example.com',
            'password' => 'securepassword',
        ];

        // Fazendo a requisição para a rota de registro
        $response = $this->postJson('/api/register', $data);

        // Verificações
        $response->assertStatus(400);
        $response->assertJson([
            'success' => false,
            'message' => 'O e-mail informado já está em uso',
        ]);
    }

    public function test_it_should_login_successfully(): void
    {
        // Criando um usuário
        $user = User::factory()->create([
            'email'    => 'johndoe@example.com',
            'password' => Hash::make('securepassword'),
        ]);

        // Simulando dados de login
        $data = [
            'email'    => 'johndoe@example.com',
            'password' => 'securepassword',
        ];

        // Fazendo a requisição para a rota de login
        $response = $this->postJson('/api/login', $data);

        // Verificações
        $response->assertStatus(200);
        $response->assertJsonStructure(['success', 'access_token', 'token_type']);
    }

    public function test_it_should_not_login_with_invalid_credentials(): void
    {
        // Criando um usuário
        User::factory()->create([
            'email'    => 'johndoe@example.com',
            'password' => Hash::make('securepassword'),
        ]);

        // Simulando dados de login incorretos
        $data = [
            'email'    => 'johndoe@example.com',
            'password' => 'wrongpassword',
        ];

        // Fazendo a requisição para a rota de login
        $response = $this->postJson('/api/login', $data);

        // Verificações
        $response->assertStatus(401);
        $response->assertJson([
            'success' => false,
            'message' => 'Os dados informados não são válidos',
        ]);
    }
}
