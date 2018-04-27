package edu.jsloan3uwyo.lokkal;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.jsloan3uwyo.lokkal.dummy.DummyContent;
import edu.jsloan3uwyo.lokkal.dummy.DummyContent.DummyItem;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GroupFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    Person acc;
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GroupFragment newInstance(int columnCount) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            acc = (Person) getArguments().getSerializable("Person");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);

        FloatingActionButton leaveGroup = (FloatingActionButton) getView().findViewById(R.id.btnLeave);
        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Test Leave",Toast.LENGTH_LONG);
                Log.d("test","Leave Group");
            }
        });
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyGroupRecyclerViewAdapter(acc.myGroup.logm, mListener));
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Empty the list because we requery it.
        acc.myGroup.logm.clear();
        URI localuri = null;
        FriendFragment myData;
        //Return Friends
        try {
            localuri = new URI("http://www.cs.uwyo.edu/~kfenster/query_groupmembers.php");
            new queryGM().execute(new sendToDatabase(localuri, acc.myGroup.GroupID));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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

        //Gets Group Members
        sendToDatabase(URI myuri, int gid) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("GroupID", String.valueOf(gid));
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

        private class queryGM extends AsyncTask<sendToDatabase, String, List<String>> {

            @Override
            protected List<String> doInBackground(sendToDatabase... params) {
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
                    List<String> los = new ArrayList<String>();
                    //the return is a single number, so simple to read like this:
                    //note the while loop should not be necessary, but just in case.
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            Log.wtf("LINE", line);
                            if (line.compareTo("") != 0) {
                                los.add(line);
                                Log.v("LOS", String.valueOf(los.size()));
                            }

                        }

                    }
                    Log.v("LOS - After loop", String.valueOf(los.size()));
                    onProgressUpdate(los);
                    return los;
                } catch (Exception e) {
                    // failure of some kind.  uncomment the stacktrace to see what happened if it is
                    // permit error.
                    e.printStackTrace();
                    return new ArrayList<String>();
                }

            }

            protected void onProgressUpdate(List<String> progress) {
                //build the data structure as we go.
                try {
                    Log.v("Progress", String.valueOf(progress.size()));

                    //Splits results by CSV values
                    for (int i = 0; i < progress.size(); i++) {
                        String parts[] = progress.get(i).split(",");
                        acc.myGroup.logm.add(new GroupMember(Integer.valueOf(parts[0]), parts[1], Double.valueOf(parts[2]), Double.valueOf(parts[3]), Double.valueOf(parts[4])));
                        //Log.v("OPU", parts[0] + parts[1] + parts[2]);
                        Log.v("Output:", parts[0] + parts[1] + parts[2] + parts[3]);
                    }
                } catch (Exception e) {
                    Log.v("donetwork", "Error line: onProgressUpdate");
                    Log.v("Error", e.getMessage());
                }
            }

            protected void onPostExecute(List<String> result) {
                //Calls this at the end of the Async Task
                doDataUpdate();  //data has been added/removed, update the recyclerview.
            }
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof OnListFragmentInteractionListener) {
                mListener = (OnListFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnListFragmentInteractionListener");
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
         * <p/>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
        public interface OnListFragmentInteractionListener {
            // TODO: Update argument type and name
            void onListFragmentInteraction(GroupMember item);
        }

    public void doDataUpdate() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
