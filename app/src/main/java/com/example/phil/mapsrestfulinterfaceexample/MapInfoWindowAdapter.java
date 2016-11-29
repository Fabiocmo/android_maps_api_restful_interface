package com.example.phil.mapsrestfulinterfaceexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Phil on 28-Nov-16.
 */

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;

    MapInfoWindowAdapter(Context context){
        LayoutInflater mInflater;
        mInflater = LayoutInflater.from(context);
        myContentsView = mInflater.inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
        tvTitle.setText(marker.getTitle());
        TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
        tvSnippet.setText(marker.getSnippet());


        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }

}