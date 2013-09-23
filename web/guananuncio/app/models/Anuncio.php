<?php
class Anuncio extends ValidatorEloquent {
	protected $table = 'anuncio';

	public $timestamps = false;

	protected $fillable = array('id', 'usuario_id','categoria_id','titulo','descripcion','precio','telefono','latitud','altitud');

	protected $rules = array(
		'titulo'=>'required|min:10|max:45',
		'descripcion'=>'required|min:25|max:750',
		'categoria_id'=>'required|integer',
		'precio'=>'numeric',
		'telefono'=>'min:0|max:10',
		'latitud'=>'numeric',
		'altitud'=>'numeric'
	);
}