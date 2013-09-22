<?php
class Token extends ValidatorEloquent {
	protected $table = 'token';
	public $timestamps = false;
	protected $fillable = array('dispositivo');

	protected $rules = array(
		'disposivito'=>'required'
	);
}