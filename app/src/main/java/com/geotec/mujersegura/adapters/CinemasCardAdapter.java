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

import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import com.geotec.mujersegura.R;
import com.geotec.mujersegura.others.Complements;

import java.util.ArrayList;

public class CinemasCardAdapter extends RecyclerView.Adapter<CinemasCardAdapter.ViewHolder> {
    private ArrayList<Cinemas> cinemasArrayList;
    private OnItemListener listener;

    private Complements c;

    public CinemasCardAdapter(Context context, ArrayList<Cinemas> cinemasArrayList) {
        this.cinemasArrayList = cinemasArrayList;
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
        viewHolder.lblName.setText(cinemasArrayList.get(position).getName());
        viewHolder.imgImage.setImageBitmap(cinemasArrayList.get(position).getImageBitmap());
        viewHolder.lblDesc.setText(cinemasArrayList.get(position).getDesc());
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
        if (cinemasArrayList.isEmpty()) {
            return 0;
        } else {
            return cinemasArrayList.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return cinemasArrayList.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.card_cinemas, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView crdCinemas;
        private TextView lblName;
        private ImageView imgImage;
        private TextView lblDesc;

        public ViewHolder(View view, final OnItemListener listener) {
            super(view);
            crdCinemas = (CardView) view.findViewById(R.id.card_cinemas);
            lblName = (TextView) view.findViewById(R.id.lbl_name_cc);
            imgImage = (ImageView) view.findViewById(R.id.img_image_cc);
            lblDesc = (TextView) view.findViewById(R.id.lbl_desc_cc);
            crdCinemas.setOnClickListener(new View.OnClickListener() {
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
