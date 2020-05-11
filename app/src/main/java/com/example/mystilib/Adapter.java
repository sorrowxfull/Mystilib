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
    private List<Book> mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView bookName;
        public Button indicatorBtn;
        public Button stateBtn;
        public Button deleteBtn;
        public MyViewHolder(ConstraintLayout cl) {
            super(cl);
            bookName = cl.findViewById(R.id.book_name);
            indicatorBtn = cl.findViewById(R.id.indicator_btn);
            stateBtn = cl.findViewById(R.id.state_btn);
            deleteBtn = cl.findViewById(R.id.delete_btn);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter(List<Book> myDataset, Context context) {
        this.mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        ConstraintLayout cl = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,
                false);
        MyViewHolder vh = new MyViewHolder(cl);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Book book = mDataset.get(position);
        holder.bookName.setText(book.name);
        holder.stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Element " + book.name, Toast.LENGTH_SHORT).show();
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}