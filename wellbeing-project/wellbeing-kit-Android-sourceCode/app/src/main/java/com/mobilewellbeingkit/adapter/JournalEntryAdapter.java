package com.mobilewellbeingkit.adapter;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mobilewellbeingkit.model.JournalEntry;
import com.mobilewellbeingkit.R;

import java.util.List;

public class JournalEntryAdapter extends ArrayAdapter<JournalEntry>{

    private int LayoutResourceID;

    public JournalEntryAdapter(Context context, int resource, List<JournalEntry> objects)
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
        TextView lblDate = row.findViewById(R.id.lblDate);
        TextView lblCategory = row.findViewById(R.id.lblCatagory);
        TextView lblEntry = row.findViewById(R.id.lblEntry);

        JournalEntry j = this.getItem(position);
        String shortDate = j.getEntryDate().toString();
        Log.d("entry date", j.getEntryDate());

        lblDate.setText(shortDate.substring(0, Math.min(shortDate.length(), 10)));

        lblCategory.setText(j.getEntryCategory());
        lblEntry.setText(j.getEntryNote());

        return row;
    }
}
