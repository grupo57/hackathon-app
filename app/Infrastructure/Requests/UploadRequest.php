<?php

namespace App\Infrastructure\Requests;

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
            'video.required' => 'Por favor, informe o arquivo a ser enviado',
            'video.mimetypes'     => 'O arquivo enviado precisa ser MP4, AVI ou MOV',
            'video.max'           => 'O arquivo enviado não pode ser maior que 20MB',
            'interval.required'  => 'Por favor, informe o intervalo de tempo',
            'interval.integer'   => 'O intervalo de tempo precisa ser um número inteiro',
            'interval.min'       => 'O intervalo de tempo precisa ser maior que 0',
            'interval.max'       => 'O intervalo de tempo precisa ser menor que 60'
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
            'video'      => ['required', 'file', 'mimetypes:video/mp4,video/x-msvideo,video/quicktime', 'max:20480'],
            'interval'  => ['required', 'integer', 'min:1', 'max:60']
        ];
    }
}
