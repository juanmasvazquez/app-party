package ar.com.playfree.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import ar.com.playfree.entities.Categoria;
import ar.com.playfree.entities.Evento;
import ar.com.playfree.entities.Foto;
import ar.com.playfree.exceptions.ServiceException;

public class DataServicesDummy {

	private static List<Foto> fotos = new ArrayList<Foto>();

	public DataServicesDummy() {
		Foto foto = null;
		for (int i = 0; i < 10; i++) {
			foto = new Foto();
			foto.setId((long) i + 1);
			foto.setCantLikes(i + 1);
			foto.setLike((i % 0) == 0);
			foto.setIdCategoria(((i % 0) == 0) ? 1L : 2L);
			foto.setMac("A1-A2-A3-A4");
			foto.setUrl("http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png");
			foto.setUsuario("andy.suarez@gmail.com");
			fotos.add(foto);
		}
	}

	public Evento connectTo(String codigoEvento) throws ServiceException {
		Evento evento = new Evento();
		evento.setCodigo(codigoEvento);
		evento.setId(1L);
		evento.setNombre("EVENTO DUMMY");
		return evento;
	}

	public List<Foto> getFotos(Context mContext) {
		List<Foto> fotos = new ArrayList<Foto>();
		Foto foto = null;
		for (int i = 0; i < 10; i++) {
			foto = new Foto();
			foto.setId((long) i + 1);
			foto.setCantLikes(i + 1);
			foto.setLike((i % 0) == 0);
			foto.setIdCategoria(1L);
			foto.setMac("A1-A2-A3-A4");
			foto.setUrl("http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png");
			foto.setUsuario("andy.suarez@gmail.com");
			fotos.add(foto);
		}
		return fotos;
	}

	public Foto getFoto(Long idFoto, Context mContext) {
		Foto foto = new Foto();
		foto.setId(1L);
		foto.setCantLikes(10);
		foto.setLike(true);
		foto.setIdCategoria(1L);
		foto.setMac("A1-A2-A3-A4");
		foto.setUrl("http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png");
		foto.setUsuario("andy.suarez@gmail.com");
		return foto;
	}

	public List<Categoria> getCategorias(Context mContext) {
		List<Categoria> categorias = new ArrayList<Categoria>();
		Categoria categoria = null;
		for (int i = 0; i < 2; i++) {
			categoria = new Categoria();
			categoria.setId((long) i + 1);
			categoria.setNombre("CATEGORIA " + (i + 1));
			categorias.add(categoria);
		}
		return categorias;
	}

	public List<Foto> getFotosCategoria(Long idCategoria, Context mContext) {
		List<Foto> fotosCategorias = new ArrayList<Foto>();
		for (Foto foto : fotos) {
			if (foto.getIdCategoria().equals(idCategoria)) {
				fotosCategorias.add(foto);
			}
		}
		return fotosCategorias;
	}

	public Foto pushFoto(Long idCategoria, InputStream foto, Context mContext) {
		return null;
	}

	public Foto sendLikeFoto(Long idFoto, Context mContext) {
		Foto fotoLike = null;
		for (Foto foto : fotos) {
			if (foto.getId().equals(idFoto)) {
				foto.setCantLikes(foto.getCantLikes() + 1);
				fotoLike = foto;
				break;
			}
		}
		return fotoLike;
	}

}