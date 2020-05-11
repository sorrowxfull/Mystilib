package com.example.mystilib.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystilib.Adapter;
import com.example.mystilib.Book;
import com.example.mystilib.MyBus;
import com.example.mystilib.R;
import com.example.mystilib.events.AddBook;
import com.example.mystilib.events.DeleteBook;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recycler;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    static List<Book> books = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        recycler = root.findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        /*
        books.add(new Book("D&D", "JDR"));
        books.add(new Book("Antika", "JDR"));
        books.add(new Book("Pathfinder", "JDR"));
        books.add(new Book("Game of Thrones", "JDR"));
        books.add(new Book("The Witcher", "JDR"));
        books.add(new Book("Shadowrun", "JDR"));
        */

        // specify an adapter (see also next example)
        adapter = new Adapter(books, getContext());
        recycler.setAdapter(adapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        MyBus.bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyBus.bus.unregister(this);
    }

    @Subscribe
    public void deleteBook(DeleteBook deleteBook) {
        Toast.makeText(getContext(),"Suppression " + deleteBook.deletedBook.name, Toast.LENGTH_SHORT).show();
        books.remove(deleteBook.deletedBook);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void addBook(AddBook addBook) {
        Toast.makeText(getContext(),"Ajout " + addBook.bookname, Toast.LENGTH_SHORT).show();
        Book newBook = new Book(addBook.bookname, addBook.groupname);
        books.add(newBook);
        adapter.notifyDataSetChanged();
    }
}
