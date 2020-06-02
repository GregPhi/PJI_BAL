package com.example.projetbal.listadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projetbal.PretRenduBookActivity;
import com.example.projetbal.R;
import com.example.projetbal.object.FoundLivre;
import com.example.projetbal.object.book.Livre;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PretRenduBookListAdapter extends RecyclerView.Adapter<PretRenduBookListAdapter.BookViewHolder> {

    protected class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView cdItemView;
        private final TextView titleItemView;
        private final TextView etatsItemView;
        private final TextView commItemView;
        private final TextView matiereItemView;
        private final TextView anneeItemView;
        private ImageView statutItemView;
        private final Button bI;
        private final Button bV;

        protected BookViewHolder(final View itemView) {
            super(itemView);
            cdItemView = itemView.findViewById(R.id.codebarre);
            titleItemView = itemView.findViewById(R.id.title);
            etatsItemView = itemView.findViewById(R.id.etats);
            matiereItemView = itemView.findViewById(R.id.matiere);
            anneeItemView = itemView.findViewById(R.id.annee);
            commItemView = itemView.findViewById(R.id.comm);
            statutItemView = itemView.findViewById(R.id.state);
            bI = itemView.findViewById(R.id.infos);
            bI.setActivated(true);
            bV = itemView.findViewById(R.id.bookVerified);
            bV.setActivated(true);
        }
    }

    protected final PretRenduBookActivity mContext;
    protected final LayoutInflater mInflater;
    protected List<FoundLivre> mfoundbooks; // Cached copy of words

    public PretRenduBookListAdapter(PretRenduBookActivity context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_book_statut, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        if (mfoundbooks != null) {
            final FoundLivre currentfound = mfoundbooks.get(position);
            final Livre book = currentfound.getLivre();
            holder.titleItemView.setText(book.getTitle());
            holder.etatsItemView.setText(book.getEtats());
            holder.matiereItemView.setText(book.getMatiere());
            holder.anneeItemView.setText(book.getAnnee());
            holder.cdItemView.setText(book.getCode_barre());
            holder.commItemView.setText(book.getCommenataires());
            holder.statutItemView.setImageResource((currentfound.getFound() ? R.drawable.ic_check_green_24dp : R.drawable.ic_not_check_red_24dp));
            holder.bI.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mContext.infosBook(currentfound);
                }
            });
            holder.bV.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mContext.bookVerified(currentfound);
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

    public void setFoundBooks(List<FoundLivre> books){
        mfoundbooks = books;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        return (mfoundbooks != null) ? mfoundbooks.size(): 0;
    }
}
