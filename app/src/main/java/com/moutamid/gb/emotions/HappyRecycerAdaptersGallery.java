package com.moutamid.gb.emotions;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.gb.R;

class HappyRecycerAdaptersGallery extends RecyclerView.Adapter<HappyRecycerAdaptersGallery.MyViewHolder> {
    private String[] HappyAscii;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView Share;
        TextView TextFaces;
        ImageView WhatsApp;

        MyViewHolder(View itemView) {
            super(itemView);
            this.TextFaces = itemView.findViewById(R.id.txt);
            this.WhatsApp = itemView.findViewById(R.id.whats);
            this.Share = itemView.findViewById(R.id.share);
        }
    }

    HappyRecycerAdaptersGallery(FragmentActivity activity, String[] HappyAscii) {
        this.context = activity;
        this.HappyAscii = HappyAscii;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listfacis, parent, false));
    }

    @SuppressLint({"LongLogTag"})
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.TextFaces.setText(this.HappyAscii[position]);
        holder.WhatsApp.setOnClickListener(v -> {
            String FACE = HappyRecycerAdaptersGallery.this.HappyAscii[position];
            ((ClipboardManager) HappyRecycerAdaptersGallery.this.context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copied Text", FACE));
            Toast.makeText(HappyRecycerAdaptersGallery.this.context, "Copied To Clipboard", Toast.LENGTH_SHORT).show();

            /*Intent whatsappIntent = new Intent("android.intent.action.SEND");
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra("android.intent.extra.TEXT", FACE);
            try {
                HappyRecycerAdaptersGallery.this.context.startActivity(whatsappIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(HappyRecycerAdaptersGallery.this.context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
            }*/
        });

        //Share button clicked event
        holder.Share.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String FACE = HappyRecycerAdaptersGallery.this.HappyAscii[position];
                Intent whatsappIntent = new Intent("android.intent.action.SEND");
                whatsappIntent.setType("text/plain");
                whatsappIntent.putExtra("android.intent.extra.TEXT", FACE);
                try {
                    HappyRecycerAdaptersGallery.this.context.startActivity(whatsappIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(HappyRecycerAdaptersGallery.this.context, "Some problems", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int getItemCount() {
        return (this.HappyAscii == null) ? 0 : this.HappyAscii.length;
    }
}
