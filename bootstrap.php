<?php

require __DIR__ . '/vendor/autoload.php';

use Bref\Context\Context;
use Bref\Event\Http\HttpRequestEvent;
use Bref\LaravelBridge\Http\HttpHandler;

$app = require __DIR__ . '/bootstrap/app.php';
$app->make(Illuminate\Contracts\Console\Kernel::class)->bootstrap();

// Criar um handler para o Laravel
$handler = new HttpHandler($app);

// Executar o Lambda Handler
return function (HttpRequestEvent $event, Context $context) use ($handler) {
    return $handler->handleRequest($event, $context);
};