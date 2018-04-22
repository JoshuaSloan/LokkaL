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
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGroupFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    Person acc;
    EditText input;
    Button createBtn;

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateGroupFragment newInstance(String param1, String param2) {
        CreateGroupFragment fragment = new CreateGroupFragment();
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
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        input = (EditText) getView().findViewById(R.id.group_name_input);
        createBtn = (Button) getView().findViewById(R.id.create_group_from_input);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(input.getText())) {
                    URI localuri = null;
                    CreateGroupFragment.sendToDatabase myData;
                    String gn = input.getText().toString();
                    try {
                        localuri = new URI("http://www.cs.uwyo.edu/~kfenster/insert_groupsession.php");
                        new insertG().execute(new sendToDatabase(localuri,gn, acc.PersonID));
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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

        //Insert Group
        sendToDatabase(URI myuri, String gn, int pid) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("GroupName", gn);
            hmap.put("GroupCreatorID", String.valueOf(pid));
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        //Query Group Creator
        sendToDatabase(URI myuri, int pid) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("PersonID", String.valueOf(pid));
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        //Insert Group Creator
        sendToDatabase(URI myuri,int gid, int pid, int rid) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("GroupID", String.valueOf(gid));
            hmap.put("PersonID", String.valueOf(pid));
            hmap.put("ResponseTypeID", String.valueOf(rid));
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }
    //Inserts the Group Creator Into GroupModule_GroupSession
    private class insertG extends AsyncTask<sendToDatabase, String, String> {

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
            //Now we need to find the Group ID of the Group we just created
            findGroupID();
        }
    }
    //Once we have insert
    private class queryG extends AsyncTask<sendToDatabase, String, String> {

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
        protected void onProgressUpdate(String... progress) {
            //build the data structure as we go.
            try
            {
                String parts[] = progress[0].split(",");
                acc.setGroup(Integer.valueOf(parts[0]), parts[1]);
            }
            catch(Exception e) {
                Log.v("donetwork", "Error line: onProgressUpdate");
                Log.v("Error", e.getMessage());
            }
        }
        protected void onPostExecute(String result) {
            //Calls this at the end of the Async Task
           insertGroupCreator();
        }
    }
    //Inserts the Group Creator into GroupModule_GroupMember;
    private class insertGC extends AsyncTask<sendToDatabase, String, String> {

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
            Toast.makeText(getActivity(), "Group Created!", Toast.LENGTH_SHORT).show();
        }
    }
    //Finds the GroupID for the Group Creator
    public void findGroupID()
    {
        URI localuri = null;
        CreateGroupFragment.sendToDatabase myData;
        try {
            localuri = new URI("http://www.cs.uwyo.edu/~kfenster/query_groupcreator.php");
            new queryG().execute(new sendToDatabase(localuri,acc.PersonID));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void insertGroupCreator()
    {
        URI localuri = null;
        CreateGroupFragment.sendToDatabase myData;
        try {
            localuri = new URI("http://www.cs.uwyo.edu/~kfenster/insert_groupmember.php");
            Log.v("GroupID", String.valueOf(acc.myGroup.GroupID));
            Log.v("GroupName", acc.myGroup.GroupName);
            new insertGC().execute(new sendToDatabase(localuri,acc.myGroup.GroupID,acc.PersonID,2));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
