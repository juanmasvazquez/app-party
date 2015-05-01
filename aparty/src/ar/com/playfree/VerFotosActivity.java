package ar.com.playfree;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
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
import ar.com.playfree.adapters.SpinAdapter;
import ar.com.playfree.entities.Categoria;
import ar.com.playfree.entities.Foto;
import ar.com.playfree.services.DataServicesDummy;

import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VerFotosActivity extends Activity {

	static List<Foto> fotos = new ArrayList<Foto>();
	Spinner categorias = null;
	DataServicesDummy dummy = new DataServicesDummy();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_fotos);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		final List<Foto> album = (List<Foto>) getIntent().getSerializableExtra(
				"album");

		try {
			if (null == album) {
				fotos = cargarFotos(this);
			} else {
				fotos = album;
			}
			cargarCategorias();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent i = new Intent(VerFotosActivity.this,
						FotoGrandeActivity.class);
				i.putExtra("position", position);
				i.putExtra("foto", (Foto) fotos.get(position));
				startActivity(i);
			}
		});
	}

	private void cargarCategorias() {
		categorias = (Spinner) findViewById(R.id.categorias);
		List<Categoria> list = dummy.getCategorias(null);
		SpinAdapter adapter;
		// for (Categoria categoria : list){
		// lista.add(categoria.getNombre());
		// }
		// ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, lista);
		// dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// categorias.setAdapter(dataAdapter);
		// addListenerOnSpinnerItemSelection();
		adapter = new SpinAdapter(VerFotosActivity.this,
				android.R.layout.simple_spinner_item, list);
		categorias.setAdapter(adapter);
		addListenerOnSpinnerItemSelection();
	}

	public void addListenerOnSpinnerItemSelection() {

		categorias
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	private List<Foto> cargarFotos(Context context) throws JSONException {

		return dummy.getFotos(null);
	}

	private class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context context) {
			mContext = context;
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
			picasso.load(foto.getUrl()).placeholder(R.raw.place_holder)
					.error(R.raw.big_problem).resize(150, 150).centerCrop()
					// .transform(new
					// BitmapTransformations.OverlayTransformation(
					// mContext.getResources(), R.drawable.watermark25))
					.into(imageView);

			if (foto.isLike()) {
				Bitmap water = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.facebooklike23);
				imageView.setDrawingCacheEnabled(true);
				imageView.measure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED), MeasureSpec
								.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				imageView.layout(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
				imageView.buildDrawingCache(true);
				Bitmap principal = Bitmap.createBitmap(imageView.getDrawingCache());
				Bitmap bmOverlay = Bitmap.createBitmap(150, 150, principal.getConfig());
				imageView.setDrawingCacheEnabled(false);
				Canvas canvas = new Canvas(bmOverlay);
				canvas.drawBitmap(principal, 0, 0, null);
				canvas.drawBitmap(water, 0, 110, null);

				imageView.setImageBitmap(bmOverlay);
			}

			return imageView;

			// return imageView;
		}
	}

	// boton BACK
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		int id = menuItem.getItemId();
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			startActivityAfterCleanup(MainActivity.class);
			return true;
		}
		if (id == R.id.action_settings) {
			return true;
		} 
		return (super.onOptionsItemSelected(menuItem));
	}

	private void startActivityAfterCleanup(Class<?> cls) {
		// if (projectsDao != null) projectsDao.close();
		Intent intent = new Intent(getApplicationContext(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private class CustomOnItemSelectedListener implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			// TODO eliminar dummy
			Categoria categoria = (Categoria) parent.getItemAtPosition(pos);
			DataServicesDummy dummy = new DataServicesDummy();
			List<Foto> album = dummy.getFotosCategoria(categoria.getId(), null);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}
	


//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
