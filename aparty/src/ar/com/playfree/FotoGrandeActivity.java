package ar.com.playfree;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.playfree.entities.Foto;
import ar.com.playfree.views.TouchImageView;

import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class FotoGrandeActivity extends Activity {

	ImageButton imageButton;
	Button botonLike;
	Button botonDownload;
	String msg = "Te gusta esta foto!";
	Foto fotoElegida = new Foto();
	ProgressDialog progressDialog;
	String appName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		Context context = getApplicationContext();
		appName = context.getString(context.getApplicationInfo().labelRes);
		// ImageView imageView = (ImageView) findViewById(R.id.imageView);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		TouchImageView imageView = (TouchImageView) findViewById(R.id.imageView);

		int position = getIntent().getIntExtra("position", -1);
		final Foto foto = (Foto) getIntent().getSerializableExtra("foto");
		fotoElegida = foto;
		if (position != -1) {
			Picasso.with(FotoGrandeActivity.this).load(foto.getUrl())
					.placeholder(R.raw.place_holder).resize(800, 800)
					.centerInside().error(R.raw.big_problem).into(imageView);
		} else {
			Picasso.with(FotoGrandeActivity.this).load(R.raw.big_problem)
					.resize(800, 800).centerCrop().into(imageView);
		}

		TextView cantLikes = (TextView) findViewById(R.id.cantLikes);
		cantLikes.setText(String.valueOf(foto.getCantLikes()));

		TextView subidaPor = (TextView) findViewById(R.id.subidaPor);
		subidaPor.setText(foto.getUsuario());

		botonLike = (Button) findViewById(R.id.botonlike);
		if (foto.isLike()) {
			botonLike.setText("Ya no me gusta");
			foto.setLike(false);
		} else {
			botonLike.setText("Me gusta");
			foto.setLike(true);
		}
		botonLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (foto.isLike()) {
					botonLike.setText("Ya no me gusta");
					foto.setLike(false);
				} else {
					botonLike.setText("Me gusta");
					foto.setLike(true);
				}

				// Toast.makeText(FotoGrandeActivity.this, msg,
				// Toast.LENGTH_SHORT).show();
				// Intent verFotosIntent = new Intent(FotoGrandeActivity.this,
				// VerFotosActivity.class);
				// startActivity(verFotosIntent);

			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

	
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			startActivityAfterCleanup(VerFotosActivity.class);
			return true;
		case R.id.bajarFoto:
			if (isDownloadManagerAvailable(getApplicationContext())) {
				String url = fotoElegida.getUrl();
				DownloadManager.Request request = new DownloadManager.Request(
						Uri.parse(url));
				request.setDescription(appName + '-' + String.valueOf(fotoElegida.getId()));
				request.setTitle(appName + '-' + String.valueOf(fotoElegida.getId()) + ".jpg");
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					request.allowScanningByMediaScanner();
					request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				}
				request.setDestinationInExternalPublicDir(
						Environment.DIRECTORY_DOWNLOADS, appName + '-'
								+ fotoElegida.getId() + ".jpg");
				DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				manager.enqueue(request);
			} else {
				doDownload(fotoElegida.getUrl(), appName + '-' + fotoElegida.getId() + ".jpg");
			}
		}
		return true;
	}

	protected void doDownload(final String urlLink, final String fileName) {
		Thread dx = new Thread() {			
			public void run() {				
				File root = android.os.Environment.getExternalStorageDirectory();
				File dir = new File(root.getAbsolutePath() + "/Download/");
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
					
					Toast.makeText(getApplicationContext(), "Descargando Imagen", Toast.LENGTH_LONG).show();
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
		Toast.makeText(getApplicationContext(), "Imagen Descargada", Toast.LENGTH_LONG).show();

	}

	private void startActivityAfterCleanup(Class<?> cls) {
		// if (projectsDao != null) projectsDao.close();
		Intent intent = new Intent(getApplicationContext(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
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

}
