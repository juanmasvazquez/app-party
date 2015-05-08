package ar.com.playfree;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.playfree.adapters.SpinAdapter;
import ar.com.playfree.entities.Categoria;
import ar.com.playfree.entities.Foto;
import ar.com.playfree.services.DataServices;
import ar.com.playfree.services.DataServicesDummy;

import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VerFotosActivity extends Activity {

	static List<Foto> fotos = new ArrayList<Foto>();
	Spinner categorias = null;
	DataServicesDummy dummy = new DataServicesDummy();
	ImageAdapter imageAdapter;
	GridView gridview;

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
		setContentView(R.layout.activity_ver_fotos);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		try {
			getPhoto();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cargarCategorias() {
		categorias = (Spinner) findViewById(R.id.categorias);
		List<Categoria> list = dummy.getCategorias(null);
		SpinAdapter adapter;
		adapter = new SpinAdapter(VerFotosActivity.this,
				android.R.layout.simple_spinner_item, list);
		categorias.setAdapter(adapter);
		addListenerOnSpinnerItemSelection();
	}

	public void addListenerOnSpinnerItemSelection() {

		categorias
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	private class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context context, List<Foto> album) {
			mContext = context;
			if (null != album) {
				fotos = album;
			}
		}

		@Override
		public int getCount() {
			return fotos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
			} else {
				imageView = (ImageView) convertView;
			}

			Picasso picasso = Picasso.with(mContext);
			Foto foto = (Foto) fotos.get(position);
			picasso.load(foto.getUrlThumb()).placeholder(R.raw.place_holder)
					.error(R.raw.big_problem).resize(150, 150).centerCrop()
					.into(imageView);

			if (foto.isLike()) {
				Bitmap water = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.facebooklike23);
				imageView.setDrawingCacheEnabled(true);
				imageView
						.measure(MeasureSpec.makeMeasureSpec(0,
								MeasureSpec.UNSPECIFIED), MeasureSpec
								.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				imageView.layout(0, 0, imageView.getMeasuredWidth(),
						imageView.getMeasuredHeight());
				imageView.buildDrawingCache(true);
				Bitmap principal = Bitmap.createBitmap(imageView
						.getDrawingCache());
				Bitmap bmOverlay = Bitmap.createBitmap(150, 150,
						principal.getConfig());
				imageView.setDrawingCacheEnabled(false);
				Canvas canvas = new Canvas(bmOverlay);
				canvas.drawBitmap(principal, 0, 0, null);
				canvas.drawBitmap(water, 0, 110, null);
				imageView.setImageBitmap(bmOverlay);
			}

			return imageView;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		int id = menuItem.getItemId();
		switch (menuItem.getItemId()) {
		// boton BACK
		case android.R.id.home:
			startActivityAfterCleanup(MainActivity.class);
			return true;
		}
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.refrescar) {
			Toast.makeText(getBaseContext(), "Refrescando fotos...",
					Toast.LENGTH_SHORT).show();
			try {
				getPhoto();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	private void startActivityAfterCleanup(Class<?> cls) {
		Intent intent = new Intent(getApplicationContext(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	private class CustomOnItemSelectedListener implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			// TODO eliminar dummy
			Categoria categoria = (Categoria) parent.getItemAtPosition(pos);
			DataServicesDummy dummy = new DataServicesDummy();
			List<Foto> album = dummy.getFotosCategoria(categoria.getId(), null);
			//android.os.SystemClock.sleep(1000);
			if (album.size() == 0) {

			}
			imageAdapter = new ImageAdapter(getApplicationContext(), album);
			gridview.setAdapter(imageAdapter);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	private void getPhoto() throws Exception {
		new PhotoTask().execute();
	}

	private class PhotoTask extends AsyncTask<Void, Void, Void> {

		private List<Foto> photos;

		protected Void doInBackground(Void... files) {
			setProgress(0);

			photos = new DataServices().getFotos(getApplicationContext());

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {

			gridview = (GridView) findViewById(R.id.gridview);
			imageAdapter = new ImageAdapter(VerFotosActivity.this, photos);
			gridview.setAdapter(imageAdapter);
			gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Intent i = new Intent(VerFotosActivity.this,
							FotoGrandeActivity.class);
					i.putExtra("position", position);
					i.putExtra("foto", (Foto) photos.get(position));
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.items, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
