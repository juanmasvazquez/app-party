package ar.com.playfree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int TAKE_PHOTO_CODE = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		// Font path
        String fontPath = "fonts/Roboto-Bold.ttf";
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set textview font
		TextView textCapturar = (TextView) findViewById(R.id.textViewCapturar);
		textCapturar.setTypeface(tf);
		// Boton Capturar Foto --------------------
		Button btnCaptura = (Button) findViewById(R.id.btnCapture);
		btnCaptura.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String dir = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
						+ "/app-party/";
				File newdir = new File(dir);
				if (!newdir.exists()) {
					newdir.mkdirs();
				}

				String file = dir + System.currentTimeMillis() + ".jpg";
				File newfile = new File(file);
				try {
					newfile.createNewFile();
				} catch (IOException e) {
				}

				Uri outputFileUri = Uri.fromFile(newfile);

				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				cameraIntent.putExtra("file", newfile);

				startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
			}
		});

		// Set textview font
		TextView textAlbum = (TextView) findViewById(R.id.textViewAlbum);
		textAlbum.setTypeface(tf);
		// Boton Ver Album --------------------
		Button btnVerAlbum = (Button) findViewById(R.id.btnVerAlbum);
		btnVerAlbum.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent verFotosIntent = new Intent(MainActivity.this,
						VerFotosActivity.class);
				startActivity(verFotosIntent);
			}
		});

		// Set textview font
		TextView textUnirse = (TextView) findViewById(R.id.textViewUnirse);
		textUnirse.setTypeface(tf);
		// Boton unir Evento --------------------
		Button btnUnirEvento = (Button) findViewById(R.id.btnUnirEvento);
		btnUnirEvento.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent unirEventoIntent = new Intent(MainActivity.this,
						UnirEventoActivity.class);
				startActivity(unirEventoIntent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == TAKE_PHOTO_CODE) {
				File file = (File) data.getExtras().get("file");
				try {
					sendPhoto(file);
				} catch (Exception e) {
				}
			}
		}
	}

	// Logica para Captura de Foto
	// -------------------------------------------------------------------------

	private void sendPhoto(File file) throws Exception {
		new UploadTask().execute(file);
	}

	private class UploadTask extends AsyncTask<File, Void, Void> {

		protected Void doInBackground(File... files) {
			if (files[0] == null)
				return null;
			setProgress(0);

			DefaultHttpClient httpclient = new DefaultHttpClient();
			try {

				WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				WifiInfo info = manager.getConnectionInfo();
				String address = info.getMacAddress();

				HttpPost httppost = new HttpPost(
						"http://172.16.19.52:8080/playfree/foto/adm.action?mac="
								+ address); // server

				InputStreamEntity reqEntity = new InputStreamEntity(
						new FileInputStream(files[0]), -1);
				reqEntity.setContentType("binary/octet-stream");
				reqEntity.setChunked(true); // Send in multiple parts if needed
				httppost.setEntity(reqEntity);
				HttpResponse response = httpclient.execute(httppost);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// Toast.makeText(CameraActivity.this, R.string.uploaded,
			// Toast.LENGTH_LONG).show();
		}
	}
}