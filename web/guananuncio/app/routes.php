<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the Closure to execute when that URI is requested.
|
*/

Route::get('/', function()
{
	return View::make('hello');
});

//-------------------------------------------
// RESTful Controllers
//-------------------------------------------
Route::controller('pais', 'PaisController');
Route::controller('usuario', 'UsuarioController');
Route::controller('anuncio', 'AnuncioController');
Route::controller('imagen', 'ImagenController');