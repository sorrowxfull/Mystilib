package com.example.mystilib.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mystilib.Book;
import com.example.mystilib.R;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    String titleList[] = {"Harry Potter", "Le Seigneur des Anneaux", "Star Wars", "Donjons et Dragons"};
    ArrayList<Book> bookList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        int i = 0;
        for (String title : titleList) {
            Book book = new Book(title, String.valueOf(i), 0, 0, 0);
            bookList.add(book);
            i++;
        }

        ArrayAdapter<String> bookAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.item, R.id.book_name, titleList
        );
        ListView list = root.findViewById(R.id.simpleListView);
        list.setAdapter(bookAdapter);

        /*
        Button stateBtn = root.findViewById(R.id.state_btn);
        Button indicatorBtn = root.findViewById(R.id.indicator_btn);

        stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
            }
        });

        indicatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
         */
        //On click d'un item, ouvrir une page pour modifier le bouquin

        return root;
    }
}
