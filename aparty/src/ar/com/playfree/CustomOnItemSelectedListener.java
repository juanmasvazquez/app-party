package ar.com.playfree;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import ar.com.playfree.entities.Foto;
 
public class CustomOnItemSelectedListener implements OnItemSelectedListener {
 
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
            long id) {
         
    	//TODO eliminar dummy
        Toast.makeText(parent.getContext(), 
                "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_LONG).show();
        List<Foto> fotos = VerFotosActivity.fotos;
        
        
    }
 
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
 
    }
 
}
