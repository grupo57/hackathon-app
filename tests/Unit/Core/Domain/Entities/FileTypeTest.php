<?php

namespace Tests\Unit\Core\Domain\Entities;

use App\Core\Domain\Entities\FileType;
use PHPUnit\Framework\TestCase;

class FileTypeTest extends TestCase
{
    public function test_it_should_return_true_for_valid_file_types()
    {
        $this->assertTrue(FileType::isValid('video.mp4'));
        $this->assertTrue(FileType::isValid('movie.avi'));
        $this->assertTrue(FileType::isValid('series.mkv'));
    }

    public function test_it_should_return_false_for_invalid_file_types()
    {
        $this->assertFalse(FileType::isValid('document.pdf'));
        $this->assertFalse(FileType::isValid('image.jpg'));
        $this->assertFalse(FileType::isValid('audio.mp3'));
    }

    public function test_it_should_return_correct_extension()
    {
        $this->assertEquals('mp4', FileType::getExtension('video.mp4'));
        $this->assertEquals('avi', FileType::getExtension('movie.avi'));
        $this->assertEquals('mkv', FileType::getExtension('series.mkv'));
        $this->assertEquals('jpg', FileType::getExtension('image.jpg'));
    }

    public function test_it_should_return_allowed_file_types()
    {
        $this->assertEquals(['mp4', 'avi', 'mkv'], FileType::getTypes());
    }
}
