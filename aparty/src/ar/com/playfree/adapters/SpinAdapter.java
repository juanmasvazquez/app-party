package ar.com.playfree.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ar.com.playfree.entities.Categoria;

public class SpinAdapter extends ArrayAdapter<Categoria>{


    public SpinAdapter(Context context, int textViewResourceId,
    		List<Categoria> list) {
        super(context, textViewResourceId, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);       
        label.setText(this.getItem(position).getNombre());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);       
        label.setText(this.getItem(position).getNombre());
        label.setHeight(50);
        return label;
    }
}