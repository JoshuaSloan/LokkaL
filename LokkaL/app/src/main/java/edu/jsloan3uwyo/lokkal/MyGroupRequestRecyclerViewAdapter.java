package edu.jsloan3uwyo.lokkal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.jsloan3uwyo.lokkal.GroupRequestFragment.OnListFragmentInteractionListener;

import java.util.List;


public class MyGroupRequestRecyclerViewAdapter extends RecyclerView.Adapter<MyGroupRequestRecyclerViewAdapter.ViewHolder> {

    private final List<GroupRequest> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyGroupRequestRecyclerViewAdapter(List<GroupRequest> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_grouprequest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).GroupName);
        holder.mFromView.setText("From:");
        holder.mContentView.setText(mValues.get(position).GroupCreatorName);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mFromView;
        public GroupRequest mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFromView = (TextView) view.findViewById(R.id.gr_from);
            mIdView = (TextView) view.findViewById(R.id.gr_group_name);
            mContentView = (TextView) view.findViewById(R.id.gr_from_pn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
