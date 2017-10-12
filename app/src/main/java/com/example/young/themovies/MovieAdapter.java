package com.example.young.themovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Young on 29/11/2016.
 */

public class MovieAdapter extends BaseAdapter {
    ArrayList<MovieModel> items;
    Activity activity;

    public MovieAdapter(Activity activity, ArrayList<MovieModel> items){
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        if (view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_grid, null);
            holder.imgItem = (ImageView)view.findViewById(R.id.item_img_grid);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        MovieModel movie = (MovieModel)getItem(position);
        Picasso.with(activity).load(movie.getImage()).into(holder.imgItem);
        return view;
    }
    static class ViewHolder{
        ImageView imgItem;
    }
}
