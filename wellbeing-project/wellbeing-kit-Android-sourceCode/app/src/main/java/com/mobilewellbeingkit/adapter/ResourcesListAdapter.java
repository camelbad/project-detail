package com.mobilewellbeingkit.adapter;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobilewellbeingkit.R;
import com.mobilewellbeingkit.model.UserResource;

import java.util.List;

public class ResourcesListAdapter extends ArrayAdapter<UserResource> {

    private int LayoutResourceID;

    public ResourcesListAdapter(Context context, int resource, List<UserResource> objects)
    {
        super(context, resource, objects);
        this.LayoutResourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(LayoutResourceID, parent, false);
        TextView lblResource = row.findViewById(R.id.lblResource);
        UserResource r = this.getItem(position);

        lblResource.setText(r.getResourceText());

        return row;
    }
}
