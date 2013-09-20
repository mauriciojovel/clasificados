<?php
class AutheticatedController extends BaseController {
	public function __construct()
    {
    	if(Request::is('anuncio/*')||Request::is('imagen/*')) {
        	$this->beforeFilter('auth.token');
    	}
    }
}