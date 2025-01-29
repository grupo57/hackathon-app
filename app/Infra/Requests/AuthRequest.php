<?php

namespace App\Infra\Requests;

use Illuminate\Foundation\Http\FormRequest;

class AuthRequest extends FormRequest
{
    /**
     * Determine if the user is authorized to make this request.
     */
    public function authorize(): bool
    {
        return true;
    }

    /**
     * Get the error messages for the defined validation rules.
     *
     * @return array<string, string>
     */
    public function messages(): array
    {
        return [
            'name.required' => 'Por favor, informe o nome do usuário',
            'email.required' => 'Por favor, informe um e-mail.',
            'email.email' => 'Por favor, informe um e-mail válido.',
            'password.required' => 'Por favor, informe a senha',
            'c_password.same:password' => 'As senhas informadas não coincidem.',
        ];
    }

    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array|string>
     */
    public function rules(): array
    {
        if (request()->method() === 'PATCH')
            return [];

        return [
            'name' => 'required',
            'email' => 'required|email',
            'password' => 'string',
            'c_password' => 'same:password',
        ];
    }
}
