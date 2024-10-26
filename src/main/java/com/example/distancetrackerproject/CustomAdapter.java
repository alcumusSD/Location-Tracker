package com.example.distancetrackerproject;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.distancetrackerproject.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Track> {

    Context context;
    int xmlResource;
    ArrayList<Track> list;

    public CustomAdapter(MainActivity context, int resource, ArrayList<Track> objects) {
        super(context, resource, objects);
        this.xmlResource = resource;
        this.list = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterLayout = layoutInflater.inflate(xmlResource, null);

        TextView addressTextView = adapterLayout.findViewById(R.id.address);
        TextView timeElapsedTextView = adapterLayout.findViewById(R.id.time);

        addressTextView.setText(list.get(position).getAddress());
        timeElapsedTextView.setText(String.valueOf(getItem(position).getTimeSpent()) + " seconds" + ""); // Convert int to String

        return adapterLayout;
    }
}
