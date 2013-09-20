package com.udb.mad.shinmen.benja.guana.anuncios.model;

import java.io.Serializable;

public class Categoria implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6418822537785903208L;
	private String codigoCategoria;
	private String nombreCategoria;
	
	public Categoria() {
		super();
	}
	
	public Categoria(String codigoCategoria, String nombreCategoria) {
		super();
		this.codigoCategoria = codigoCategoria;
		this.nombreCategoria = nombreCategoria;
	}
	
	public String getCodigoCategoria() {
		return codigoCategoria;
	}
	public void setCodigoCategoria(String codigoCategoria) {
		this.codigoCategoria = codigoCategoria;
	}
	public String getNombreCategoria() {
		return nombreCategoria;
	}
	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	@Override
	public String toString() {
		return this.nombreCategoria;
	}
}