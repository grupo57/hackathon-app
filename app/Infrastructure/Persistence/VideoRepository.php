<?php

namespace App\Infrastructure\Persistence;

use App\Core\Domain\Entities\Video;
use App\Core\Domain\Repositories\VideoRepositoryInterface;
use App\Core\Gateway\VideoGateway;
use App\Infrastructure\Models\UploadModel;
use Illuminate\Support\Facades\Auth;

class VideoRepository implements VideoRepositoryInterface
{
    public function save(Video $video): VideoGateway
    {
        return UploadModel::create([
            'user_id' => Auth::id(),
            'name' => $video->title,
            'path' => $video->path,
            'size' => $video->size,
            'type' => $video->format,
            'extension' => $video->format,
            'mime_type' => $video->format,
            'url' => $video->zipUrl,
        ]);
    }

    public function findById(string $id): ?Video
    {
        $video = UploadModel::find($id);
        return $video ? new Video(
            $video->id, 
            $video->name, 
            $video->path, 
            $video->size, 
            $video->extension, 
            $video->status, 
            $video->url
        ) : null;
    }

    public function delete(string $id): void
    {
        UploadModel::destroy($id);
    }

    public function findAll(int $userId): array
    {
        return UploadModel::where('user_id', $userId)
            ->orderBy('created_at', 'desc')
            ->get()->map(function ($video) {
                return new Video(
                    $video->id, 
                    $video->name, 
                    $video->path, 
                    $video->size, 
                    $video->extension, 
                    $video->status, 
                    $video->url
                );
            })->toArray();
    }
}