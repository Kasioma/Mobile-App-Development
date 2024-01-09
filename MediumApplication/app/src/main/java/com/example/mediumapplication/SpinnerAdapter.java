package com.example.mediumapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    List<String> images;
    public SpinnerAdapter(Context context, List<String> images){
        super(context, R.layout.item, images);
        this.context = context;
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.dropdown, parent, false);

        ImageView gun = row.findViewById(R.id.img);

        Resources res = context.getResources();
        String drawableName = images.get(position).toLowerCase();

        int resId = res.getIdentifier(drawableName, "drawable", context.getPackageName());
        Drawable drawable = res.getDrawable(resId);

        gun.setImageDrawable(drawable);

        return row;
    }
}
