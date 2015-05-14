package ar.com.playfree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.playfree.entities.Categoria;
import ar.com.playfree.entities.Evento;
import ar.com.playfree.exceptions.ServiceException;
import ar.com.playfree.services.DataServices;
import ar.com.playfree.utils.TransparentProgressDialog;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "Party-App-Prefs";
	private static final int TAKE_PHOTO_CODE = 0;
	private File fileSelect;
	private Evento evento;
	private CharSequence[] categoriasOpciones;

	public static MainActivity instance;

	// Campos

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Setear la fuente a utilizar en el mainActivity
		String fontPath = "fonts/Roboto-Thin.ttf";
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

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

		// Set textview font
		TextView textCapturar = (TextView) findViewById(R.id.textViewCapturar);
		textCapturar.setTypeface(tf);
		// Boton Capturar Foto --------------------
		Button btnCaptura = (Button) findViewById(R.id.btnCapture);
		btnCaptura.setTypeface(tf);
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
				fileSelect = newfile;

				startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
			}
		});

		// Set textview font
		TextView textAlbum = (TextView) findViewById(R.id.textViewAlbum);
		textAlbum.setTypeface(tf);
		// Boton Ver Album --------------------
		Button btnVerAlbum = (Button) findViewById(R.id.btnVerAlbum);
		btnVerAlbum.setTypeface(tf);
		btnVerAlbum.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent verFotosIntent = new Intent(MainActivity.this,
						VerFotosActivity.class);
				startActivity(verFotosIntent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		// Set textview font
		TextView textUnirse = (TextView) findViewById(R.id.textViewUnirse);
		textUnirse.setTypeface(tf);
		// Boton unir Evento --------------------
		Button btnUnirEvento = (Button) findViewById(R.id.btnUnirEvento);
		btnUnirEvento.setTypeface(tf);
		btnUnirEvento.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent unirEventoIntent = new Intent(MainActivity.this,
						UnirEventoActivity.class);
				startActivity(unirEventoIntent);
			}
		});

		TextView textConectar = (TextView) findViewById(R.id.textConectar);
		textConectar.setTypeface(tf);
		Button btnConectar = (Button) findViewById(R.id.btnConectar);
		btnConectar.setTypeface(tf);
		btnConectar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				loadEvento();
			}
		});

		loadEvento();

		instance = this;
	}

	private void loadEvento() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String codigo = settings.getString("EVENTO_CODIGO", "");
		if (!codigo.isEmpty()) {
			getEvento(codigo);
		} else {
			TextView textUnirse = (TextView) findViewById(R.id.textViewUnirse);
			Button btnUnirEvento = (Button) findViewById(R.id.btnUnirEvento);
			textUnirse.setVisibility(TextView.VISIBLE);
			btnUnirEvento.setVisibility(Button.VISIBLE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == TAKE_PHOTO_CODE) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this,
						AlertDialog.THEME_DEVICE_DEFAULT_DARK);
				builder.setCancelable(Boolean.FALSE);
				builder.setTitle(R.string.seleccion_categoria).setItems(
						categoriasOpciones,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									final int which) {

								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Categoria categoria = evento
												.getCategorias().get(which);
										try {
											sendPhoto(categoria.getId(),
													fileSelect);
										} catch (Exception e) {
										}
									}
								});
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
	}

	// Logica para Captura de Foto
	// -------------------------------------------------------------------------

	private void sendPhoto(Long idCategoria, File file) throws Exception {
		new CameraTask(idCategoria).execute(file);
	}

	private class CameraTask extends AsyncTask<File, Void, Void> {

		private TransparentProgressDialog pdia;
		private ServiceException sexc;
		private Long idCategoria;

		public CameraTask(Long idCategoria) {
			this.idCategoria = idCategoria;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdia = new TransparentProgressDialog(MainActivity.this,
					R.drawable.loading);
			pdia.show();
		}

		protected Void doInBackground(File... files) {
			try {
				new DataServices().pushFoto(idCategoria, new FileInputStream(
						files[0]), getApplicationContext());
			} catch (ServiceException sexc) {
				this.sexc = sexc;
			} catch (FileNotFoundException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pdia.dismiss();
			if (null != this.sexc) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, sexc.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	}

	// Logica para Validar Evento
	// -------------------------------------------------------------------------

	private void getEvento(String codigo) {
		new EventoTask().execute(codigo);
	}

	private class EventoTask extends AsyncTask<String, Void, Void> {

		private TransparentProgressDialog pdia;
		private ServiceException sexc;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdia = new TransparentProgressDialog(MainActivity.this,
					R.drawable.loading);
			pdia.show();
		}

		protected Void doInBackground(String... codigo) {
			try {
				evento = new DataServices().connectTo(codigo[0]);
				if (null != evento) {
					setEvento(evento);
				} else {
					finalizarSesion();
				}
			} catch (final ServiceException sexc) {
				this.sexc = sexc;
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pdia.dismiss();
			if (null != this.sexc) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						TextView textConectar = (TextView) findViewById(R.id.textConectar);
						textConectar.setVisibility(TextView.VISIBLE);
						Button btnConectar = (Button) findViewById(R.id.btnConectar);
						btnConectar.setVisibility(Button.VISIBLE);

						Toast.makeText(MainActivity.this, sexc.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.titulo, menu);
		return true;
	}

	public void setEvento(final Evento evento) {
		this.evento = evento;

		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("EVENTO_CODIGO", evento.getCodigo());
		editor.commit();

		List<Categoria> categorias = evento.getCategorias();
		this.categoriasOpciones = new CharSequence[categorias.size()];
		for (int i = 0; i < categorias.size(); i++) {
			Categoria categoria = categorias.get(i);
			this.categoriasOpciones[i] = categoria.getNombre();
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				TextView textAlbum = (TextView) findViewById(R.id.textViewAlbum);
				textAlbum.setVisibility(TextView.VISIBLE);
				// Boton Ver Album --------------------
				Button btnVerAlbum = (Button) findViewById(R.id.btnVerAlbum);
				btnVerAlbum.setVisibility(Button.VISIBLE);

				TextView textCapturar = (TextView) findViewById(R.id.textViewCapturar);
				textCapturar.setVisibility(TextView.VISIBLE);
				// Boton Capturar Foto --------------------
				Button btnCaptura = (Button) findViewById(R.id.btnCapture);
				btnCaptura.setVisibility(Button.VISIBLE);

				TextView textUnirse = (TextView) findViewById(R.id.textViewUnirse);
				textUnirse.setVisibility(TextView.INVISIBLE);
				// Boton unir Evento --------------------
				Button btnUnirEvento = (Button) findViewById(R.id.btnUnirEvento);
				btnUnirEvento.setVisibility(Button.INVISIBLE);

				TextView textConectar = (TextView) findViewById(R.id.textConectar);
				textConectar.setVisibility(TextView.INVISIBLE);
				Button btnConectar = (Button) findViewById(R.id.btnConectar);
				btnConectar.setVisibility(Button.INVISIBLE);

				TextView textEvento = (TextView) findViewById(R.id.tituloEvento);
				textEvento.setText(evento.getNombre());
			}
		});

	}

	public void finalizarSesion() {
		this.evento = null;
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TextView textUnirse = (TextView) findViewById(R.id.textViewUnirse);
				textUnirse.setVisibility(TextView.VISIBLE);
				Button btnUnirEvento = (Button) findViewById(R.id.btnUnirEvento);
				btnUnirEvento.setVisibility(Button.VISIBLE);
			}
		});
	}

	public List<Categoria> getCategorias() {
		if (null != evento) {
			return evento.getCategorias();
		}
		return null;
	}

}