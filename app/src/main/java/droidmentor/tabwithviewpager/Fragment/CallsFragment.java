package droidmentor.tabwithviewpager.Fragment;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import droidmentor.tabwithviewpager.R;



import static android.app.Activity.RESULT_OK;
import static android.speech.tts.TextToSpeech.QUEUE_ADD;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallsFragment extends Fragment implements TextToSpeechIniListener {
    private RecyclerView recyclerView;
    private ChatAdapter mAdapter;
    private ArrayList messageArrayList;
    private EditText inputMessage;
    private ImageButton btnSend;
    private ImageButton btnRecord;
    JSONArray optionArray;
    //private Map<String,Object> context = new HashMap<>();
//    com.ibm.watson.developer_cloud.assistant.v1.model.Context context = null;
//    StreamPlayer streamPlayer;
    private boolean initialRequest;
    private boolean permissionToRecordAccepted = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String TAG = "MainActivity";
    private static final int RECORD_REQUEST_CODE = 101;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private boolean listening = false;
    public boolean flag1=false;
    public String valuetoSend="";
    //    private SpeechToText speechService;
//    private MicrophoneInputStream capture;
//    private SpeakerLabelsDiarization.RecoTokens recoTokens;
//    private MicrophoneHelper microphoneHelper;
    String myResponse = null;
    public JSONArray respJsonArray;
    public String outputText = "";
    public String session = null;
    private TextToSpeechInitializer i;
    private static android.speech.tts.TextToSpeech talk;
    public String  temprry="";
    String  temp="";
    public boolean numbercheck=false;

    ;
    private boolean flag = false;
    ArrayList<String> mylist = new ArrayList<String>();


    public CallsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public void onSucces(TextToSpeech tts) {
        this.talk = tts;
        flag = true;

    }

    @Override
    public void onFailure(TextToSpeech tts) {
        flag = false;
        getActivity().finish();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calls, container, false);
        inputMessage = rootView.findViewById(R.id.message);
        btnSend = rootView.findViewById(R.id.btn_send);
        btnRecord = rootView.findViewById(R.id.btn_record);

        String customFont = "Montserrat-Regular.ttf";
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), customFont);

        inputMessage.setTypeface(typeface);
        recyclerView = rootView.findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();
        mAdapter = new ChatAdapter(getActivity(),messageArrayList);
        //  microphoneHelper = new MicrophoneHelper(this);

        //  final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        this.inputMessage.setText("");
        this.initialRequest = true;
        i = new TextToSpeechInitializer(getActivity(), Locale.UK, this);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection()) {
                    sendMessage();
                }
            }
        });
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makeRequest();
                askSpeechInput();
                //recordMessage();
            }
        });
        sendMessage();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Message audioMessage;
                try {


                    audioMessage =(Message) messageArrayList.get(position);
                    if(audioMessage.getId().equals("3"))
                    {
                        valuetoSend=audioMessage.getMessage();
                        flag1=true;
                        sendMessage();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Thread thread = new Thread(new Runnable() {
//                    public void run() {
//                        Message audioMessage;
//                        try {
//
//
//                            audioMessage =(Message) messageArrayList.get(position);
//                            if(audioMessage.getId().equals("3"))
//                            {
//                                valuetoSend=audioMessage.getMessage();
//                                flag1=true;
//                                sendMessage();
//
//                            }
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                thread.start();

            }
            @Override
            public void onLongClick(View view, int position) {
                //recordMessage();

            }
        }));
        return rootView;
    }

    private void sendMessage() {

        final   String inputmessage = this.inputMessage.getText().toString().trim();
        final Message inputMessage = new Message();
        if(!this.initialRequest) {

            Message Inputfrombtn=new Message();
            if (flag1)

            {

                inputMessage.setMessage(valuetoSend);
                flag1=false;
            }
            else {
                inputMessage.setMessage(inputmessage);
            }
            inputMessage.setId("1");
            if(!(inputMessage.getMessage().isEmpty())) {
                messageArrayList.add(inputMessage);
            }
        }
        else
        {

            inputMessage.setMessage(inputmessage);
            inputMessage.setId("100");
            this.initialRequest = false;
            Toast.makeText(getActivity().getApplicationContext(),"Tap on the message for Voice",Toast.LENGTH_LONG).show();

        }
        this.inputMessage.setText("");
        mAdapter.notifyDataSetChanged();
        //code by kevin
        if (mAdapter.getItemCount() > 1) {
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() );

        }



        Thread thread = new Thread(new Runnable(){
            public void run() {
                try {

                    //code by kevin to fetch the iceXD response
                    if(!(inputMessage.getMessage().isEmpty())) {

                        HttpPost httpPost = new HttpPost("http://ice-xd.southindia.cloudapp.azure.com:14121/api/conversation/master/dialogue");
                        //System.out.println("this is th transcript"+transcript);
                        httpPost.addHeader("Content-type", "application/json");
                        JSONObject jsonobj = new JSONObject();
                        jsonobj.put("request", inputMessage.getMessage());
                        //jsonobj.put("userId", "1001");
                        //   jsonobj.put("type", "TEXT");
                        jsonobj.put("serviceid", "0AwytEuZlWShosO28uNtibIvw9onUxxN8lBLPpLuYIqBD5uu0j7QxZWkrpN0sJxD");
                        jsonobj.put("session", session);
                        StringEntity se = null;
                        try {
                            se = new StringEntity(jsonobj.toString());        //System.out.println("this is th transcript);

                        } catch (UnsupportedEncodingException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        System.out.println("JSOnObject is..." + jsonobj);
                        httpPost.setEntity(se);
                        try {
                            HttpClient sendClient = new DefaultHttpClient();
                            // HttpClient httpClient = HttpClientBuilder.create().build();
                            HttpResponse httpResponse = sendClient.execute(httpPost);
                            java.io.InputStream inputStream = httpResponse.getEntity().getContent();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(
                                    inputStreamReader);
                            StringBuilder stringBuilder = new StringBuilder();
                            String bufferedStrChunk = null;
                            while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                                stringBuilder.append(bufferedStrChunk);
                            }
                            // return stringBuilder.toString();
                            System.out
                                    .println("Reply is"
                                            + stringBuilder.toString());
                            JSONObject jsonObj = new JSONObject(stringBuilder.toString());
                            // System.out.println(jsonString);
                            System.out.println("---------------------------");
                            System.out.println(jsonObj);
                            JSONArray respJsonArray = jsonObj.getJSONArray("response");
                            if((session!=null))
                            {
                               optionArray=jsonObj.getJSONArray("options");

                                mylist = new ArrayList<String>();

                                if (optionArray.equals("[]")) {
                                    System.out.println("json is empty");
                                }
                                else
                                {
                                    int length = optionArray.length();
                                    for (int i=0;i<length;i++){
                                        mylist.add(optionArray.get(i).toString());

                                    }
                                }

                            }

                            session = jsonObj.getString("session");
                            String Temp2=respJsonArray.getString(0);
                              if (Temp2.contains("Automated") && respJsonArray.length() == 1 )
                            {

                                 temp=respJsonArray.getString(0);
                                String[] adresarr=temp.split (" ");

                                for(int j=0;j<adresarr.length;j++)

                                {

                                    if(Isinteger (adresarr[j]))

                                    {
                                        numbercheck=true;

                                        adresarr[j]=adresarr[j].replace (""," ");

                                    }

                                    temprry +=adresarr[j]+" ";



                                }

                                outputText=temprry;
                            }

                               else  if (respJsonArray.length() == 2) {
                                outputText = respJsonArray.getString(0) + respJsonArray.getString(1);
                            } else
                                outputText = respJsonArray.getString(0);
                            flag = true;



                            if (flag) {
                                if (!(outputText.trim().isEmpty())) {

                                    talk.speak(outputText, QUEUE_ADD, null);

                                }

                            }


                        } catch (ClientProtocolException cpe) {
                            System.out
                                    .println("First Exception coz of HttpResponese :"
                                            + cpe);
                            cpe.printStackTrace();
                        } catch (IOException ioe) {
                            System.out
                                    .println("Second Exception coz of HttpResponse :"
                                            + ioe);
                            ioe.printStackTrace();
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // txtString.setText(myResponse);
                            }
                        });


//
//        Assistant assistantservice = new Assistant("2018-02-16");
//        //If you like to use USERNAME AND PASSWORD
//        //Your Username: "apikey", password: "<APIKEY_VALUE>"
//        assistantservice.setUsernameAndPassword("apikey", "<API_KEY_VALUE>");
//
//      //  TODO: Uncomment this line if you want to use API KEY
//        assistantservice.setApiKey("<API_KEY_VALUE>");
//
//      //  Set endpoint which is the URL. Default value: https://gateway.watsonplatform.net/assistant/api
//        assistantservice.setEndPoint("<ASSISTANT_URL>");
//        InputData input = new InputData.Builder(inputmessage).build();
//        //WORKSPACES are now SKILLS
//        MessageOptions options = new MessageOptions.Builder().workspaceId("<WORKSPACE_ID>").input(input).context(context).build();
//        MessageResponse response = assistantservice.message(options).execute();
//                    Log.i(TAG, "run: "+response);

                        //  String outputText = "";

//                    int length=response.getOutput().getText().size();
//                    Log.i(TAG, "run: "+length);
//                    if(length>1) {
//                        for (int i = 0; i < length; i++) {
//                            outputText += '\n' + response.getOutput().getText().get(i).trim();
//                        }
//                    }
//                    else
//                       outputText = response.getOutput().getText().get(0);
//
//                    Log.i(TAG, "run: "+outputText);
//               //Passing Context of last conversation
//                if(response.getContext() !=null)
//                    {
//                        //context.clear();
//                        context = response.getContext();
//
//                    }
                        Message outMessage = new Message();
                        if (outputText != null) {
//              if(response.getOutput()!=null && response.getOutput().containsKey("text"))
//              {
//                  ArrayList responseList = (ArrayList) response.getOutput().get("text");
//                  if(null !=responseList && responseList.size()>0){
//
//                  }
                            if(numbercheck)
                            {

                                outMessage.setMessage(temp);
                                outMessage.setId("2");                           }
                            else {

                                outMessage.setMessage(outputText);
                                outMessage.setId("2");

                            }
                            messageArrayList.add(outMessage);
                            //   }
                            if(mylist.size()>0)
                            {

                                for(int i=0;i<mylist.size();i++) {

                                    Message selectionmessage=new Message();
                                    selectionmessage.setMessage(optionArray.get(i).toString());
                                    selectionmessage.setId("3");
                                    messageArrayList.add(selectionmessage);




                                }


                            }


                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                    if (mAdapter.getItemCount() > 1) {
                                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() );

                                    }

                                }
                            });

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });

        thread.start();

    }


    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Check for network connections
        if (isConnected) {
            return true;
        } else {
            Toast.makeText(getContext(), " No Internet Connection available ", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String numwithoutspace = " ";
                    String removespace = result.get(0);
                    removespace = removespace.replace(" ", "");
//
//                    String regex ="^[0-9]+$";
//                    Pattern p = Pattern.compile(regex);
//                    Matcher m = p.matcher(removespace);
                    if (StringUtils.isNumeric(removespace) ||removespace.contains("/"))
                    {
                        numwithoutspace = removespace.replace(" ", "");
                        inputMessage.setText(numwithoutspace);
                    } else {
                        inputMessage.setText(result.get(0));
                    }


                }
                break;
            }

        }
    }
    public   static Boolean Isinteger(String s )

    {



           if( StringUtils.isNumeric(s))
           {
               return true;
           }

       else {
               return false;
           }



        }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calls_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (talk != null) {
            talk.stop();
            talk.shutdown();

        }
    }
}


