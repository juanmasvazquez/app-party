package ar.com.playfree;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import ar.com.playfree.entities.Foto;
import ar.com.playfree.services.DataServicesDummy;

import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams")
public class VerFotosActivity extends Activity {

	static List<Foto> fotos = new ArrayList<Foto>();
	JSONObject fotosJson = null;
	Spinner categorias = null;
	private Button botonMeGusta = null;
	// JSON Node names
	private static final String TAG_RESPONSE = "response";
	private static final String TAG_ERRORMESSAGE = "errorMessage";
	private static final String TAG_CANTLIKES = "cantLikes";
	private static final String TAG_ID = "id";
	private static final String TAG_ID_CATEGORIA = "idCategoria";
	private static final String TAG_LIKE = "like";
	private static final String TAG_MAC = "mac";
	private static final String TAG_URL = "url";
	private static final String TAG_USUARIO = "usuario";
	private static final String TAG_SUCCESS = "success";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_fotos);

		try {
			cargarFotos(this);
			cargarCategorias();
		} catch (JSONException e) {
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
		List<String> list = new ArrayList<String>();
		list.add("Android");
		list.add("Java");
		list.add("Spinner Data");
		list.add("Spinner Adapter");
		list.add("Spinner Example");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		categorias.setAdapter(dataAdapter);

		addListenerOnSpinnerItemSelection();

	}

	public void addListenerOnSpinnerItemSelection() {

		categorias.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	private List<Foto> cargarFotos(Context context) throws JSONException {

		DataServicesDummy dummy = new DataServicesDummy();
		return dummy.getFotos(context);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is
	// present.
	// getMenuInflater().inflate(R.menu.ver_fotos, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	// our custom adapter
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
			// ImageView imageView;
			ImageView imageView;
			// check to see if we have a view
			if (convertView == null) {
				// no view - so create a new one
				imageView = new ImageView(mContext);
			} else {
				// use the recycled view object
				imageView = (ImageView) convertView;
			}

			Picasso picasso = Picasso.with(mContext);

			Foto foto = (Foto) fotos.get(position);
			picasso.load(foto.getUrl())
					.placeholder(R.raw.place_holder)
					.error(R.raw.big_problem)
					.resize(150, 150)
					.centerCrop()
					// .transform(new
					// BitmapTransformations.OverlayTransformation(
					// mContext.getResources(), R.drawable.watermark25))
					.into(imageView);

			// if (!foto.isMeGusta()){
			// imageView.setBackgroundResource(R.drawable.watermark25);
			// imageView.getBackground().setLevel(1000);
			// //meGusta.setText("No me gusta");
			// }
			return imageView;
		}
	}
}
