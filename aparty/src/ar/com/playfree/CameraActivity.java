package ar.com.playfree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class CameraActivity extends Activity {
	int TAKE_PHOTO_CODE = 0;
	public static int count = 0;
	private File newfile;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		// here,we are making a folder named picFolder to store pics taken by
		// the camera using this application
		final String dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
				+ "/picFolder/";
		File newdir = new File(dir);
		newdir.mkdirs();

		Button capture = (Button) findViewById(R.id.btnCapture);
		capture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// here,counter will be incremented each time,and the picture
				// taken by camera will be stored as 1.jpg,2.jpg and likewise.
				count++;
				String file = dir + count + ".jpg";
				newfile = new File(file);
				try {
					newfile.createNewFile();
				} catch (IOException e) {
				}

				Uri outputFileUri = Uri.fromFile(newfile);

				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

				startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		
		
		if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			Bitmap bitmap = BitmapFactory.decodeFile(newfile.getAbsolutePath(),bmOptions);
			bitmap = Bitmap.createScaledBitmap(bitmap,200,200,true);
			try {
				sendPhoto(bitmap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void sendPhoto(Bitmap bitmap) throws Exception {
		new UploadTask().execute(bitmap);
	}

private class UploadTask extends AsyncTask<Bitmap, Void, Void> {
		
		protected Void doInBackground(Bitmap... bitmaps) {
			if (bitmaps[0] == null)
				return null;
			setProgress(0);
			
			DefaultHttpClient httpclient = new DefaultHttpClient();
			try {
				
				WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				WifiInfo info = manager.getConnectionInfo();
				String address = info.getMacAddress();

				HttpPost httppost = new HttpPost(
						"http://172.16.19.52:8080/playfree/foto/adm.action?mac=" + address); // server

				    InputStreamEntity reqEntity = new InputStreamEntity(
				            new FileInputStream(newfile), -1);
				    reqEntity.setContentType("binary/octet-stream");
				    reqEntity.setChunked(true); // Send in multiple parts if needed
				    httppost.setEntity(reqEntity);
				    HttpResponse response = httpclient.execute(httppost);
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			Toast.makeText(CameraActivity.this, R.string.uploaded, Toast.LENGTH_LONG).show();
		}
	}


private class UploadTask2 extends AsyncTask<Bitmap, Void, Void> {
	
	protected Void doInBackground(Bitmap... bitmaps) {
		if (bitmaps[0] == null)
			return null;
		setProgress(0);
		
		Bitmap bitmap = bitmaps[0];
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
		InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream

		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			
			WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = manager.getConnectionInfo();
			String address = info.getMacAddress();

			HttpPost httppost = new HttpPost(
					"http://192.168.1.144:8080/playfree/foto/adm.action?mac=" + address); // server
			
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("photo_playfree",
					System.currentTimeMillis() + ".jpg", in);
			httppost.setEntity(reqEntity);

			HttpResponse response = null;
			try {
				response = httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
			} finally {

			}
		} finally {

		}

		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		Toast.makeText(CameraActivity.this, R.string.uploaded, Toast.LENGTH_LONG).show();
	}
}

}
