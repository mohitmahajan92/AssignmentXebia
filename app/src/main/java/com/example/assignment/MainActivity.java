package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.ColorSpace;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    String responseList, statusString;
    private ArrayList<Model> arrayList;

    @BindView(R.id.rvHotelList)
    RecyclerView rvHotelList;

    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);


        getProperties("loading...");


    }


    private void getProperties(String msg) {

        if (!MtUtil.isConnectedToInternet(MainActivity.this)) {
            Utility.showSignUpErrMsg("1", MainActivity.this);

            return;
        }

        if (!TextUtils.isEmpty(msg)) {
            MtUtil.showProgressDialog(msg, MainActivity.this);
        }


        VoidFunction bgWork = new VoidFunction() {
            @Override
            public void function() {
                bgWorkGetList();
            }
        };

        VoidFunction uiWork = new VoidFunction() {
            @Override
            public void function() {
                uiWorkGetPropertiesList();
            }
        };

        new MtThread().execute(bgWork, uiWork);
    }

    private void bgWorkGetList() {


        responseList = MtUtil.makeRequest(MtServer.URL_Base, " ", MainActivity.this, 0);
    }

    private void uiWorkGetPropertiesList() {

        MtUtil.hideProgressDialog();

        try {

            mLayoutManager = new LinearLayoutManager(MainActivity.this);
            rvHotelList.setLayoutManager(mLayoutManager);
            setArticleList();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setArticleList() {
        if (responseList != null) {
            JSONObject jsonObject = null;
            try {

                jsonObject = new JSONObject(responseList);
                if (jsonObject != null) {
                    statusString = jsonObject.optString("status");
                    if (statusString.equals("OK")) {

                        arrayList = new ArrayList<>();

                        JSONArray jsonArray = jsonObject.optJSONArray("results");

                        if (jsonArray != null) {
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject joData = jsonArray.optJSONObject(i);

                                    Model cum = new Model();

                                    cum.strTitle = joData.optString("title");
                                    cum.strDate = joData.optString("published_date");
                                    cum.strAbstract = joData.optString("abstract");
                                    cum.strSource = joData.optString("source");


                                    JSONArray jaMedia = joData.optJSONArray("media");
                                    if (jaMedia != null) {
                                        if (jaMedia.length() > 0) {
                                            cum.arrayListMedia = new ArrayList<>();
                                            for (int q = 0; q < jaMedia.length(); q++) {
                                                JSONObject joMedia = jaMedia.optJSONObject(q);
                                                MediaModel mediaModel = new MediaModel();
                                                JSONArray jaMediaMetaData = joMedia.optJSONArray("media-metadata");
                                               if (jaMediaMetaData != null) {

                                                    if (jaMediaMetaData.length() > 0) {

                                                        mediaModel.mediaMetaDataModels = new ArrayList<>();

                                                        for (int k = 0; k < jaMediaMetaData.length(); k++) {
                                                            JSONObject joMediaMetaData = jaMediaMetaData.optJSONObject(k);
                                                            MediaMetaDataModel mediaMetaDataModel = new MediaMetaDataModel();
                                                            mediaMetaDataModel.strImage = joMediaMetaData.optString("url");
                                                            mediaModel.mediaMetaDataModels.add(mediaMetaDataModel);
                                                        }

                                                    }

                                                }

                                                cum.arrayListMedia.add(mediaModel);


                                            }
                                        }
                                    }


                                    arrayList.add(cum);

                                    mAdapter = new Adapter(MainActivity.this, arrayList);
                                    rvHotelList.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                }

                            }
                        }


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else
            Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
    }

}
