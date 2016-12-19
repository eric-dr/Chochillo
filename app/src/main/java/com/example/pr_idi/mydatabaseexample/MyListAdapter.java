package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by eric.domingo on 12/16/16.
 */
public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitulo;
        public TextView mAutor;
        public TextView mAny;
        public TextView mEditorial;
        public TextView mCategoria;
        public RatingBar mValoracio;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mTitulo = (TextView) itemView.findViewById(R.id.txtvw_titol);
            this.mAutor = (TextView) itemView.findViewById(R.id.txtvw_autor);
            this.mAny = (TextView) itemView.findViewById(R.id.txtvw_any);
            this.mEditorial = (TextView) itemView.findViewById(R.id.txtvw_editorial);
            this.mCategoria = (TextView) itemView.findViewById(R.id.txtvw_categoria);
            this.mValoracio = (RatingBar) itemView.findViewById(R.id.rtgbar_valoracio);
        }

    }

    Context mContext;
    List<Book> mList;
    float val;


    public MyListAdapter(Context context, List<Book> myList) {
        mContext = context;
        mList = myList;
    }

    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewRow = LayoutInflater.from(this.mContext).inflate(
                R.layout.recycler_view_custom,
                parent,
                false);

        ViewHolder viewHolder = new MyListAdapter.ViewHolder(viewRow);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyListAdapter.ViewHolder holder, int position) {
        holder.mTitulo.setText(mList.get(position).getTitle());
        holder.mAutor.setText(mList.get(position).getAuthor());
        holder.mAny.setText(mList.get(position).getYear());
        holder.mEditorial.setText(mList.get(position).getPublisher());
        holder.mCategoria.setText(mList.get(position).getCategory());
        if(mList.get(position).getPersonal_evaluation().equals("Molt bo")) val = 5.0f;
        else if(mList.get(position).getPersonal_evaluation().equals("Bo")) val = 4.0f;
        else if(mList.get(position).getPersonal_evaluation().equals("Regular")) val = 3.0f;
        else if(mList.get(position).getPersonal_evaluation().equals("Dolent")) val = 2.0f;
        else if(mList.get(position).getPersonal_evaluation().equals("Molt dolent")) val = 1.0f;
        holder.mValoracio.setRating(val);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(List<Book> list){
        mList = list;
        notifyDataSetChanged();
    }
}
