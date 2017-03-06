package com.beardev.findrestaurant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jean on 03/11/2016.
 */

public class ReviewViewAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public ReviewViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) row.findViewById(R.id.txtName);
            holder.text = (TextView) row.findViewById(R.id.txtReview);
            holder.review = (RatingBar) row.findViewById(R.id.rbReview);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Review item = (Review) data.get(position);
        holder.name.setText(item.getName());
        holder.text.setText(item.getDescriprion());

        Integer rating = item.getAttendance() + item.getFood() + item.getPlace() + item.getPrice();

        holder.review.setRating((float) (rating / 4.0));

        return row;
    }

    static class ViewHolder {
        TextView name;
        TextView text;
        RatingBar review;
    }

}
