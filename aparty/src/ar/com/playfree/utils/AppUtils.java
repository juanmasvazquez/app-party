package ar.com.playfree.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class AppUtils {

	private static final String SERVER_URL = "http://172.16.19.191:8080/playfree/service/service!";
	private static final String PUSH_SERVICE = "pushPhoto.action";
	private static final String PHOTO_SERVICE = "getPhoto.action";
	private static final String PHOTOS_SERVICE = "getPhotos.action";
	private static final String JOIN_SERVICE = "connectTo.action";
	private static final String LIKE_SERVICE = "sendLikePhoto.action";
	private static final String CATEGORIA_SERVICE = "getCategorias.action";
	private static final String PHOTOS_CATEGORIA_SERVICE = "getPhotosCategoria.action";

	public static String getMac(Context mContext) {
		WifiManager manager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		return info.getMacAddress();
	}

	public static String getUsername(Context mContext) {
		AccountManager manager = (AccountManager) mContext
				.getSystemService(Context.ACCOUNT_SERVICE);
		Account[] accounts = manager.getAccountsByType("com.google");
		return accounts[0].name;
	}

	public static String getPushService(Long idCategoria, Context mContext) {
		Long idEvento = 1L;
		return SERVER_URL + PUSH_SERVICE + "?evId=" + idEvento + "&mac="
				+ getMac(mContext) + "&usr=" + getUsername(mContext)
				+ "&catId=" + idCategoria;
	}

	public static String getPhotosService(Context mContext) {
		Long idEvento = 1L;
		return SERVER_URL + PHOTOS_SERVICE + "?evId=" + idEvento + "&mac="
				+ getMac(mContext) + "&usr=" + getUsername(mContext);
	}

	public static String getPhotoService(Long idFoto, Context mContext) {
		return SERVER_URL + PHOTO_SERVICE + "?fotoId=" + idFoto + "&mac="
				+ getMac(mContext) + "&usr=" + getUsername(mContext);
	}

	public static String getJoinService(String codigoEvento) {
		return SERVER_URL + JOIN_SERVICE + "?evCodigo=" + codigoEvento;
	}

	public static String getLikeService(Long idFoto, Context mContext) {
		return SERVER_URL + LIKE_SERVICE + "?fotoId=" + idFoto + "&mac="
				+ getMac(mContext) + "&usr=" + getUsername(mContext);
	}

	public static String getCategoriaService(Context mContext) {
		Long idEvento = 1L;
		return SERVER_URL + CATEGORIA_SERVICE + "?evId=" + idEvento;
	}

	public static String getPhotosCategoriaService(Long idCategoria,
			Context mContext) {
		Long idEvento = 1L;
		return SERVER_URL + PHOTOS_CATEGORIA_SERVICE + "?evId=" + idEvento
				+ "&mac=" + getMac(mContext) + "&usr=" + getUsername(mContext)
				+ "&catId=" + idCategoria;
	}
}
