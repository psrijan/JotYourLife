package com.mycompany.jotyourlife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MySimpleArrayAdapter(Context context, String[] values) {
        super(context, R.layout.row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);
        // Change the icon for Windows and iPhone
        String s = values[position];

        if (s.startsWith("Jou")) {
            imageView.setImageResource(R.drawable.img_journey);
        } else if (s.startsWith("Cal")) {
            imageView.setImageResource(R.drawable.img_calendar);
        } else if (s.startsWith("Photos")) {
            imageView.setImageResource(R.drawable.img_camera);
        } else if (s.startsWith("Atl")) {
            imageView.setImageResource(R.drawable.img_atlas);
        } else if (s.startsWith("Ins")) {
            imageView.setImageResource(R.drawable.img_inspiration);
        } else {
            imageView.setImageResource(R.drawable.img_search);

        }
        return rowView;
    }
} 