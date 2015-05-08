package ar.com.playfree.utils;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import ar.com.playfree.MainActivity;
import ar.com.playfree.R;
import ar.com.playfree.entities.Categoria;

public class CategoriaSelectionDialog extends DialogFragment {

	private List<Categoria> categorias;
	private CharSequence[] categoriasOpciones;

	@Override
	public void setArguments(Bundle args) {
		List<Categoria> categorias = (List<Categoria>) args
				.getSerializable("categorias");
		this.categorias = categorias;
		this.categoriasOpciones = new CharSequence[categorias.size()];
		for (int i = 0; i < categorias.size(); i++) {
			Categoria categoria = categorias.get(i);
			this.categoriasOpciones[i] = categoria.getNombre();
		}
	}
}
