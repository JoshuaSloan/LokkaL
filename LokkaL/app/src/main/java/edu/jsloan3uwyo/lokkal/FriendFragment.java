package edu.jsloan3uwyo.lokkal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class FriendFragment extends Fragment {

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
    public FriendFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FriendFragment newInstance(int columnCount) {
        FriendFragment fragment = new FriendFragment();
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
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
             recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyFriendRecyclerViewAdapter(acc.lof, mListener));
        }
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        //Empty the list because we requery it.
        acc.lof.clear();
        URI localuri = null;
        FriendFragment myData;
        //Return Friends
        try {
            localuri = new URI("http://www.cs.uwyo.edu/~kfenster/query_friends.php");
            new FriendFragment.pullF().execute(new FriendFragment.sendToDatabase(localuri,acc.PersonID));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //Helper for handling the swipe right vs. swipe left
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Gets position of the item on the list.
                final int position = viewHolder.getAdapterPosition();
                FriendRequestFragment myData;
                //Declining Friend Request
                if(direction == ItemTouchHelper.LEFT) {
                    Log.v("SWIPE:", "Swiping Left");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Delete Friend: " + acc.lof.get(position).PersonName + "?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Remove Item From Recycler View
                            URI localuri = null;
                            try {
                                localuri = new URI("http://www.cs.uwyo.edu/~kfenster/update_friend.php");
                                new FriendFragment.deleteF().execute(new FriendFragment.sendToDatabase(localuri, acc.lof.get(position).FriendshipID, 3));
                                //Removing friend from list
                                //The Async Thread notifies the adapater the dataset has been changed once done,
                                //So we don't need to notify the adapter here.
                                acc.lof.remove(position);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doDataUpdate();
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                if(direction == ItemTouchHelper.RIGHT)
                {
                    //Sends Group Invite, if you are in a group
                    if(acc.myGroup != null)
                    {
                        URI localuri = null;
                        try {
                            localuri = new URI("http://www.cs.uwyo.edu/~kfenster/insert_groupmember.php");
                            new FriendFragment.insertGR().execute(new FriendFragment.sendToDatabase(localuri,acc.myGroup.GroupID ,acc.lof.get(position).PersonID, 1));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        //Sets swipe functionality on the recycler view
        itemTouchHelper.attachToRecyclerView(recyclerView);

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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Friend f);
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

        //Gets Friend List
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
        //Updates Friends List
        sendToDatabase (URI myuri, int fid, int rtid) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("FriendshipID", String.valueOf(fid));
            hmap.put("ResponseTypeID", String.valueOf(rtid));
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        //Sends Group Invite
        sendToDatabase (URI myuri,int gid, int pid, int rtid) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("GroupID", String.valueOf(gid));
            hmap.put("PersonID", String.valueOf(pid));
            hmap.put("ResponseTypeID", String.valueOf(rtid));
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    private class pullF extends AsyncTask<FriendFragment.sendToDatabase, String, List<String>> {
        @Override
        protected List<String> doInBackground(FriendFragment.sendToDatabase... params) {
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
                        if(line.compareTo("") != 0)
                        {
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
                for(int i =0; i < progress.size(); i++)
                {
                    String parts[] = progress.get(i).split(",");
                    acc.lof.add(new Friend(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), parts[2]));
                    Log.v("OPU", parts[0] + parts[1] + parts[2]);
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

    private class deleteF extends AsyncTask<FriendFragment.sendToDatabase, String, String> {
        @Override
        protected String doInBackground(FriendFragment.sendToDatabase... params) {
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
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        //This should be the number of rows affected. Should always be one.
                        Log.wtf("LINE", line);
                        response += line;
                    }
                }
                Log.v("Response", response);
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
            doDataUpdate();  //data has been added/removed, update the recyclerview.
        }
    }

    private class insertGR extends AsyncTask<FriendFragment.sendToDatabase, String, String> {
        @Override
        protected String doInBackground(FriendFragment.sendToDatabase... params) {
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
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        //This should be the number of rows affected. Should always be one.
                        Log.wtf("LINE", line);
                        response += line;
                    }
                }
                Log.v("Response", response);
                return response;
            } catch (Exception e) {
                // failure of some kind.  uncomment the stacktrace to see what happened if it is
                // permit error.
                e.printStackTrace();
                return "0";
            }
        }
    }
    public void doDataUpdate() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
