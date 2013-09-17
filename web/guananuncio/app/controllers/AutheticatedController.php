<?php
class AutheticatedController extends BaseController {
	public function __construct()
    {
    	if(Request::is('anuncio/*')) {
        	$this->beforeFilter('auth.token');
    	}
    }
}