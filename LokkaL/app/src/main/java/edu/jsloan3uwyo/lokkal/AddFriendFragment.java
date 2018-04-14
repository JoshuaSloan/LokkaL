package edu.jsloan3uwyo.lokkal;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFriendFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    Person acc;
    EditText input;
    Button addBtn;

    public AddFriendFragment() {
        // Required empty public constructor
    }

    public static AddFriendFragment newInstance() {
        AddFriendFragment fragment = new AddFriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            acc = (Person) getArguments().getSerializable("Person");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_friend, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        input = (EditText) getView().findViewById(R.id.email_input);
        addBtn = (Button) getView().findViewById(R.id.add_friend_from_input);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(input.getText())) {
                    URI localuri = null;
                    sendToDatabase myData;
                    String fre = input.getText().toString();
                    try {
                        localuri = new URI("http://www.cs.uwyo.edu/~kfenster/insert_friendrequest.php");
                        Log.v("SERVER", "Accessed insert_friendrequest.php");
                        new insertFR().execute(new sendToDatabase(localuri,acc.PersonID, fre));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class sendToDatabase {
        URI uri;
        String data;

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

        //Insert Friend Request
        sendToDatabase(URI myuri, int lpid, String em) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("LeftPersonID", String.valueOf(lpid));
            hmap.put("Email", em);
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    private class insertFR extends AsyncTask<sendToDatabase, String, String> {

        //how to write the parameters via a post method were used from here:
        //http://stackoverflow.com/questions/29536233/deprecated-http-classes-android-lollipop-5-1

        @Override
        protected String doInBackground(sendToDatabase... params) {
            try {
                //setup the url
                URL url = params[0].uri.toURL();
                Log.wtf("network", url.toString());
                //make the connection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //setup as post method and write out the parameters.
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(params[0].data);
                writer.flush();
                writer.close();
                os.close();

                //get the response code (ie success 200 or something else
                int responseCode = con.getResponseCode();
                Log.wtf("Response Code", String.valueOf(responseCode));
                Log.wtf("Message", con.getResponseMessage());
                String response = "";
                //the return is a single number, so simple to read like this:
                //note the while loop should not be necessary, but just in case.
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        Log.wtf("LINE", line);
                        response += line;

                    }
                    if (response == "") {
                        Log.wtf("QUERY", "Line was empty");
                        response = "0";
                    }
                } else
                    response = "0";
                Log.wtf("RESPONSE", response);
                onProgressUpdate(response);
                return response;
            } catch (Exception e) {
                // failure of some kind.  uncomment the stacktrace to see what happened if it is
                // permit error.
                e.printStackTrace();
                return "0";
            }

        }

        protected void onPostExecute(String result) {
            //Calls this at the end of the Async Task
            Toast.makeText(getActivity(), "Friend Request Sent", Toast.LENGTH_SHORT).show();
        }
    }
}
