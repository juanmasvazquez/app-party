package ar.com.playfree;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class FotoGrandeActivity extends Activity {

	ImageButton imageButton;
	Button botonLike;
	String msg = "Te gusta esta foto!";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		ImageView imageView = (ImageView) findViewById(R.id.imageView);

		int position = getIntent().getIntExtra("position", -1);
		final FotoEntity foto = (FotoEntity) getIntent().getSerializableExtra("foto");
		if (position != -1) {
			Picasso.with(FotoGrandeActivity.this)
					.load(foto.getURL())
					.placeholder(R.raw.place_holder)
					.resize(800, 800)
					.centerCrop()
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
		subidaPor.setText(foto.getSubidaPor());

		botonLike = (Button) findViewById(R.id.botonlike);
		botonLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {				
				if (!foto.isMeGusta()){
					msg = "No te gusta esta foto!";
				}
				Toast.makeText(FotoGrandeActivity.this, msg,
						Toast.LENGTH_SHORT).show();
				Intent verFotosIntent = new Intent(FotoGrandeActivity.this,
						VerFotosActivity.class);
				startActivity(verFotosIntent);

			}
		});

	}
}
