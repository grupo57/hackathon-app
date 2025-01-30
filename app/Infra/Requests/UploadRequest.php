<?php

namespace App\Infra\Requests;

use Illuminate\Foundation\Http\FormRequest;

class UploadRequest extends FormRequest
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
            'file.required' => 'Por favor, informe o arquivo a ser enviado',
            'file.mimetypes'     => 'O arquivo enviado precisa ser MP4, AVI ou MOV',
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
            'file' => ['required', 'file', 'mimetypes:video/mp4,video/x-msvideo,video/quicktime'],
        ];
    }
}
