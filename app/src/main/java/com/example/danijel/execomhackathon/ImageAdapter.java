package com.example.danijel.execomhackathon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.danijel.execomhackathon.db.Monuments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Danijel on 5/23/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Monuments> monuments;

    public ImageAdapter(Context c,List<Monuments> monuments) {
        mContext = c;
        this.monuments = monuments;
        Log.d("from adapter",monuments+"");
    }

    public int getCount() {
      // return mThumbIds.length;
         return monuments.size();
    }

    public Object getItem(int position) {
        return monuments.get(position).getImage();
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.grid, null);
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView.findViewById(R.id.gridImg);
        }

        try {
            String path = monuments.get(position).getImage();
            Bitmap original = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(original);

        }catch (Exception e){

        }

       //imageView.setImageResource(mThumbIds[position]);
        return convertView;
    }

}