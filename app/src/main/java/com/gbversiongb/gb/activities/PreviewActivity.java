package com.gbversiongb.gb.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;

import com.gbversiongb.gb.R;
import com.gbversiongb.gb.modules.AdController;
import com.gbversiongb.gb.modules.SharedPrefs;
import com.gbversiongb.gb.modules.Utils;
import com.gbversiongb.gb.Model.StatusModel;
import com.gbversiongb.gb.adapters.PreviewAdapter;

public class PreviewActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<StatusModel> imageList;
    int position;

    LinearLayout downloadIV, shareIV, deleteIV, wAppIV;
    PreviewAdapter previewAdapter;
    boolean statusDownload;
    ImageView backIV;
    private com.facebook.ads.AdView faceBookBanner;
    private LinearLayout bannerBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        Utils.setLanguage(PreviewActivity.this, SharedPrefs.getLang(PreviewActivity.this));

        backIV = findViewById(R.id.backIV);


        viewPager = findViewById(R.id.viewPager);

        shareIV = findViewById(R.id.shareIV);

        downloadIV = findViewById(R.id.downloadIV);

        deleteIV = findViewById(R.id.deleteIV);
        wAppIV = findViewById(R.id.repostIV);


        imageList = getIntent().getParcelableArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);
        statusDownload = getIntent().getBooleanExtra("statusdownload", false);


        if (statusDownload) {
            downloadIV.setVisibility(View.GONE);
        } else {
            downloadIV.setVisibility(View.VISIBLE);
        }

        previewAdapter = new PreviewAdapter(PreviewActivity.this, imageList);
        viewPager.setAdapter(previewAdapter);
        viewPager.setCurrentItem(position);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                File myFile, file;
                file = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());
                File extStore = Environment.getExternalStorageDirectory();
                if (isVideoFile(imageList.get(viewPager.getCurrentItem()).getFilePath()))
                    myFile = new File(extStore.getAbsolutePath() + "/Download" + File.separator + getResources().getString(R.string.app_name) + "/Videos" + File.separator + file.getName());
                else
                    myFile = new File(extStore.getAbsolutePath() + "/Download" + File.separator + getResources().getString(R.string.app_name) + "/Images" + File.separator + file.getName());


                if (myFile.exists()) {
                    downloadIV.setVisibility(View.GONE);
                } else {
                    downloadIV.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        downloadIV.setOnClickListener(clickListener);
        shareIV.setOnClickListener(clickListener);
        deleteIV.setOnClickListener(clickListener);
        backIV.setOnClickListener(clickListener);
        wAppIV.setOnClickListener(clickListener);

        LinearLayout container = findViewById(R.id.banner_container);
        bannerBox = (LinearLayout) findViewById(R.id.BannerBox);

        AdController.loadAdFifty(this, container);
    }
    private ViewPager.OnPageChangeListener mPageLitsener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            File myFile, file;
            file = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());
            File extStore = Environment.getExternalStorageDirectory();
            if (isVideoFile(imageList.get(viewPager.getCurrentItem()).getFilePath()))
                myFile = new File(extStore.getAbsolutePath() + "/Download" + File.separator + getResources().getString(R.string.app_name) + "/Videos" + File.separator + file.getName());
            else
                myFile = new File(extStore.getAbsolutePath() + "/Download" + File.separator + getResources().getString(R.string.app_name) + "/Images" + File.separator + file.getName());


            if (myFile.exists()) {
                downloadIV.setVisibility(View.GONE);
            } else {
                downloadIV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };
    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backIV:
                    finish();
                    break;

                case R.id.downloadIV:
                    if (imageList.size() > 0) {


                        try {
                            downloadIV.setVisibility(View.GONE);
                            Utils.download(PreviewActivity.this, imageList.get(viewPager.getCurrentItem()).getFilePath());
                            Toast.makeText(PreviewActivity.this, getResources().getString(R.string.saved_success), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(PreviewActivity.this, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        finish();
                    }
                    break;

                case R.id.shareIV:
                    if (imageList.size() > 0) {
                        if (isImageFile(imageList.get(viewPager.getCurrentItem()).getFilePath())) {
                            File imageFileToShare = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());

                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            share.setType("image/*");
                            Uri photoURI = FileProvider.getUriForFile(
                                    getApplicationContext(), getApplicationContext()
                                            .getPackageName() + ".provider", imageFileToShare);
                            share.putExtra(Intent.EXTRA_STREAM,
                                    photoURI);
                            startActivity(Intent.createChooser(share, "Share via"));

                        } else if (isVideoFile(imageList.get(viewPager.getCurrentItem()).getFilePath())) {

                            Uri videoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext()
                                    .getPackageName() + ".provider", new File(imageList.get(viewPager.getCurrentItem()).getFilePath()));
                            Intent videoshare = new Intent(Intent.ACTION_SEND);
                            videoshare.setType("*/*");
                            videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            videoshare.putExtra(Intent.EXTRA_STREAM, videoURI);

                            startActivity(videoshare);
                        }
                    } else {
                        finish();
                    }
                    break;

                case R.id.deleteIV:
                    if (imageList.size() > 0) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreviewActivity.this);
                        alertDialog.setTitle(R.string.confirm);
                        alertDialog.setMessage(R.string.del_status);
                        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                int currentItem = 0;

                                File file = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());
                                if (file.exists()) {
                                    boolean del = file.delete();
                                    if (imageList.size() > 0 && viewPager.getCurrentItem() < imageList.size()) {
                                        currentItem = viewPager.getCurrentItem();
                                    }
                                    imageList.remove(viewPager.getCurrentItem());
                                    previewAdapter = new PreviewAdapter(PreviewActivity.this, imageList);
                                    viewPager.setAdapter(previewAdapter);

                                    Intent intent = new Intent();
                                    setResult(10, intent);

                                    if (imageList.size() > 0) {
                                        viewPager.setCurrentItem(currentItem);
                                    } else {
                                        finish();
                                    }
                                }
                            }
                        });
                        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        finish();
                    }
                    break;

                case R.id.repostIV:
                    if (isImageFile(imageList.get(viewPager.getCurrentItem()).getFilePath())) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        share.setPackage("com.whatsapp");
                        File imageFileToShare = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext()
                                .getPackageName() + ".provider", imageFileToShare);
                        share.putExtra(Intent.EXTRA_STREAM, photoURI);
                        startActivity(Intent.createChooser(share, "Share Image!"));
                    } else if (isVideoFile(imageList.get(viewPager.getCurrentItem()).getFilePath())) {
                        Uri videoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext()
                                .getPackageName() + ".provider", new File(imageList.get(viewPager.getCurrentItem()).getFilePath()));
                        Intent videoshare = new Intent(Intent.ACTION_SEND);
                        videoshare.setType("*/*");
                        videoshare.setPackage("com.whatsapp");
                        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        videoshare.putExtra(Intent.EXTRA_STREAM, videoURI);
                        startActivity(videoshare);
                    }
                    break;

                default:
                    break;
            }
        }
    };



    public boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

}
