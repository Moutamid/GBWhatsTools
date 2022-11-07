package com.gbversiongb.gb.fragments;

import static com.gbversiongb.gb.modules.AdController.LoadFBInterstitial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gbversiongb.gb.R;
import com.gbversiongb.gb.modules.AdController;
import com.gbversiongb.gb.Model.StatusModel;
import com.gbversiongb.gb.adapters.MyStatusAdapter;

public class MyPhotos extends Fragment implements MyStatusAdapter.OnCheckboxListener {

    GridView imageGrid;
    ArrayList<StatusModel> f = new ArrayList<>();
    MyStatusAdapter myAdapter;
    int save = 10;
    ArrayList<StatusModel> filesToDelete = new ArrayList<>();
    LinearLayout actionLay, deleteIV;
    CheckBox selectAll;
    RelativeLayout loaderLay, emptyLay;
    com.facebook.ads.InterstitialAd interstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_status_fragment, container, false);

        loaderLay = rootView.findViewById(R.id.loaderLay);
        emptyLay = rootView.findViewById(R.id.emptyLay);

        imageGrid = rootView.findViewById(R.id.videoGrid);
        populateGrid();

        actionLay = rootView.findViewById(R.id.actionLay);
        deleteIV = rootView.findViewById(R.id.deleteIV);
        deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!filesToDelete.isEmpty()) {
                    new AlertDialog.Builder(getContext())
                            .setMessage(getResources().getString(R.string.delete_alert))
                            .setCancelable(true)
                            .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
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
                                        Toast.makeText(getContext(), getResources().getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
                                    } else if (success == 1) {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                    }
                                    actionLay.setVisibility(View.GONE);
                                    selectAll.setChecked(false);
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                }
            }
        });

        selectAll = rootView.findViewById(R.id.selectAll);
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                LoadFBInterstitial(getActivity());
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
            }
        });

        return rootView;
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
                myAdapter = new MyStatusAdapter(MyPhotos.this, f, MyPhotos.this);
                imageGrid.setAdapter(myAdapter);
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
        File file = new File(
                Environment
                        .getExternalStorageDirectory().toString() + File.separator + "Download" + File.separator + getResources().getString(R.string.app_name) + "/Images");
        f = new ArrayList<>();
        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (int i = 0; i < listFile.length; i++) {
                f.add(new StatusModel(listFile[i].getAbsolutePath(), false));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myAdapter.onActivityResult(requestCode, resultCode, data);
        if (requestCode == save && resultCode == save) {
            myAdapter.notifyDataSetChanged();

            getFromSdcard();
            myAdapter = new MyStatusAdapter(MyPhotos.this, f, MyPhotos.this);
            imageGrid.setAdapter(myAdapter);

            actionLay.setVisibility(View.GONE);
            selectAll.setChecked(false);
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
        if (filesToDelete.size() == f.size()) {
            selectAll.setChecked(true);
        }
        if (!filesToDelete.isEmpty()) {
            actionLay.setVisibility(View.VISIBLE);
            return;
        }
        selectAll.setChecked(false);
        actionLay.setVisibility(View.GONE);
    }
}
