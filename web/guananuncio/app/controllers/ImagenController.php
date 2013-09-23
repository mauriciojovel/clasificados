<?php
use Illuminate\Database\Eloquent\ModelNotFoundException;
class ImagenController extends AutheticatedController {
	public function getDownload($id) {
		try {
			$image = Imagen::findOrFail($id);
			return Response::make($image->imagen, 200, array('content-type' => 'image/jpeg'));
		} catch(ModelNotFoundException $e){
            return $e->getMessage();
        }
	}

    public function getSingle($id) {
        try {
            $image = Imagen::where('anuncio_id','=',$id)->firstOrFail();
            return Response::make($image->imagen, 200, array('content-type' => 'image/jpeg'));
        } catch(ModelNotFoundException $e){
            return $e->getMessage();
        }
    }

    public function postSingle($id) {
        return $this->getSingle($id);
    }

    public function getList($id) {
        try {
            $image = Imagen::where('anuncio_id','=',$id)->select('id')->get();
            return Response::json(array('estado'=>'1', 'imagenes'=>$imagen));
        } catch(Exception $e){
            return Response::json(array('estado'=>0, 'error'=>$e->getMessage()));
        }
    }

    public function postList($id) {
        return $this->getList($id);
    }

    public function postDownload($id) {
        return $this->getDownload($id);
    }

	public function postSave() {
        //if(Input::hasFile('imagen')) {
        	$imagen = new Imagen(Input::all());
        	if($imagen->validate(Input::all())) {
        		try {
        			/*$user = Usuario::where('nombre',=,Input::get('usuario'))->firstOrFail();
        			$imagen->usuario_id = $user->id;*/
                    /*$file = Input::file('imagen');
                    $destinationPath = 'uploads/'.str_random(8);
                    $filename = $file->getClientOriginalName();
                    $uploadSuccess = $file->move($destinationPath, $filename);
                    if( $uploadSuccess ) {
                        $imagen->imagen = File::get($destinationPath.'/'.$filename);
                        $imagen->fecha_creacion = date('Y-m-d H:i:s');
                        $imagen->save();
                        return Response::json(array('estado'=>1));
                    } else {
                        return Response::json(array('estado'=>0,'errors'=>array('mensaje'=>'No se pudo cargar la imagen.')));
                    }*/
                    $imagen->imagen = base64_decode($imagen->imagen);
                    $imagen->fecha_creacion = date('Y-m-d H:i:s');
                    $imagen->save();
                    return Response::json(array('estado'=>1));
        		}/* catch(ModelNotFoundException $e) {
        			return Response::json(array('estado'=>-1,'errors'=>array('mensaje'=>'Usuario no encontrado')));
        		}*/catch(Exception $e) {
        			return Response::json(array('estado'=>-1,'errors'=>array('mensaje'=>'Error no esperado, vuelva a intentar')));
        		}
        	} else {
        		return Response::json(array('estado'=>0, 'errors'=>$imagen->errors()->getMessages()));
        	}
        //} else {
        //    return Response::json(array('estado'=>0, 'errors'=>array('imagen'=>'Es obligatorio el archivo')));
        //}
    }
}