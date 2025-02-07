<?php

namespace Tests\Unit\Core\Domain\Entities;

use App\Core\Domain\Entities\Video;
use PHPUnit\Framework\TestCase;

class VideoTest extends TestCase
{
    public function test_it_should_create_video_instance()
    {
        $video = new Video(
            id: 1,
            title: 'Sample Video',
            path: '/videos/sample.mp4',
            size: 1024,
            format: 'mp4'
        );

        $this->assertEquals(1, $video->id);
        $this->assertEquals('Sample Video', $video->title);
        $this->assertEquals('/videos/sample.mp4', $video->path);
        $this->assertEquals(1024, $video->size);
        $this->assertEquals('mp4', $video->format);
        $this->assertEquals('pending', $video->status);
        $this->assertEquals('', $video->zipUrl);
    }

    public function test_it_should_set_id()
    {
        $video = new Video(
            id: 1,
            title: 'Sample Video',
            path: '/videos/sample.mp4',
            size: 1024,
            format: 'mp4'
        );

        $video->setId(2);
        $this->assertEquals(2, $video->id);
    }

    public function test_it_should_set_status()
    {
        $video = new Video(
            id: 1,
            title: 'Sample Video',
            path: '/videos/sample.mp4',
            size: 1024,
            format: 'mp4'
        );

        $video->setStatus('processed');
        $this->assertEquals('processed', $video->status);
    }

    public function test_it_should_allow_null_zipUrl()
    {
        $video = new Video(
            id: 1,
            title: 'Sample Video',
            path: '/videos/sample.mp4',
            size: 1024,
            format: 'mp4',
            zipUrl: null
        );

        $this->assertNull($video->zipUrl);
    }
}
