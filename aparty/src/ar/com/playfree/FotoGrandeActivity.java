package ar.com.playfree;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
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

//	@Override
//	public boolean onOptionsItemSelected(MenuItem menuItem) {
//
//		switch (menuItem.getItemId()) {
//		case android.R.id.home:
//			startActivityAfterCleanup(VerFotosActivity.class);
//			return true;
//		case R.id.bajarFoto:
//	//		new BajarFoto().execute(fotoElegida.getUrl());
//		}
//		return true;
//	}

	//private class BajarFoto extends AsyncTask<String, Void, Bitmap> {
	    /** Reference to the view which should receive the image */
//	    private final WeakReference imageRef;public BajarFoto(ImageView imageView) {
//	        imageRef = new WeakReference(imageView);
//	    }
//	 
//	    @Override
//	    protected void onPreExecute() {
//	        super.onPreExecute();
//	        progressDialog = ProgressDialog.show(FotoGrandeActivity.this, "Wait", "Downloading...");
//	    }
//	 
//	    @Override
//	    protected Bitmap doInBackground(String... params) {
//	        InputStream input = null;
//	        try {
//	            URL url = new URL(params[0]);
//	            // We open the connection
//	            URLConnection conection = url.openConnection();
//	            conection.connect();
//	            input = new BufferedInputStream(url.openStream(), 8192);
//	            // we convert the inputStream into bitmap
//	            bitmap = BitmapFactory.decodeStream(input);
//	            input.close();
//	        } catch (Exception e) {
//	            Log.e("Error: ", e.getMessage());
//	        }
//	        return bitmap;
//	    }
//	 
//	    /**
//	    * After completing background task Dismiss the progress dialog
//	    * **/
//	    protected void onPostExecute(Bitmap bitmap) {
//	        progressDialog.dismiss();
//	        if (isCancelled()) {
//	            bitmap = null;
//	        }
//	 
//	        if (imageRef != null) {
//	            ImageView imageView = imageRef.get();
//	            if (imageView != null &amp;&amp; bitmap != null) {
//	                imageView.setImageBitmap(bitmap);
//	            } else
//	                Toast.makeText(DownloadImageActivity.this, "Error while downloading the image!", Toast.LENGTH_LONG).show();
//	        }
//	    }
//	}
//
//	private void startActivityAfterCleanup(Class<?> cls) {
//		// if (projectsDao != null) projectsDao.close();
//		Intent intent = new Intent(getApplicationContext(), cls);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_foto_grande, menu);
//		return true;
//	}

}
