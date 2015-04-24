package ar.com.playfree;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class VerFotosActivity extends Activity {

	static List<FotoEntity> fotos = new ArrayList<FotoEntity>();
	JSONObject fotosJson = null;
	Spinner categorias = null;
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
				i.putExtra("foto", (FotoEntity)fotos.get(position));
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
         
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                     (this, android.R.layout.simple_spinner_item,list);
                      
        dataAdapter.setDropDownViewResource
                     (android.R.layout.simple_spinner_dropdown_item);
                      
        categorias.setAdapter(dataAdapter);
        
        addListenerOnSpinnerItemSelection();

    }
 
    public void addListenerOnSpinnerItemSelection(){
         
    	categorias.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
     
	private void cargarFotos(String mac) throws JSONException {

		JSONObject json = new JSONObject(
				"{'response':"
						+ "{'errorMessage':'',"
						+ "'response':["
						+ "{'cantLikes':1,'id':13,'idCategoria':1,'like':true,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':14,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':15,'idCategoria':1,'like':true,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':16,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':17,'idCategoria':1,'like':true,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':18,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':19,'idCategoria':1,'like':true,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':20,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':21,'idCategoria':1,'like':true,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':22,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.pngg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':25,'idCategoria':1,'like':true,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429759915224.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':26,'idCategoria':1,'like':false,'mac':'EC-F4-BB-58-D7-FA','url':'http://localhost:80/eventos/1/EC-F4-BB-58-D7-FA_1429759939301.jpg','usuario':'juanma.svazquez@gmail.com'},"
						+ "{'cantLikes':0,'id':27,'idCategoria':1,'like':true,'mac':'EC-F4-BB-58-D7-FA','url':'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Intel-logo.svg/2000px-Intel-logo.svg.png','usuario':'juanma.svazquez@gmail.com'}],"
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
					}
				} else { 
					Toast.makeText(VerFotosActivity.this, TAG_ERRORMESSAGE,
							Toast.LENGTH_SHORT).show();
				} 
			} catch (JSONException e) {
				e.printStackTrace();

			}
		} 
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
					.resize(150, 150)
					.centerCrop()
					// .transform(new
					// BitmapTransformations.OverlayTransformation(
					// mContext.getResources(), R.drawable.watermark25))
					.into(imageView);
			
			// Read your drawable from somewhere
			Drawable dr = getResources().getDrawable(R.drawable.watermark25);
			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			// Scale it to 50 x 50
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 60, true));
			// Set your new, scaled drawable "d"
			if (!foto.isMeGusta()){
				imageView.setBackground(d);
			}
			return imageView;
		}
	}
}
