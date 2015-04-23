package ar.com.playfree;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class VerFotosActivity extends Activity {

	List fotos = new ArrayList<FotoEntity>();
	JSONObject fotosJson = null;
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
			cargarFotos(CameraActivity.address);
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
				i.putExtra("foto", (FotoEntity)fotos.get(position));
				startActivity(i);
			}
		});
	}

	private void cargarFotos(String mac) throws JSONException {

		// FotoEntity foto1 = new FotoEntity();
		// foto1.setCantLikes(145);
		// foto1.setURL("http://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Dell_Logo.png/1027px-Dell_Logo.png");
		// foto1.setSubidaPor("Jimmy Neutron");
		// foto1.setMeGusta(true);
		// foto1.setCategoria(1);
		//
		// FotoEntity foto2 = new FotoEntity();
		// foto2.setCantLikes(2);
		// foto2.setURL("http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png");
		// foto2.setSubidaPor("Chicharito");
		// foto2.setMeGusta(false);
		// foto2.setCategoria(2);
		//
		// fotos = new FotoEntity[] { foto1, foto2 };

		JSONObject json = new JSONObject(
				"{'response':"
						+ "{'errorMessage':'',"
						+ "'response':["
						+ "{'cantLikes':1,'id':13,'idCategoria':1,'like':true,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429754540659.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':14,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429754757869.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':15,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429754777132.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':16,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429758106664.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':17,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429758211609.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':18,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429758249052.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':19,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429758278136.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':20,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429759065311.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':21,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429759128078.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':22,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429759151506.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':25,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429759915224.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':26,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429759939301.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':27,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429760014116.jpg','usuario':'juanma.svazquez@gmail.com'}],"
						+ "'success':true}}");

		if (json != null) {
			try {				
				fotosJson = json.getJSONObject(TAG_RESPONSE);
				if (fotosJson.getBoolean(TAG_SUCCESS)) {
					JSONArray fotosJsonArray = fotosJson.getJSONArray(TAG_RESPONSE);
					for (int i = 0; i < fotosJsonArray.length(); i++) {
						JSONObject c = fotosJsonArray.getJSONObject(i);
						
						FotoEntity foto = new FotoEntity();

						foto.setId(c.getString(TAG_ID));
						foto.setCantLikes(c.getString(TAG_CANTLIKES));
						foto.setIdCategoria(c.getString(TAG_ID_CATEGORIA));
						foto.setMeGusta(c.getBoolean(TAG_LIKE));
						foto.setSubidaPor(c.getString(TAG_USUARIO));
						foto.setURL(c.getString(TAG_URL));
						foto.setMac(c.getString(TAG_MAC));

						fotos.add(foto);
					}// FOR
				}// IF
			} catch (JSONException e) {// Try
				e.printStackTrace();

			}// CATCH
		} else { // IF
			Toast.makeText(VerFotosActivity.this, "Error al leer fotos",
					Toast.LENGTH_SHORT).show();
		} // ELSE
	}

	// static Integer[] mThumbIds = { 1, 2 };
	// static String[] imagesURL = {
	// "http://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Dell_Logo.png/1027px-Dell_Logo.png",
	// "http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png"
	// };

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

			// picasso.with(VerFotosActivity.this)
			FotoEntity foto = (FotoEntity) fotos.get(position);
			picasso.load(foto.getURL())
					.placeholder(R.raw.place_holder)
					.error(R.raw.big_problem)
					.resize(150, 150).centerCrop()
					// .transform(new
					// BitmapTransformations.OverlayTransformation(
					// mContext.getResources(), R.drawable.watermark25))
					.into(imageView);

			return imageView;
		}
	}
}
