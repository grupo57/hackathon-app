<?php

namespace Database\Factories\Infrastructure\Models;

use App\Infrastructure\Models\UploadModel;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Model>
 */
class UploadModelFactory extends Factory
{
    protected $model = UploadModel::class;

    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'user_id' => fake()->numberBetween(1, 10),
            'name' => fake()->name(),
            'path' => fake()->url(),
            'size' => fake()->numberBetween(1, 1000),
            'type' => fake()->mimeType(),
            'mime_type' => fake()->mimeType(),
            'extension' => fake()->fileExtension(),
            'url' => fake()->url(),
            'status' => fake()->randomElement(['pending', 'processing', 'completed']),
        ];
    }
}
