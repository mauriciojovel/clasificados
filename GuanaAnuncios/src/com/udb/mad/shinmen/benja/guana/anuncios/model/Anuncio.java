package com.udb.mad.shinmen.benja.guana.anuncios.model;

import java.io.Serializable;

public class Anuncio implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3002937859974957126L;
	private String codigoAnuncio;
	private String tituloAnuncio;
	private String descripcionAnuncio;
	
	public String getCodigoAnuncio() {
		return codigoAnuncio;
	}
	public void setCodigoAnuncio(String codigoAnuncio) {
		this.codigoAnuncio = codigoAnuncio;
	}
	public String getTituloAnuncio() {
		return tituloAnuncio;
	}
	public void setTituloAnuncio(String tituloAnuncio) {
		this.tituloAnuncio = tituloAnuncio;
	}
	public String getDescripcionAnuncio() {
		return descripcionAnuncio;
	}
	public void setDescripcionAnuncio(String descripcionAnuncio) {
		this.descripcionAnuncio = descripcionAnuncio;
	}
	
	

}
