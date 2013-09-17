<?php
class PaisController extends AutheticatedController {

	public function getIndex()
    {
        $paises = Pais::orderBy('id', 'asc')->get();
        return Response::json($paises);
    }
}