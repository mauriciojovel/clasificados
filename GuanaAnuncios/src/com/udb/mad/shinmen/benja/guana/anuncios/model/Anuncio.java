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
	private String categoriaId;
	private String precio;
	private String telefono;
	private String correo;
	private String usuario;
	private double latitud;
	private double altitud;
	
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
	public String getCategoriaId() {
		return categoriaId;
	}
	public void setCategoriaId(String categoriaId) {
		this.categoriaId = categoriaId;
	}
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getAltitud() {
		return altitud;
	}
	public void setAltitud(double altitud) {
		this.altitud = altitud;
	}
	
}
