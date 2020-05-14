package com.example.mystilib.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystilib.Adapter;
import com.example.mystilib.Book;
import com.example.mystilib.MyBus;
import com.example.mystilib.R;
import com.example.mystilib.events.AddBook;
import com.example.mystilib.events.DeleteBook;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HomeFragment extends Fragment {

    private RecyclerView recycler;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    static List<Book> books = new ArrayList<>();
    String file = "save.txt";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recycler = root.findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        //Création de l'adapter
        adapter = new Adapter(books, getContext());
        recycler.setAdapter(adapter);

        return root;
    }

    //Au démarrage ou redémarrage de l'activité, lecture du fichier de sauvegarde
    @Override
    public void onStart() {
        super.onStart();
        readFile(file);
    }

    //Lorsque l'on arrive ou revient sur l'activité, on s'abonne au bus d'évenement
    @Override
    public void onResume() {
        super.onResume();
        MyBus.bus.register(this);
    }

    //Lorsque l'activité est mise en pause, on la désabonne du bus d'évenement
    @Override
    public void onPause() {
        super.onPause();
        MyBus.bus.unregister(this);
    }

    //Lorsque l'on va quitter l'activité, on écrit le fichier de sauvegarde et on réinitialise la liste de livre
    @Override
    public void onStop() {
        super.onStop();
        writeFile(file);
        books.removeAll(books);
    }

    //Ecriture du fichier de sauvegarde
    private void writeFile(String file) {
        String data = "";
        //Une ligne par livre, avec | en délimiteur
        for (Book book : books) {
            data = data + book.name + "|" + book.group + "|" + book.state + "|" + book.indicator + "|" + book.stars + "\n";
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = getContext().openFileOutput(file, Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Lecture du fichier de sauvegarde
    private void readFile(String file) {
        FileInputStream fileInputStream = null;
        StringBuilder stringBuilder = null;
        try {
            fileInputStream = getContext().openFileInput(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            stringBuilder = new StringBuilder();
            String line;
            //Pour chaque ligne, création d'un livre
            while((line = bufferedReader.readLine()) != null) {
                //Découpe line avec | en délimiteur
                StringTokenizer st = new StringTokenizer(line, "|");
                int numTokens = st.countTokens();
                if (numTokens >= 5) {
                    AddBook addBook = new AddBook();
                    addBook.bookname = st.nextToken();
                    addBook.groupname = st.nextToken();
                    addBook.state = Integer.parseInt(st.nextToken());
                    addBook.indicator = Integer.parseInt(st.nextToken());
                    addBook.stars = Float.parseFloat(st.nextToken());
                    addBook(addBook);
                }
                stringBuilder.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (stringBuilder != null) {
            System.out.println(stringBuilder);
        } else {
            System.err.println("stringBuilder est null");
        }
    }

    //Evenement de suppression d'un livre
    @Subscribe
    public void deleteBook(DeleteBook deleteBook) {
        books.remove(deleteBook.deletedBook);
        adapter.notifyDataSetChanged();
    }

    //Evenement d'ajout d'un livre
    @Subscribe
    public void addBook(AddBook addBook) {
        Book newBook = new Book(addBook.bookname, addBook.groupname, addBook.state, addBook.indicator, addBook.stars);
        books.add(newBook);
        adapter.notifyDataSetChanged();
    }
}
