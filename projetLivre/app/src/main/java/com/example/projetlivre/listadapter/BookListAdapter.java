package com.example.projetlivre.listadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projetlivre.ListeNewBookActivity;
import com.example.projetlivre.R;
import com.example.projetlivre.object.Book;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleItemView;
        private final TextView descriptionItemView;
        private final TextView matiereItemView;
        private final TextView anneeItemView;
        private final Button bD;
        private final Button bI;

        private BookViewHolder(final View itemView) {
            super(itemView);
            titleItemView = itemView.findViewById(R.id.title);
            descriptionItemView = itemView.findViewById(R.id.description);
            matiereItemView = itemView.findViewById(R.id.matiere);
            anneeItemView = itemView.findViewById(R.id.annee);
            bD = itemView.findViewById(R.id.delete_book);
            bD.setActivated(true);
            bI = itemView.findViewById(R.id.infos);
            bI.setActivated(true);
        }
    }

    private final ListeNewBookActivity mContext;
    private final LayoutInflater mInflater;
    private List<Book> mbooks; // Cached copy of words

    public BookListAdapter(ListeNewBookActivity context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_book, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        if (mbooks != null) {
            final Book current = mbooks.get(position);
            holder.titleItemView.setText(current.getTitle());
            holder.descriptionItemView.setText(current.getDescription());
            holder.matiereItemView.setText(current.getMatiere());
            holder.anneeItemView.setText(current.getAnnee());
            /*holder.bD.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mContext.removeBook(current);
                }
            });
            holder.bI.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mContext.infosBook(current);
                }
            });*/
        } else {
            // Covers the case of data not being ready yet.
            holder.titleItemView.setText("Titre");
            holder.descriptionItemView.setText("Description");
            holder.matiereItemView.setText("Matiere");
            holder.anneeItemView.setText("Annee");
        }
    }

    public void setBooks(List<Book> books){
        mbooks = books;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        return (mbooks != null) ? mbooks.size(): 0;
    }
}

