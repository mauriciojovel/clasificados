<?php
class ValidatorEloquent extends Eloquent
{
    protected $rules = array();

    protected $errors;

    protected $validation;

    protected $messages = array(
        'required' => ':attribute es necesario.',
        'same'    => 'El campo :attribute y :other deben de ser iguales.',
        'size'    => 'El campo :attribute deberia de contener :size exactamente.',
        'between' => 'El campo :attribute deberia de estar entre :min - :max.',
        'in'      => 'El campo :attribute deberia de ser uno de los siguientes valores: :values',
        'min'     => 'El campo :attribute debe de tener como minimo :min caracteres',
        'max'     => 'El campo :attribute debe de tener como maximo :max',
    );

    public function validate($data,$rules2=array())
    {
        if(count($rules2)==0) {
            $rules3 = $this->rules;
        }else {
            $rules3 = $rules2;
        }
        // make a new validator object
        $v = Validator::make($data, $rules3, $this->messages);

        // check for failure
        if ($v->fails())
        {
            // set errors and return false
            $this->errors = $v->messages();
            $this->validation = $v;
            return false;
        }

        // validation pass
        return true;
    }

    public function errors()
    {
        return $this->errors;
    }

    public function validator() {
        return $this->validation;
    }

    public function rules() {
        return $this->rules;
    }
}