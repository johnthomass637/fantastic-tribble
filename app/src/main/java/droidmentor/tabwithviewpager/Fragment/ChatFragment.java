package droidmentor.tabwithviewpager.Fragment;





import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import droidmentor.tabwithviewpager.R;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;

public class ChatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private DashboardAdapter mAdapter;
    public ChatFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        mRVFishPrice = (RecyclerView) rootView.findViewById(R.id.idRecyclerViewVerticalList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        new AsyncFetch().execute();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                mSwipeRefreshLayout.post(new Runnable() {
                                             @Override
                                             public void run() {


                                                 mSwipeRefreshLayout.setRefreshing(true);

                                                 new AsyncFetch().execute();
                                             }
                                         }
                );

            }
        });

        //new AsyncFetch().execute();

        return rootView;
    }

    @Override
    public void onRefresh() {
        new AsyncFetch().execute();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public class AsyncFetch extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            String stringUrl = "http://ec2-18-206-211-81.compute-1.amazonaws.com:3000/api/";
            String result;
            String inputLine;
     try {
        //Create a URL object holding our url
        URL myUrl = new URL(stringUrl);
        //Create a connection
        HttpURLConnection connection =(HttpURLConnection)
                myUrl.openConnection();
        //Set methods and timeouts
        connection.setRequestMethod(REQUEST_METHOD);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);

        //Connect to our url
        connection.connect();
        //Create a new InputStreamReader
        InputStreamReader streamReader = new
                InputStreamReader(connection.getInputStream());
        //Create a new buffered reader and String Builder
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        //Check if the line we are reading is not null
        while((inputLine = reader.readLine()) != null){
            stringBuilder.append(inputLine);
        }
        //Close our InputStream and Buffered reader
        reader.close();
        streamReader.close();
        //Set our result equal to our stringBuilder
        result = stringBuilder.toString();
    }
     catch(IOException e){
         e.printStackTrace();
         result = null;
     }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

           // pdLoading.dismiss();
            List<dashboardclass> data=new ArrayList<>();

           // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    dashboardclass dashboard = new dashboardclass();
                   // dashboard.Name= json_data.getString("fish_img");
                    dashboard.Name= json_data.getString("memname");
                    dashboard.caseID= json_data.getString("_id");
                    String lastCaseidDigit= dashboard.caseID.substring( dashboard.caseID.length() - 5);
                    dashboard.caseID=lastCaseidDigit.toUpperCase();
                 //   dashboard.sizeName= json_data.getString("size_name");
                    dashboard.Status= json_data.getString("MMSStatus");
                    data.add(dashboard);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new DashboardAdapter(getActivity(), data);

                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(getActivity()));

            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

        @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


            inflater.inflate(R.menu.menu_chat_fragment, menu);
            super.onCreateOptionsMenu(menu, inflater);
    }

}
