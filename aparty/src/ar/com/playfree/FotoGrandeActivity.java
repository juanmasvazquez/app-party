package ar.com.playfree;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.playfree.entities.Foto;
import ar.com.playfree.exceptions.ServiceException;
import ar.com.playfree.services.DataServices;
import ar.com.playfree.views.TouchImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class FotoGrandeActivity extends Activity {

	Button botonLike;
	Button botonDownload;
	ProgressDialog progressDialog;
	String appName = "";
	TouchImageView imageView = null;
	ProgressDialog pd;
	Context context;
	String storeDir;
	Foto foto = new Foto();
	TextView cantLikes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Setear la fuente a utilizar en el mainActivity
		String fontPath = "fonts/Roboto-Thin.ttf";

		// Cambiar la fuente del actionbar
		int actionBarTitle = Resources.getSystem().getIdentifier(
				"action_bar_title", "id", "android");
		TextView actionBarTitleView = (TextView) getWindow().findViewById(
				actionBarTitle);
		Typeface forte = Typeface.createFromAsset(getAssets(), fontPath);
		if (actionBarTitleView != null) {
			actionBarTitleView.setTypeface(forte);
			actionBarTitleView.setTextSize(20);
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		context = getApplicationContext();
		appName = context.getString(context.getApplicationInfo().labelRes);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		imageView = (TouchImageView) findViewById(R.id.imageView);

		final int position = getIntent().getIntExtra("position", -1);
		foto = (Foto) getIntent().getSerializableExtra("foto");
		if (position != -1) {
			ImageLoader imageLoader = ImageLoader.getInstance();
			DisplayImageOptions options = new DisplayImageOptions.Builder()
							.cacheInMemory(true)
							.resetViewBeforeLoading(true)
							.showImageOnLoading(R.drawable.ic_loading)
							.showImageForEmptyUri(R.drawable.ic_error)
							.showImageOnFail(R.drawable.ic_error)
							.cacheInMemory(true)
							.build();

			//download and display image from url
			imageLoader.displayImage(foto.getUrl(), imageView, options);
		} else {
			Picasso.with(FotoGrandeActivity.this).load(R.raw.big_problem)
					.resize(800, 800).centerCrop().into(imageView);
		}

		TextView subidaPor = (TextView) findViewById(R.id.subidaPor);
		subidaPor.setText(limpiarUsuario(foto.getUsuario()));

		cantLikes = (TextView) findViewById(R.id.cantLikes);

		botonLike = (Button) findViewById(R.id.botonlike);
		toggleLikeBoton(foto);
		botonLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new SendLike().execute(foto.getId());
			}
		});
	}

	private class SendLike extends AsyncTask<Long, Integer, Void> {

		@Override
		protected Void doInBackground(Long... params) {

			try {
				foto = new DataServices().sendLikeFoto(params[0],
						getApplicationContext());
			} catch (final ServiceException sexc) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(FotoGrandeActivity.this,
								sexc.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
			toggleLikeBoton(foto);
			return null;
		}

	}

	private CharSequence limpiarUsuario(String usuario) {
		return usuario.substring(0, usuario.indexOf("@"));
	}

	private void toggleLikeBoton(Foto foto) {
		if (foto.isLike()) {
			cambiarBotonLikeTexto(botonLike, R.string.nomeGusta);
			foto.setLike(false);
		} else {
			cambiarBotonLikeTexto(botonLike, R.string.meGusta);
			foto.setLike(true);
		}
		if (foto.getCantLikes() != 0) {
			cambiarCantLikesTexto(cantLikes,
					String.valueOf(foto.getCantLikes()) + " Me gusta");
		}
	}

	private void cambiarCantLikesTexto(final TextView cantLikes,
			final String texto) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				cantLikes.setText(texto);
			}
		});
	}

	private void cambiarBotonLikeTexto(Button boton, final int texto) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				botonLike.setText(texto);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

		switch (menuItem.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.bajarFoto:
			Toast.makeText(getApplicationContext(), "Descargando Imagen...",
					Toast.LENGTH_LONG).show();
			if (isDownloadManagerAvailable(getApplicationContext())) {
				String url = foto.getUrl();
				DownloadManager.Request request = new DownloadManager.Request(
						Uri.parse(url));
				request.setDescription(appName + '-'
						+ String.valueOf(foto.getId()));
				request.setTitle(appName + '-' + String.valueOf(foto.getId())
						+ ".jpg");
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					request.allowScanningByMediaScanner();
					request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				}
				request.setDestinationInExternalPublicDir(
						Environment.DIRECTORY_DOWNLOADS,
						appName + '-' + foto.getId() + ".jpg");
				DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				manager.enqueue(request);
			} else {
				doDownload(foto.getUrl(), appName + '-' + foto.getId() + ".jpg");
			}
		}
		return true;
	}

	protected void doDownload(final String urlLink, final String fileName) {
		Thread dx = new Thread() {
			public void run() {
				File root = android.os.Environment
						.getExternalStorageDirectory();
				// File dir = new File(root.getAbsolutePath() + "/Download/");
				File dir = new File(Environment.DIRECTORY_DOWNLOADS);
				if (dir.exists() == false) {
					dir.mkdirs();
				}
				// Save the path as a string value

				try {
					URL url = new URL(urlLink);
					Log.i("FILE_NAME", "File name is " + fileName);
					Log.i("FILE_URLLINK", "File URL is " + url);
					URLConnection connection = url.openConnection();
					connection.connect();
					// this will be useful so that you can show a typical 0-100%
					// progress bar
					int fileLength = connection.getContentLength();

					// download the file

					Toast.makeText(getApplicationContext(),
							"Descargando Imagen", Toast.LENGTH_LONG).show();
					InputStream input = new BufferedInputStream(
							url.openStream());
					OutputStream output = new FileOutputStream(dir + "/"
							+ fileName);

					byte data[] = new byte[1024];
					long total = 0;
					int count;
					while ((count = input.read(data)) != -1) {
						total += count;

						output.write(data, 0, count);
					}

					output.flush();
					output.close();
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("ERROR ON DOWNLOADING FILES", "ERROR IS" + e);
				}
			}
		};
		dx.start();
		Toast.makeText(getApplicationContext(), "Imagen Descargada",
				Toast.LENGTH_LONG).show();

	}

	private void startActivityAfterCleanup(Class<?> cls) {
		// if (projectsDao != null) projectsDao.close();
		Intent intent = new Intent(getApplicationContext(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_foto_grande, menu);
		return true;
	}

	/**
	 * @param context
	 *            used to check the device version and DownloadManager
	 *            information
	 * @return true if the download manager is available
	 */
	public static boolean isDownloadManagerAvailable(Context context) {
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				return false;
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.downloads.ui",
					"com.android.providers.downloads.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager()
					.queryIntentActivities(intent,
							PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public void downloadFile() {
		storeDir = createStoreDir();
		if (storeDir != null) {
			try {

				BackTask bt = new BackTask();
				bt.execute(foto.getUrl());

			} catch (Exception e) {
			}
		} else {
			Toast.makeText(this, "Folder can't be create. Download failed.",
					Toast.LENGTH_LONG).show();
		}
	}

	public String createStoreDir() {
		String storeDir = Environment.getExternalStorageDirectory()
				+ "/edoc_download";
		File f = new File(storeDir);
		if (!f.exists())
			if (!f.mkdir()) {
				Log.e("Error", "Can't create edoc_download directory");
				return null;
			} else
				return storeDir;
		else
			return storeDir;
	}

	// background process to download the file from server
	private class BackTask extends AsyncTask<String, Integer, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setTitle("Downloading the file to " + storeDir);
			pd.setMessage("Please wait.");
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMax(100);
			pd.setCancelable(true);
			pd.setIndeterminate(false);
			pd.show();

		}

		protected Void doInBackground(String... params) {
			URL url;
			int count;
			try {

				url = new URL(params[0]);
				String outpath = "";
				try {

					File f = new File(storeDir);
					if (f.exists()) {
						HttpURLConnection con = (HttpURLConnection) url
								.openConnection();
						InputStream is = con.getInputStream();
						String fullpath = url.getPath();
						String filename = fullpath.substring(fullpath
								.lastIndexOf('/') + 1);
						outpath = storeDir + "/" + filename;
						FileOutputStream fos = new FileOutputStream(outpath);
						int lenghtOfFile = con.getContentLength();
						byte data[] = new byte[1024];
						long total = 0;
						while ((count = is.read(data)) != -1) {
							total += count;
							// publishing the progress
							publishProgress((int) ((total * 100) / lenghtOfFile));
							// writing data to output file
							fos.write(data, 0, count);
						}

						is.close();
						fos.flush();
						fos.close();
					} else {
						Log.e("Error", "Not found: " + storeDir);

					}

				} catch (IOException e) {
					e.printStackTrace();
				}

				// //

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		protected void onProgressUpdate(Integer... progress) {
			// setting progress percentage
			pd.setProgress(progress[0]);
		}

		protected void onPostExecute(Void result) {

			pd.dismiss();

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		// startActivityAfterCleanup(VerFotosActivity.class);
	}
}
