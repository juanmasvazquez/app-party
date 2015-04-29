package ar.com.playfree;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import ar.com.playfree.entities.Foto;

import com.squareup.picasso.Picasso;


public class FotoGrandeActivity extends Activity {

	ImageButton imageButton;
	Button botonLike;
	String msg = "Te gusta esta foto!";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
	//	ImageView imageView = (ImageView) findViewById(R.id.imageView);
		TouchImageView imageView = (TouchImageView) findViewById(R.id.imageView);
		
		int position = getIntent().getIntExtra("position", -1);
		final Foto foto = (Foto) getIntent().getSerializableExtra("foto");
		if (position != -1) {
			Picasso.with(FotoGrandeActivity.this)
					.load(foto.getUrl())
					.placeholder(R.raw.place_holder)
					.resize(800, 800)
					.centerInside()
					.error(R.raw.big_problem)
					.into(imageView);
		} else {
			Picasso.with(FotoGrandeActivity.this)
					.load(R.raw.big_problem)
					.resize(800, 800)
					.centerCrop()
					.into(imageView);
		}
		
		TextView cantLikes = (TextView)findViewById(R.id.cantLikes);		
		cantLikes.setText(String.valueOf(foto.getCantLikes()));
		
		TextView subidaPor = (TextView)findViewById(R.id.subidaPor);		
		subidaPor.setText(foto.getUsuario());

		botonLike = (Button) findViewById(R.id.botonlike);
		if (foto.isLike()){
			botonLike.setText("Ya no me gusta");
			foto.setLike(false);
		} else {
			botonLike.setText("Me gusta");
			foto.setLike(true);
		}
		botonLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {				
				if (foto.isLike()){
					botonLike.setText("Ya no me gusta");
					foto.setLike(false);
				} else {
					botonLike.setText("Me gusta");
					foto.setLike(true);
				}

//				Toast.makeText(FotoGrandeActivity.this, msg,
//						Toast.LENGTH_SHORT).show();
//				Intent verFotosIntent = new Intent(FotoGrandeActivity.this,
//						VerFotosActivity.class);
//				startActivity(verFotosIntent);

			}
		});

	}
}
