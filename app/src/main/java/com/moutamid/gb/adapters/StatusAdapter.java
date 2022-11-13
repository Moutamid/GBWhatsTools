package com.moutamid.gb.adapters;

import static com.moutamid.gb.modules.AdController.LoadFBInterstitial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.moutamid.gb.R;
import com.moutamid.gb.activities.PreviewActivity;
import com.moutamid.gb.modules.Utils;
import com.moutamid.gb.Model.StatusModel;


public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ImageViewHolder> {

    Context context;
    List<StatusModel> arrayList;
    int width;
    LayoutInflater inflater;
    public OnCheckboxListener onCheckboxListener;
    public final static String POSITION = "Image Position";
    com.facebook.ads.InterstitialAd interstitialAd;
    private int position;

    public StatusAdapter(Context context, List<StatusModel> arrayList, OnCheckboxListener onCheckboxListener) {
        this.context = context;
        this.arrayList = arrayList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        width = displayMetrics.widthPixels;

        this.onCheckboxListener = onCheckboxListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_recent, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {


        if (isVideoFile(arrayList.get(position).getFilePath())) {
            holder.play.setVisibility(View.VISIBLE);
        } else {
            holder.play.setVisibility(View.GONE);
        }


        Glide.with(context).load(arrayList.get(position).getFilePath()).into(holder.gridImage);


        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((StatusModel) arrayList.get(position)).setSelected(isChecked);
                if (onCheckboxListener != null) {
                    onCheckboxListener.onCheckboxListener(buttonView, arrayList);
                }
            }
        });

        if(arrayList.get(position).getFileIsThere()){
            holder.saved.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
            holder.saveSS.setVisibility(View.GONE);
        }
        if (arrayList.get(position).isSelected()) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }

        holder.saveSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.saved.setVisibility(View.VISIBLE);
                holder.checkbox.setVisibility(View.GONE);
                holder.saveSS.setVisibility(View.GONE);
                Utils.download(context, arrayList.get(position).getFilePath());
            }
        });
        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PreviewActivity.class);
                intent.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) arrayList);
                intent.putExtra("position", position);
                intent.putExtra("statusdownload", arrayList.get(position).getFileIsThere());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                LoadFBInterstitial(context);

            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gridImage) ImageView gridImage;
        @BindView(R.id.checkbox) CheckBox checkbox;
        @BindView(R.id.play) ImageView play;
        @BindView(R.id.saveSS) ImageView saveSS;
        @BindView(R.id.saved) LinearLayout saved;
        @BindView(R.id.itemCard) CardView itemCard;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");
    }

    public interface OnCheckboxListener {
        void onCheckboxListener(View view, List<StatusModel> list);
    }

    public boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }
}
