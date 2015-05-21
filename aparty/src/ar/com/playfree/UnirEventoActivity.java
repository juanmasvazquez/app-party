package ar.com.playfree;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ar.com.playfree.entities.Evento;
import ar.com.playfree.exceptions.ServiceException;
import ar.com.playfree.services.DataServices;

public class UnirEventoActivity extends Activity {

	private Handler handler = new Handler();
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unir_evento);

		// // set the last parameter to true to open front light if available
		IntentIntegrator.initiateScan(UnirEventoActivity.this,
				R.layout.activity_unir_evento, R.id.viewfinder_view,
				R.id.preview_view, true);

	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE:
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			if (scanResult == null) {
				return;
			}
			final String result = scanResult.getContents();
			if (result != null) {

				new RetrieveEventTask().execute(result);

			}
			break;
		default:
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.unir_evento, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class RetrieveEventTask extends AsyncTask<String, Void, Evento> {

		private ServiceException exception;

		protected Evento doInBackground(String... codigo) {
			Evento evento = null;
			try {
				evento = new DataServices().connectTo(codigo[0]);
			} catch (ServiceException sexc) {
				this.exception = sexc;
			}
			return evento;
		}

		@Override
		protected void onPostExecute(final Evento result) {
			if (null != exception) {
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_LONG;
				Toast toast = Toast.makeText(context, exception.getMessage(),
						duration);
				toast.show();
			} else {

				handler.post(new Runnable() {
					@Override
					public void run() {

						AlertDialog alertDialog = new AlertDialog.Builder(
								UnirEventoActivity.this).create();
						alertDialog.setTitle("Evento encontrado");
						alertDialog.setMessage("¿Desea unirse al evento: "
								+ result.getNombre() + " ?");
						alertDialog.setIcon(R.drawable.ic_unirse_dialog);

						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
								"Aceptar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										MainActivity mainActivity = MainActivity.instance;
										mainActivity.setEvento(result);
										dialog.dismiss();
										finish();
									}
								});
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
								"Cancelar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										finish();

									}
								});

						alertDialog.setOnCancelListener(new OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								dialog.dismiss();
								finish();
							}
						});

						alertDialog.show();

					}
				});
			}
		}
	}

}
