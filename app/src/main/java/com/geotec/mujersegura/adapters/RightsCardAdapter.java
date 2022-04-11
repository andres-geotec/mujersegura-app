package com.geotec.mujersegura.adapters;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.geotec.mujersegura.R;
import com.geotec.mujersegura.others.Complements;

import java.util.ArrayList;

public class RightsCardAdapter extends RecyclerView.Adapter<RightsCardAdapter.ViewHolder> {

    private ArrayList<Rights> rightsArrayList;
    private OnItemListener listener;

    private Complements C;

    public RightsCardAdapter(Context context, ArrayList<Rights> rightsArrayList) {
        this.rightsArrayList = rightsArrayList;
        C = new Complements(context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        //super.onBindViewHolder(holder, position, payloads);
        viewHolder.lblName          .setText(rightsArrayList.get(position).getName());
        viewHolder.lblDescription   .setText(rightsArrayList.get(position).getDescription());
        viewHolder.lblSource        .setText(rightsArrayList.get(position).getSource());
        if (rightsArrayList.get(position).isDownload()) {
            viewHolder.btnDownload.setImageResource(R.drawable.ic_delete_sweep_red);
        } else {
            viewHolder.btnDownload.setImageResource(R.drawable.ic_cloud_download_blue);
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

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
        if (rightsArrayList.isEmpty()) {
            return 0;
        } else {
            return rightsArrayList.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return rightsArrayList.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.card_rights, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView crdRights;
        private TextView lblName, lblDescription, lblSource;
        private ImageButton btnDownload;
        private ProgressBar pbrRights;

        public ViewHolder(View view, final OnItemListener listener) {
            super(view);
            crdRights       = (CardView) view.findViewById(R.id.card_right);
            lblName         = (TextView) view.findViewById(R.id.lbl_right_name);
            lblDescription  = (TextView) view.findViewById(R.id.lbl_right_description);
            lblSource       = (TextView) view.findViewById(R.id.lbl_right_source);
            btnDownload     = (ImageButton) view.findViewById(R.id.btn_right_download);
            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDownload(position);
                        }
                    }
                }
            });
            pbrRights = (ProgressBar) view.findViewById(R.id.pbr_right_load_online);
        }
    }

    public interface OnItemListener {
        void onDownload(int position);
    }
    public void setOnItemListener(OnItemListener listener) {
        this.listener = listener;
    }
}
