<?php

namespace App\Infrastructure\Middlewares;

use Closure;
use Illuminate\Http\Request;

class ForceJsonResponse
{
    /**
     * Manipula uma solicitação de entrada.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return \Illuminate\Http\JsonResponse|\Illuminate\Http\Response
     */
    public function handle(Request $request, Closure $next)
    {
        // Define o cabeçalho "Accept" para forçar JSON
        $request->headers->set('Accept', 'application/json');

        return $next($request);
    }
}
