package com.geotec.mujersegura.adapters;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.geotec.mujersegura.R;
import com.geotec.mujersegura.others.Complements;

import java.util.ArrayList;

public class MoviesCardAdapter extends RecyclerView.Adapter<MoviesCardAdapter.ViewHolder> {
    private ArrayList<Movies> moviesArrayList;
    private OnItemListener listener;

    private Complements c;

    public MoviesCardAdapter(Context context, ArrayList<Movies> moviesArrayList) {
        this.moviesArrayList = moviesArrayList;
        c = new Complements(context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //super.onBindViewHolder(viewHolder, position, payloads);
        //TextView nombreTextView = viewHolder.nombre;
        /*if (position == derechosArrayList.size() - 1) {
            viewHolder.pbrDerechos.setVisibility(View.VISIBLE);
        }*/
        //if (derechosArrayList.get(position) != null) {
        viewHolder.lblName.setText(moviesArrayList.get(position).getName());
        viewHolder.imgImage.setImageBitmap(moviesArrayList.get(position).getImageBitmap());
        viewHolder.lblDesc.setText(moviesArrayList.get(position).getDesc());
        viewHolder.lblDetails.setText(
                "Clasificación: " + moviesArrayList.get(position).getClasification() +
                " - " + moviesArrayList.get(position).getGenre() +
                " (" + moviesArrayList.get(position).getDuration() + " minutos)"
        );
        /*} else {
            viewHolder.crdDerechos.setVisibility(View.GONE);
            viewHolder.pbrDerechos.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int centerX = 0, centerY = 0;
            float startRadius = 0, endRadius = Math.max(viewHolder.itemView.getWidth(), viewHolder.itemView.getHeight());
            Animator animator = ViewAnimationUtils.createCircularReveal(viewHolder.itemView, centerX, centerY, startRadius, endRadius);
            viewHolder.itemView.setVisibility(View.VISIBLE);
            animator.start();
        }
    }

    @Override
    public int getItemCount() {
        if (moviesArrayList.isEmpty()) {
            return 0;
        } else {
            return moviesArrayList.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return moviesArrayList.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.card_movies, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView crdMovies;
        private TextView lblName;
        private TextView lblDesc;
        private ImageView imgImage;
        private TextView lblDetails;
        private ProgressBar pbrDerechos;

        public ViewHolder(View view, final OnItemListener listener) {
            super(view);
            crdMovies = (CardView) view.findViewById(R.id.card_movies);
            lblName = (TextView) view.findViewById(R.id.lbl_name_cm);
            lblDesc = (TextView) view.findViewById(R.id.lbl_desc_cm);
            imgImage = (ImageView) view.findViewById(R.id.img_image_cm);
            lblDetails = (TextView) view.findViewById(R.id.lbl_details_cm);
            pbrDerechos = (ProgressBar) view.findViewById(R.id.pbr_load_online_movies);
            crdMovies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            //listener.onDownload(position);
                            listener.showFunctions(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemListener {
        //void onDownload(int position);
        void showFunctions(int position);
    }

    public void setOnItemListener(OnItemListener listener) {
        this.listener = listener;
    }
}
