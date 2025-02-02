<?php

require __DIR__ . '/vendor/autoload.php';

use Bref\Event\Http\FastCgiHandler;
use Bref\Context\Context;
use Bref\Event\Http\HttpRequestEvent;

$app = require __DIR__ . '/bootstrap/app.php';
$app->make(Illuminate\Contracts\Console\Kernel::class)->bootstrap();

$handler = new FastCgiHandler('public/index.php');

return function(HttpRequestEvent $event, Context $context) use ($handler) {
    return $handler->handle($event, $context);
};
