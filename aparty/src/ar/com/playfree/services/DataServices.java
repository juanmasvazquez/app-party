package ar.com.playfree.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
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
				return evento;
			} else {
				throw new ServiceException(errorMsj);
			}

		} catch (Exception e) {
			throw new ServiceException("Error al conectar con servicios");
		}
	}

	public List<Foto> getFotos(Context mContext) {
		String service = AppUtils.getPhotosService(mContext);
		try {
			JSONObject response = getJSONResponse(service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Foto getFoto(Long idFoto, Context mContext) {
		String service = AppUtils.getPhotoService(idFoto, mContext);
		try {
			JSONObject response = getJSONResponse(service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Categoria> getCategorias(Context mContext) {
		String service = AppUtils.getCategoriaService(mContext);
		try {
			JSONObject response = getJSONResponse(service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Foto> getFotosCategoria(Long idCategoria, Context mContext) {
		String service = AppUtils.getPhotosCategoriaService(idCategoria,
				mContext);
		try {
			JSONObject response = getJSONResponse(service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Foto pushFoto(Long idCategoria, InputStream foto, Context mContext) {
		String service = AppUtils.getPushService(idCategoria, mContext);
		try {
			JSONObject response = getJSONResponse(service, foto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Foto sendLikeFoto(Long idFoto, Context mContext) {
		String service = AppUtils.getLikeService(idFoto, mContext);
		try {
			JSONObject response = getJSONResponse(service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static JSONObject getJSONResponse(String service) throws Exception {
		return getJSONResponse(service, null);
	}

	private static JSONObject getJSONResponse(String service, InputStream in)
			throws Exception {
		JSONObject response = null;
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpPost httppost = new HttpPost(service);
			httppost.setHeader("Content-type",
					"application/json; charset=iso-8859-1");

			if (null != in) {
				InputStreamEntity reqEntity = new InputStreamEntity(in, -1);
				reqEntity.setContentType("binary/octet-stream");
				reqEntity.setChunked(true); // Send in multiple parts if needed
				httppost.setEntity(reqEntity);
			}

			HttpResponse httpResponse = httpclient.execute(httppost);
			HttpEntity entity = httpResponse.getEntity();

			InputStream inputStream = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			response = new JSONObject(sb.toString());
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
		return response;
	}

}
