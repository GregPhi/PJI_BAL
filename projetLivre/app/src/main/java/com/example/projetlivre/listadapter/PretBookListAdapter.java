package com.example.projetlivre.listadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projetlivre.PretBookActivity;
import com.example.projetlivre.R;
import com.example.projetlivre.object.Livre;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PretBookListAdapter extends RecyclerView.Adapter<PretBookListAdapter.BookViewHolder> {

    protected class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView cdItemView;
        private final TextView titleItemView;
        private final TextView etatsItemView;
        private final TextView commItemView;
        private final TextView matiereItemView;
        private final TextView anneeItemView;
        private final ImageView imgItemView;
        private final Button bI;

        protected BookViewHolder(final View itemView) {
            super(itemView);
            cdItemView = itemView.findViewById(R.id.codebarre);
            titleItemView = itemView.findViewById(R.id.title);
            etatsItemView = itemView.findViewById(R.id.etats);
            matiereItemView = itemView.findViewById(R.id.matiere);
            anneeItemView = itemView.findViewById(R.id.annee);
            commItemView = itemView.findViewById(R.id.comm);
            imgItemView = itemView.findViewById(R.id.state);
            bI = itemView.findViewById(R.id.infos);
            bI.setActivated(true);
        }
    }

    protected final PretBookActivity mContext;
    protected final LayoutInflater mInflater;
    protected List<Livre> mbooks; // Cached copy of words

    public PretBookListAdapter(PretBookActivity context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyvlerview_book_statut, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        if (mbooks != null) {
            final Livre current = mbooks.get(position);
            holder.titleItemView.setText(current.getTitle());
            holder.etatsItemView.setText(current.getEtats());
            holder.matiereItemView.setText(current.getMatiere());
            holder.anneeItemView.setText(current.getAnnee());
            holder.cdItemView.setText(current.getCode_barre());
            holder.commItemView.setText(current.getCommenataires());
            holder.bI.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mContext.infoBook(current);
                }
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.titleItemView.setText("Titre");
            holder.etatsItemView.setText("Etats");
            holder.matiereItemView.setText("Matiere");
            holder.anneeItemView.setText("Annee");
            holder.cdItemView.setText("Code barres");
            holder.commItemView.setText("Commentaires");
        }
    }


    public void setBooks(List<Livre> books){
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

