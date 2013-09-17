<?php
class Anuncio extends ValidatorEloquent {
	protected $table = 'anuncio';

	public $timestamps = false;

	protected $fillable = array('id', 'usuario_id','categoria_id','titulo','descripcion');

	protected $rules = array(
		'titulo'=>'required|min:10|max:45',
		'descripcion'=>'required|min:25|max:750',
		'usuario_id'=>'required|integer',
		'categoria_id'=>'required|integer',
	);
}