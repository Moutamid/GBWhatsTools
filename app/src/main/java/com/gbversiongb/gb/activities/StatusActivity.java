package com.gbversiongb.gb.activities;

import static com.gbversiongb.gb.modules.AdController.LoadFBInterstitial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gbversiongb.gb.R;
import com.gbversiongb.gb.modules.AdController;
import com.gbversiongb.gb.modules.Utils;
import com.gbversiongb.gb.adapters.StatusAdapter;
import com.gbversiongb.gb.Model.StatusModel;

public class StatusActivity extends AppCompatActivity implements StatusAdapter.OnCheckboxListener {

    ArrayList<StatusModel> f = new ArrayList<>();
    StatusAdapter myAdapter;
    ArrayList<StatusModel> filesToDelete = new ArrayList<>();
    LinearLayout actionLay, downloadIV, deleteIV;
    CheckBox selectAll;
    RelativeLayout loaderLay, emptyLay;
    TextView typeSS;
    String type;
    File file, myFile;
    boolean fileIsThere = false;
    LinearLayout bannerBox;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_fragment);

        typeSS = findViewById(R.id.typeSS);
        recyclerView = findViewById(R.id.recylerViewImage);

        bannerBox = findViewById(R.id.bannerBox);
        AdController.loadAdFifty(this, bannerBox);

        findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                type = extras.getString("type");
                if (type.equals("WA")) {
                    typeSS.setText("WhatsApp");
                } else if (type.equals("WAGB")) {
                    typeSS.setText("WhatsApp GB");
                } else if (type.equals("WABS")) {
                    typeSS.setText("WhatsApp Business");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loaderLay = findViewById(R.id.loaderLay);
        emptyLay = findViewById(R.id.emptyLay);

        populateGrid();

        actionLay = findViewById(R.id.actionLay);
        deleteIV = findViewById(R.id.deleteIV);
        deleteIV.setOnClickListener(view -> {

            if (!filesToDelete.isEmpty()) {
                new AlertDialog.Builder(StatusActivity.this)
                        .setMessage(getResources().getString(R.string.delete_alert))
                        .setCancelable(true)
                        .setNegativeButton(getResources().getString(R.string.yes), (dialogInterface, i) -> {
                            int success = -1;
                            ArrayList<StatusModel> deletedFiles = new ArrayList<>();

                            for (StatusModel details : filesToDelete) {
                                File file = new File(details.getFilePath());
                                if (file.exists()) {
                                    if (file.delete()) {
                                        deletedFiles.add(details);
                                        if (success == 0) {
                                            return;
                                        }
                                        success = 1;
                                    } else {
                                        success = 0;
                                    }
                                } else {
                                    success = 0;
                                }
                            }

                            filesToDelete.clear();
                            for (StatusModel deletedFile : deletedFiles) {
                                f.remove(deletedFile);
                            }
                            myAdapter.notifyDataSetChanged();
                            if (success == 0) {
                                Toast.makeText(StatusActivity.this, getResources().getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(StatusActivity.this, getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                            }
                            actionLay.setVisibility(View.GONE);
                            selectAll.setChecked(false);
                        })
                        .setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }
        });

        downloadIV = findViewById(R.id.downloadIV);
        downloadIV.setOnClickListener(view -> {

LoadFBInterstitial(StatusActivity.this);
            if (!filesToDelete.isEmpty()) {

                int success = -1;
                ArrayList<StatusModel> deletedFiles = new ArrayList<>();

                for (StatusModel details : filesToDelete) {
                    File file = new File(details.getFilePath());
                    if (file.exists()) {
                        if (Utils.download(StatusActivity.this, details.getFilePath())) {
                            deletedFiles.add(details);
                            if (success == 0) {
                                return;
                            }
                            success = 1;
                        } else {
                            success = 0;
                        }
                    } else {
                        success = 0;
                    }

                }

                filesToDelete.clear();
                for (StatusModel deletedFile : deletedFiles) {
                    f.contains(deletedFile.selected = false);
                }
                myAdapter.notifyDataSetChanged();
                if (success == 0) {
                    Toast.makeText(StatusActivity.this, getResources().getString(R.string.save_error), Toast.LENGTH_SHORT).show();
                } else if (success == 1) {
                    Toast.makeText(StatusActivity.this, getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                }
                actionLay.setVisibility(View.GONE);
                selectAll.setChecked(false);
            }
        });

        selectAll = findViewById(R.id.selectAll);
        selectAll.setOnCheckedChangeListener((compoundButton, b) -> {


            LoadFBInterstitial(StatusActivity.this);
            if (!compoundButton.isPressed()) {
                return;
            }

            filesToDelete.clear();

            for (int i = 0; i < f.size(); i++) {
                if (!f.get(i).selected) {
                    b = true;
                    break;
                }
            }

            if (b) {
                for (int i = 0; i < f.size(); i++) {
                    f.get(i).selected = true;
                    filesToDelete.add(f.get(i));
                }
                selectAll.setChecked(true);
            } else {
                for (int i = 0; i < f.size(); i++) {
                    f.get(i).selected = false;
                }
                actionLay.setVisibility(View.GONE);
            }
            myAdapter.notifyDataSetChanged();
        });
    }

    public void populateGrid() {
        new loadDataAsync().execute();
    }

    class loadDataAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderLay.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getFromSdcard();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new Handler().postDelayed(() -> {
                myAdapter = new StatusAdapter(StatusActivity.this, f, StatusActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(StatusActivity.this, 2));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();


                loaderLay.setVisibility(View.GONE);

                if (f == null || f.size() == 0) {
                    emptyLay.setVisibility(View.VISIBLE);
                } else {
                    emptyLay.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }

    public void getFromSdcard() {
        if (type.equals("WA")) {
            file = getWhatsupFolder();
        } else if (type.equals("WAGB")) {
            file = getWhatsupGFolder();
        } else if (type.equals("WABS")) {
            file = getWhatsupBFolder();
        }
        f = new ArrayList<>();
        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            if (listFile != null) {
                Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                for (int i = 0; i < listFile.length; i++) {
                    File extStore = Environment.getExternalStorageDirectory();
                    if (isVideoFile(listFile[i].getAbsolutePath()))
                        myFile = new File(extStore.getAbsolutePath() + "/Download" + File.separator + getResources().getString(R.string.app_name) + "/Videos" + File.separator + listFile[i].getName());
                    else
                        myFile = new File(extStore.getAbsolutePath() + "/Download" + File.separator + getResources().getString(R.string.app_name) + "/Images" + File.separator + listFile[i].getName());
                    if (!listFile[i].getAbsolutePath().contains(".nomedia"))
                        f.add(new StatusModel(listFile[i].getAbsolutePath(), myFile.exists()));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (filesToDelete.size() > 0) {
            filesToDelete.clear();
            selectAll.setChecked(false);
            for (int i = 0; i < f.size(); i++) {
                f.get(i).selected = false;
            }
            actionLay.setVisibility(View.GONE);
            myAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myAdapter.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            myAdapter.notifyDataSetChanged();

            getFromSdcard();


            myAdapter = new StatusAdapter(StatusActivity.this, f, StatusActivity.this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(StatusActivity.this, 2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            actionLay.setVisibility(View.GONE);
            selectAll.setChecked(false);
        }
    }


    public boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public File getWhatsupFolder() {
        if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + ".Statuses").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + ".Statuses");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + ".Statuses");
        }
    }

    public File getWhatsupBFolder() {
        if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business" + File.separator + "Media" + File.separator + ".Statuses").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business" + File.separator + "Media" + File.separator + ".Statuses");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp Business" + File.separator + "Media" + File.separator + ".Statuses");
        }
    }

    public File getWhatsupGFolder() {
        if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp.w4b/GBWhatsApp" + File.separator + "Media" + File.separator + ".Statuses").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp.w4b/GBWhatsApp" + File.separator + "Media" + File.separator + ".Statuses");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "GBWhatsApp" + File.separator + "Media" + File.separator + ".Statuses");
        }
    }


    @Override
    public void onCheckboxListener(View view, List<StatusModel> list) {
        filesToDelete.clear();
        for (StatusModel details : list) {
            if (details.isSelected()) {
                filesToDelete.add(details);
            }
        }
        selectAll.setChecked(filesToDelete.size() == f.size());
        if (!filesToDelete.isEmpty()) {
            actionLay.setVisibility(View.VISIBLE);
            return;
        }

        selectAll.setChecked(false);
        actionLay.setVisibility(View.GONE);
    }
}
