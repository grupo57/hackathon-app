<?php

namespace App\Infrastructure\Providers;

use App\Core\Domain\Repositories\VideoRepositoryInterface;
use App\Infrastructure\Persistence\VideoRepository;
use Illuminate\Support\ServiceProvider;

class AppServiceProvider extends ServiceProvider
{
    /**
     * Register any application services.
     */
    public function register(): void
    {
        $this->app->bind(VideoRepositoryInterface::class, VideoRepository::class);
    }

    /**
     * Bootstrap any application services.
     */
    public function boot(): void
    {
        //
    }
}
