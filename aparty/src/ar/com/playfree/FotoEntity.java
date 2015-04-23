package ar.com.playfree;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FotoEntity implements Serializable {

	String URL = "";
	String subidaPor = "";
	String cantLikes = "";
	boolean meGusta;
	String idCategoria = "";
	String id = "";
	String mac = "";

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getCantLikes() {
		return cantLikes;
	}

	public void setCantLikes(String cantLikes) {
		this.cantLikes = cantLikes;
	}

	public String getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isMeGusta() {
		return meGusta;
	}

	public void setMeGusta(boolean meGusta) {
		this.meGusta = meGusta;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getSubidaPor() {
		return subidaPor;
	}

	public void setSubidaPor(String subidaPor) {
		this.subidaPor = subidaPor;
	}
}
