<?php
use Illuminate\Database\Eloquent\ModelNotFoundException;
class AnuncioController extends AutheticatedController {
    protected $rangeLatitud = 1000;
    protected $rangeAltitud = 1000;

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

    public function getAnunciosbusqueda($start, $limit) {
        return $this->postAnunciosBusqueda($start, $limit);
    }

    public function postAnunciosbusqueda($start, $limit) {
        $categoria = Input::get('categoria');
        $texto = Input::get('texto');
        $query = Anuncio::where('es_activo','=','1');
        if(isset($categoria) && $categoria != '') {
            $query->where('categoria_id','=',$categoria);
        }
        if(isset($texto) && $texto != '') {
            $query->where(function($q) use($texto) {
                $q->where('titulo','like', '%'.$texto.'%')
                  ->orWhere('descripcion', 'like', '%'.$texto.'%');
            });
        }
        $anuncios = $query->skip($start*$limit)->take($limit)->get();
        return Response::json($anuncios);
        //$queries = DB::getQueryLog();
        //$last_query = end($queries);
        //return $last_query;
    }

    public function postAnuncioscercanos($start, $limit) {
        $altitud = Input::get('altitud');
        $latitud = Input::get('latitud');
        $categoria = Input::get('categoria');
        $texto = Input::get('texto');
        $query = Anuncio::join('usuario','usuario.id','=','anuncio.usuario_id')
            ->where('es_activo','=','1');

        if(isset($categoria) && $categoria != '') {
            $query->where('categoria_id','=',$categoria);
        }

        if(isset($texto) && $texto != '') {
            $query->where(function($q) use($texto) {
                $q->where('titulo','like', '%'.$texto.'%')
                  ->orWhere('descripcion', 'like', '%'.$texto.'%');
            });
        }

        $query->where('usuario.altitud','>=',$altitud-$this->rangeAltitud);
        $query->where('usuario.altitud','<=',$altitud+$this->rangeAltitud);
        $query->where('usuario.latitud','>=',$latitud-$this->rangeLatitud);
        $query->where('usuario.latitud','<=',$latitud+$this->rangeLatitud);
        $query->select('anuncio.titulo', 'anuncio.descripcion', 'anuncio.fecha_creacion'
            , 'anuncio.es_activo', 'anuncio.precio'
            , 'anuncio.telefono', 'usuario.latitud', 'usuario.altitud');

        $anuncios = $query->orderBy('anuncio.id','desc')->skip($start*$limit)->take($limit)->get();
        return Response::json($anuncios);
        //$queries = DB::getQueryLog();
        //$last_query = end($queries);
        //return $last_query;
    }
}