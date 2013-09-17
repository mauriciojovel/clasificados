<?php
use Illuminate\Database\Eloquent\ModelNotFoundException;

class UsuarioController extends AutheticatedController {

	public function getIndex() {
        return 'Denegado';
    }

    public function getAutenticar() {
    	return 'Denegado';
    }

    public function postAutenticar() {
    	$usuario = Input::get('usuario');
    	$clave = Input::get('clave');
    	if($usuario == '' || $clave == '') {
    		return Response::json(array('estado'=>'0','errors'=>array('usuario'=>'El nombre de usuario es obligatorio'
    			, 'clave'=>'La clave es obligatoria')));
    	} else {
    		try {
	    		$user = Usuario::where('nombre','=',$usuario)->orWhere('correo_electronico','=',$usuario)->firstOrFail();
	    		
    			if($user->clave == $clave) {
    				$newToken = $this->token();
    				$user->token = $newToken;
    				$user->save();
    				return Response::json(array('estado'=>'1','errors'=>array(),'token'=>$newToken));
    			} else {
    				return Response::json(array('estado'=>'0','errors'=>array('clave'=>'Clave invalida')));
    			}
	    		
    		} catch(ModelNotFoundException $e) {
    			return Response::json(array('estado'=>'0','errors'=>array('usuario'=>'Usuario no encontrado')));
    		}
    	}
    }

    public function getSave() {
    	return 'Denegado';
    }

    public function postSave() {
    	$input = Input::all();
    	$usuario = new Usuario();
    	if($usuario->validate($input)) {
    		$usuario->fill($input);
    		try {
    			$newToken = $this->token();
    			$user->token = $newToken;
	    		$usuario->save();
	    		return Response::json(array('estado'=>'1', 'errors'=>array(),'token'=>$newToken));
	    	} catch(Exception $e) {
	    		$m = '';
	    		if(strpos($e->getMessage(), 'unq_nombre_usuario')===false) {
	    			if(strpos($e->getMessage(),'unq_correo_electronico')===false) {
	    				$m = $e->getMessage();
	    			} else {
	    				$m = 'El correo electronico ya existe';
	    			}
	    		} else {
	    			$m = 'El nombre de usuario ya existe';
	    		}
	    		return Response::json(array('estado'=>'-1', 'errors'=>(array('mensaje'=>$m))));
	    	}
    	} else {
    		return Response::json(array('estado'=>'0', 'errors'=>$usuario->errors()->getMessages()));
    	}
    }

    private function token() {
    	$tok = '';
    	$tok .= chr(rand(65,90));
    	$tok .= rand(0,100);
    	$tok .= chr(rand(97,122));
    	$tok .= rand(0,100);
    	$tok .= chr(rand(35,38));
    	$tok .= chr(rand(65,90));
    	$tok .= rand(0,100);
    	$tok .= chr(rand(97,122));
    	$tok .= rand(0,100);
    	$tok .= chr(rand(35,38));
    	$tok .= chr(rand(65,90));
    	$tok .= rand(0,100);
    	$tok .= chr(rand(97,122));
    	$tok .= rand(0,100);
    	$tok .= chr(rand(35,38));
    	$tok .= chr(rand(65,90));
    	$tok .= rand(0,100);
    	$tok .= chr(rand(97,122));
    	$tok .= rand(0,100);
    	$tok .= chr(rand(35,38));
    	return md5($tok);
    }
}