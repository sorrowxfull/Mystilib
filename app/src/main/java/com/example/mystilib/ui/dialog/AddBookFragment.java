package com.example.mystilib.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_addbook, null);
        final EditText bookname = view.findViewById(R.id.book_name);
        final EditText groupname = view.findViewById(R.id.group_name);
        final RadioGroup state = view.findViewById(R.id.radio_state);
        final RadioGroup indicator = view.findViewById(R.id.radio_indicator);
        final RatingBar stars = view.findViewById(R.id.rating_stars);
        builder.setView(view);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AddBook addBook = new AddBook();
                        addBook.bookname = bookname.getText().toString();
                        addBook.groupname = groupname.getText().toString();
                        addBook.state = state.getCheckedRadioButtonId();
                        //Switch case sur l'état
                        MyBus.bus.post(addBook);
                        Toast.makeText(getContext(), "Add " + bookname.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddBookFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
