package com.example.mystilib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystilib.events.DeleteBook;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private List<Book> mDataset; //La liste de livre
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //Chaque livre affiche
        public TextView bookName;   //son nom
        public Button stateBtn;     //un bouton pour le state
        public Button indicatorBtn; //un bouton pour l'indicator
        public Button deleteBtn;    //un bouton pour delete
        public MyViewHolder(ConstraintLayout cl) {
            super(cl);
            bookName = cl.findViewById(R.id.book_name);
            stateBtn = cl.findViewById(R.id.state_btn);
            indicatorBtn = cl.findViewById(R.id.indicator_btn);
            deleteBtn = cl.findViewById(R.id.delete_btn);
        }
    }

    public Adapter(List<Book> myDataset, Context context) {
        this.mDataset = myDataset;
        this.context = context;
    }

    //La vue créée pour un item (livre)
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        ConstraintLayout cl = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,
                false);
        MyViewHolder vh = new MyViewHolder(cl);
        return vh;
    }

    //On affecte ici les actions des éléments d'un item
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Book book = mDataset.get(position);
        holder.bookName.setText(book.name);
        UpdateState(holder.stateBtn, book.state);
        UpdateIndicator(holder.indicatorBtn, book.indicator);
        holder.stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.state++;
                if (book.state > 2) {
                    book.state = 0;
                }
                Button stateBtn = view.findViewById(R.id.state_btn);
                UpdateState(stateBtn, book.state);
            }
        });
        holder.indicatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.indicator++;
                if (book.indicator > 2) {
                    book.indicator = 0;
                }
                Button indicatorBtn = view.findViewById(R.id.indicator_btn);
                UpdateIndicator(indicatorBtn, book.indicator);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteBook deleteBookEvt = new DeleteBook();
                deleteBookEvt.deletedBook = book;
                MyBus.bus.post(deleteBookEvt);
            }
        });
    }

    //Retourne la taille de mDataset
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void UpdateState(Button stateBtn, int state) {
        switch (state) {
            case 0:
                stateBtn.setText("Acquired");
                break;
            case 1:
                stateBtn.setText("Borrowed");
                break;
            case 2:
                stateBtn.setText("To buy");
                break;
        }
    }

    public void UpdateIndicator(Button indicatorBtn, int indicator) {
        switch (indicator) {
            case 0:
                indicatorBtn.setText("To read");
                break;
            case 1:
                indicatorBtn.setText("In progress");
                break;
            case 2:
                indicatorBtn.setText("Read");
                break;
        }
    }
}