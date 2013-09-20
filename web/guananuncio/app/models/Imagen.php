<?php
class Imagen extends ValidatorEloquent {
	protected $table = 'imagen';

	public $timestamps = false;

	protected $fillable = array('id', 'imagen','anuncio_id');

	protected $rules = array(
		'imagen'=>'required',
		'anuncio_id'=>'required|integer|min:1'
	);
}