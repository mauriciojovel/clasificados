<?php
use Illuminate\Database\Eloquent\ModelNotFoundException;
class AnuncioController extends AutheticatedController {

	public function getCategorias() {
		return 'Denegado';
	}

	public function postCategorias() {
        $categorias = Categoria::orderBy('nombre', 'asc')->get();
        return Response::json($categorias);
    }

    public function getAnuncios($init, $limit) {
    	return 'Denegado';
    }

    public function postAnuncios($init, $limit) {
    	$anuncios = Anuncio::orderBy('id', 'desc')
    	      ->where('es_activo', '=','1')
    	      ->skip($init*$limit)->take($limit)->get();
    	return Response::json($anuncios);
    }

    public function getSave() {
    	return 'Denegado';
    }

    public function postSave() {
    	$input = Input::all();
    	$anuncio = new Anuncio();
    	if($anuncio->validate($input)) {
    		$anuncio->fill($input);
    		$anuncio->fecha_creacion = date('Y-m-d H:i:s');
    		$anuncio->es_activo = 1;
    		try {
	    		$anuncio->save();
	    		return Response::json(array('estado'=>1));
    		} catch(Exception $e) {
    			return Response::json(array('estado'=>-1,'errors'=>array('mensaje'=>'Error no esperado, vuelva a intentar')));
    		}
    	} else {
    		return Response::json(array('estado'=>0, 'errors'=>$anuncio->errors()->getMessages()));
    	}
    }

    public function getInactivar($id) {
    	return 'Denegado';
    }

    public function postInactivar($id) {
    	try {
    		$anuncio = Anuncio::findOrFail($id);
    		$anuncio->es_activo = 0;
    		$anuncio->save();
    		return Response::json(array('estado'=>1));
    	}catch(ModelNotFoundException $e) {
    		return Response::json(array('estado'=>0, 'errors'=>array('mensaje'=>'Anuncio no encontrado')));
    	}catch(Exception $e) {
    		return Response::json(array('estado'=>0, 'errors'=>array('mensaje'=>'Error inesperado, vuelva a intentar')));
    	}
    }
}