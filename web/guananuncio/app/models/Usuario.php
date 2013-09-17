<?php
class Usuario extends ValidatorEloquent {
	protected $table = 'usuario';

	public $timestamps = false;

	protected $fillable = array('id', 'pais_id','nombre','correo_electronico','clave','latitud', 'altitud');

	protected $rules = array(
		'nombre'=>'required|min:5|max:80',
		'correo_electronico'=>'required|email',
		'clave'=>'required|min:32',
		'latitud'=>'required|integer',
		'altitud'=>'required|integer',
		'pais_id'=>'required|integer',
	);
}