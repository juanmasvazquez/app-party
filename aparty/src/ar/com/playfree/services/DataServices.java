package ar.com.playfree.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import ar.com.playfree.entities.Categoria;
import ar.com.playfree.entities.Evento;
import ar.com.playfree.entities.Foto;
import ar.com.playfree.exceptions.ServiceException;
import ar.com.playfree.utils.AppUtils;

public class DataServices {

	public Evento connectTo(String codigoEvento) throws ServiceException {
		String service = AppUtils.getJoinService(codigoEvento);
		try {
			JSONObject response = getJSONResponse(service).getJSONObject(
					"response");
			JSONObject data = response.getJSONObject("data");
			boolean success = response.getBoolean("success");
			String errorMsj = response.getString("errorMessage");

			if (success) {
				Evento evento = new Evento();
				evento.setCodigo(data.getString("codigo"));
				evento.setId(data.getLong("id"));
				evento.setNombre(data.getString("nombre"));
				evento.setCategorias(new ArrayList<Categoria>());

				JSONArray categorias = data.getJSONArray("categorias");
				Categoria categoria = null;
				for (int i = 0; i < categorias.length(); i++) {
					categoria = new Categoria();
					categoria.setId(categorias.getJSONObject(i).getLong("id"));
					categoria.setNombre(categorias.getJSONObject(i).getString(
							"nombre"));
					evento.getCategorias().add(categoria);
				}
				return evento;
			} else {
				throw new ServiceException(errorMsj);
			}

		} catch (JSONException e) {
			throw new ServiceException("Error interno");
		}
	}

	public List<Foto> getFotos(Context mContext) throws ServiceException {
		String service = AppUtils.getPhotosService(mContext);
		try {
			JSONObject response = getJSONResponse(service);
			response = response.getJSONObject("response");
			JSONArray data = response.getJSONArray("data");
			boolean success = response.getBoolean("success");
			String errorMsj = response.getString("errorMessage");

			if (success) {
				List<Foto> fotos = new ArrayList<Foto>();
				for (int i = 0; i < data.length(); i++) {
					Foto foto = new Foto();
					foto.setCantLikes(data.getJSONObject(i).getInt("cantLikes"));
					foto.setId(data.getJSONObject(i).getLong("id"));
					foto.setIdCategoria(data.getJSONObject(i).getLong(
							"idCategoria"));
					foto.setLike(data.getJSONObject(i).getBoolean("like"));
					foto.setMac(data.getJSONObject(i).getString("mac"));
					foto.setUrl(data.getJSONObject(i).getString("url"));
					foto.setUrlThumb(data.getJSONObject(i)
							.getString("urlThumb"));
					foto.setUsuario(data.getJSONObject(i).getString("usuario"));
					fotos.add(foto);
				}
				return fotos;
			} else {
				throw new ServiceException(errorMsj);
			}
		} catch (JSONException e) {
			throw new ServiceException("Error interno");
		}
	}

	public Foto getFoto(Long idFoto, Context mContext) throws ServiceException {
		String service = AppUtils.getPhotoService(idFoto, mContext);
		try {
			JSONObject response = getJSONResponse(service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Categoria> getCategorias(Context mContext)
			throws ServiceException {
		String service = AppUtils.getCategoriaService(mContext);
		try {
			JSONObject response = getJSONResponse(service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Foto> getFotosCategoria(Long idCategoria, Context mContext)
			throws ServiceException {
		String service = AppUtils.getPhotosCategoriaService(idCategoria,
				mContext);
		try {
			JSONObject response = getJSONResponse(service);
			response = response.getJSONObject("response");
			JSONArray data = response.getJSONArray("data");
			boolean success = response.getBoolean("success");
			String errorMsj = response.getString("errorMessage");

			if (success) {
				List<Foto> fotos = new ArrayList<Foto>();
				for (int i = 0; i < data.length(); i++) {
					Foto foto = new Foto();
					foto.setCantLikes(data.getJSONObject(i).getInt("cantLikes"));
					foto.setId(data.getJSONObject(i).getLong("id"));
					foto.setIdCategoria(data.getJSONObject(i).getLong(
							"idCategoria"));
					foto.setLike(data.getJSONObject(i).getBoolean("like"));
					foto.setMac(data.getJSONObject(i).getString("mac"));
					foto.setUrl(data.getJSONObject(i).getString("url"));
					foto.setUrlThumb(data.getJSONObject(i)
							.getString("urlThumb"));
					foto.setUsuario(data.getJSONObject(i).getString("usuario"));
					fotos.add(foto);
				}
				return fotos;
			} else {
				throw new ServiceException(errorMsj);
			}
		} catch (JSONException e) {
			throw new ServiceException("Error interno");
		}
	}

	public Foto pushFoto(Long idCategoria, InputStream foto, Context mContext)
			throws ServiceException {
		String service = AppUtils.getPushService(idCategoria, mContext);
		getJSONResponse(service, foto);
		return null;
	}

	public Foto sendLikeFoto(Long idFoto, Context mContext)
			throws ServiceException {
		String service = AppUtils.getLikeService(idFoto, mContext);
		try {
			JSONObject response = getJSONResponse(service);
			response = response.getJSONObject("response");
			JSONObject data = response.getJSONObject("data");
			boolean success = response.getBoolean("success");
			String errorMsj = response.getString("errorMessage");
			if (success) {
				Foto foto = new Foto();
				foto.setCantLikes(data.getInt("cantLikes"));
				foto.setId(data.getLong("id"));
				foto.setIdCategoria(data.getLong("idCategoria"));
				foto.setLike(data.getBoolean("like"));
				foto.setMac(data.getString("mac"));
				foto.setUrl(data.getString("url"));
				foto.setUrlThumb(data.getString("urlThumb"));
				foto.setUsuario(data.getString("usuario"));
				return foto;
			} else {
				throw new ServiceException(errorMsj);
			}
		} catch (JSONException e) {
			throw new ServiceException("Error interno");
		}
	}

	private static JSONObject getJSONResponse(String service)
			throws ServiceException {
		try {
			return getJSONResponse(service, null);
		} catch (Exception e) {
			throw new ServiceException("No se pudo conectar con los Servicios");
		}
	}

	private static JSONObject getJSONResponse(String service, InputStream in)
			throws ServiceException {
		JSONObject response = null;
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient(
					new BasicHttpParams());

			HttpUriRequest request = null;
			if (null != in) {
				HttpPost httppost = new HttpPost(service);
				httppost.setHeader("Content-type",
						"application/json; charset=iso-8859-1");
				InputStreamEntity reqEntity = new InputStreamEntity(in, -1);
				reqEntity.setContentType("binary/octet-stream");
				reqEntity.setChunked(true); // Send in multiple parts if needed
				httppost.setEntity(reqEntity);
				request = httppost;
			} else {
				HttpGet httpget = new HttpGet(service);
				httpget.setHeader("Content-type",
						"application/json; charset=iso-8859-1");
				request = httpget;
			}

			HttpResponse httpResponse = httpclient.execute(request);
			HttpEntity entity = httpResponse.getEntity();

			InputStream inputStream = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			response = new JSONObject(sb.toString());
		} catch (Exception e) {
			throw new ServiceException("No se pudo conectar con los Servicios");
		}
		return response;
	}

}
