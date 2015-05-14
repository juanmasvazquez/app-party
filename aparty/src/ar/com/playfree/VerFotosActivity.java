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
import android.view.LayoutInflater;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.playfree.adapters.SpinAdapter;
import ar.com.playfree.entities.Categoria;
import ar.com.playfree.entities.Foto;
import ar.com.playfree.exceptions.ServiceException;
import ar.com.playfree.services.DataServices;
import ar.com.playfree.services.DataServicesDummy;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VerFotosActivity extends Activity {

	static List<Foto> fotos = new ArrayList<Foto>();
	Spinner categorias = null;
	DataServices services = new DataServices();
	ImageAdapter2 imageAdapter;
	GridView gridview;
	protected ImageLoader imageLoader;

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
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		setContentView(R.layout.activity_ver_fotos);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		try {
			getFotosByCategoria(null);
			cargarCategorias();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void cargarCategorias() {
		categorias = (Spinner) findViewById(R.id.categorias);
		List<Categoria> list = MainActivity.instance.getCategorias();
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

//	private class ImageAdapter extends BaseAdapter {
//		private Context mContext;
//
//		public ImageAdapter(Context context, List<Foto> album) {
//			mContext = context;
//			if (null != album) {
//				fotos = album;
//			}
//		}
//
//		@Override
//		public int getCount() {
//			return fotos.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			ImageView imageView;
//			if (convertView == null) {
//				imageView = new ImageView(mContext);
//			} else {
//				imageView = (ImageView) convertView;
//			}
//
//			Picasso picasso = Picasso.with(mContext);
//			Foto foto = (Foto) fotos.get(position);
//			picasso.load(foto.getUrlThumb()).placeholder(R.raw.place_holder)
//					.error(R.raw.big_problem).resize(150, 150).centerCrop()
//					.into(imageView);
//
//			if (foto.isLike()) {
//				Bitmap water = BitmapFactory.decodeResource(
//						mContext.getResources(), R.drawable.facebooklike23);
//				imageView.setDrawingCacheEnabled(true);
//				imageView
//						.measure(MeasureSpec.makeMeasureSpec(0,
//								MeasureSpec.UNSPECIFIED), MeasureSpec
//								.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//				imageView.layout(0, 0, imageView.getMeasuredWidth(),
//						imageView.getMeasuredHeight());
//				imageView.buildDrawingCache(true);
//				Bitmap principal = Bitmap.createBitmap(imageView
//						.getDrawingCache());
//				Bitmap bmOverlay = Bitmap.createBitmap(150, 150,
//						principal.getConfig());
//				imageView.setDrawingCacheEnabled(false);
//				Canvas canvas = new Canvas(bmOverlay);
//				canvas.drawBitmap(principal, 0, 0, null);
//				canvas.drawBitmap(water, 0, 110, null);
//				imageView.setImageBitmap(bmOverlay);
//			}
//
//			return imageView;
//		}
//	}

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
				getFotosByCategoria(null);
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

			Categoria categoria = (Categoria) parent.getItemAtPosition(pos);
			try {
				getFotosByCategoria(categoria.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			imageAdapter = new ImageAdapter2(getApplicationContext(), fotos);
			gridview.setAdapter(imageAdapter);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	private void getFotosByCategoria(Long idCategoria) throws Exception {
		new PhotoTask().execute(idCategoria);
	}

	private class PhotoTask extends AsyncTask<Long, Void, Void> {

		protected Void doInBackground(Long... categorias) {
			setProgress(0);
			Long idCategoria = categorias[0];

			try {
				fotos = new DataServices().getFotosCategoria(idCategoria, getApplicationContext());
			} catch (final ServiceException sexc) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(VerFotosActivity.this,
								sexc.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
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

			gridview = (GridView) findViewById(R.id.gridview);
			imageAdapter = new ImageAdapter2(VerFotosActivity.this, fotos);
			gridview.setAdapter(imageAdapter);
			gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Intent i = new Intent(VerFotosActivity.this,
							FotoGrandeActivity.class);
					i.putExtra("position", position);
					i.putExtra("foto", (Foto) fotos.get(position));
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
	
	private static class ImageAdapter2 extends BaseAdapter {

		private LayoutInflater inflater;

		private DisplayImageOptions options;
		
		private Context mContext ;

		ImageAdapter2(Context context, List<Foto> album) {
			fotos = album;
			inflater = LayoutInflater.from(context);
			mContext = context;
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.raw.place_holder)
					.showImageForEmptyUri(R.raw.place_holder)
					.showImageOnFail(R.raw.big_problem)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.imageView2 = (ImageView) view.findViewById(R.id.image2);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			Foto foto = (Foto) fotos.get(position);
			if (foto.isLike()){
				holder.imageView2.setVisibility(View.VISIBLE);
				holder.imageView2.setImageResource(R.drawable.facebooklike23);
			}			
			ImageLoader.getInstance()
					.displayImage(foto.getUrlThumb(), holder.imageView, options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri, View view, int current, int total) {
							holder.progressBar.setProgress(Math.round(100.0f * current / total));
						}
					});

			return view;
		}
	}

	static class ViewHolder {
		ImageView imageView;
		ImageView imageView2;
		ProgressBar progressBar;
	}

}
