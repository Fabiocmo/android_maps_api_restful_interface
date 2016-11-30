package com.example.phil.mapsrestfulinterfaceexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.phil.mapsrestfulinterfaceexample.POJO.Review;

import java.util.ArrayList;

/**
 * Created by Phil on 29-Nov-16.
 */
public class ReviewsAdapter extends ArrayAdapter<Review> {
    public ReviewsAdapter(Context context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Review review = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }
        // Lookup view for data population
        TextView titleText = (TextView) convertView.findViewById(R.id.review_item_title);
        TextView bodyText = (TextView) convertView.findViewById(R.id.review_item_text);
        RatingBar reviewRating = (RatingBar) convertView.findViewById(R.id.ratingBarReviewDetails);
        // Populate the data into the template view using the data object
        titleText.setText(review.getTitle());
        bodyText.setText(review.getText_body());
        reviewRating.setRating((float)review.getStar_rating());
        // Return the completed view to render on screen
        return convertView;
    }
}
