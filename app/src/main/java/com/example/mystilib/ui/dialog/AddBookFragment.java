package com.example.mystilib.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.mystilib.MyBus;
import com.example.mystilib.R;
import com.example.mystilib.events.AddBook;

public class AddBookFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        //Ici on récupère un lien vers les éléments de l'ajout d'un livre
        final View view = inflater.inflate(R.layout.dialog_addbook, null);
        final EditText bookname = view.findViewById(R.id.book_name);
        final EditText groupname = view.findViewById(R.id.group_name);
        final RadioGroup stateGroup = view.findViewById(R.id.radio_state);
        final RadioGroup indicatorGroup = view.findViewById(R.id.radio_indicator);
        final RatingBar stars = view.findViewById(R.id.rating_stars);
        builder.setView(view);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            //Lorsqu'il clique sur ADD
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AddBook addBook = new AddBook(); //On stocke ici le bouquin qui sera ajouté
                        addBook.bookname = bookname.getText().toString();
                        addBook.groupname = groupname.getText().toString();
                        //En fonction du bouton check, on déduit le state et l'indicator du livre
                        RadioButton state = checkedButton(view, stateGroup);
                        switch(state.getText().toString()) {
                            case "Acquired":
                                addBook.state = 0;
                                break;
                            case "Borroweed":
                                addBook.state = 1;
                                break;
                            case "To buy":
                                addBook.state = 2;
                                break;
                        }
                        RadioButton indicator = checkedButton(view, indicatorGroup);
                        switch(indicator.getText().toString()) {
                            case "To read":
                                addBook.indicator = 0;
                                break;
                            case "In progress":
                                addBook.indicator = 1;
                                break;
                            case "Read":
                                addBook.indicator = 2;
                                break;
                        }
                        addBook.stars = stars.getRating();
                        //On poste l'évenement addBook
                        MyBus.bus.post(addBook);
                    }
                }) //Lorsqu'il clique sur CANCEL
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddBookFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    //Retourne le lien vers le bouton radio qui est checké dans le groupe radioGroup
    public RadioButton checkedButton(View view, RadioGroup radioGroup) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        RadioButton radio = view.findViewById(radioId);
        return radio;
    }
}
