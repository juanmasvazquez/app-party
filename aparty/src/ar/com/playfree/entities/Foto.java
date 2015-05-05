package ar.com.playfree.entities;

import java.io.Serializable;

public class Foto implements Serializable{
 
	private Long id;
	private Long idCategoria;
	private String mac;
	private String usuario;
	private String url;
	private String urlThumb;
	public String getUrlThumb() {
		return urlThumb;
	}

	public void setUrlThumb(String urlThumb) {
		this.urlThumb = urlThumb;
	}

	private int cantLikes;
	private boolean like;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Long idCategoria) {
		this.idCategoria = idCategoria;
	}

	public int getCantLikes() {
		return cantLikes;
	}

	public void setCantLikes(int cantLikes) {
		this.cantLikes = cantLikes;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

}
