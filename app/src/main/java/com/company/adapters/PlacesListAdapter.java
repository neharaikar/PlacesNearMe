package com.company.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.company.entities.Geometry;
import com.company.placesnearme.BaseActivity;
import com.company.placesnearme.MainActivity;
import com.company.placesnearme.MapActivity;
import com.company.entities.OpeningHours;
import com.company.entities.Result;
import com.company.placesnearme.R;
import com.company.placesnearme.adapter;

import java.io.InputStream;
import java.util.List;


public class PlacesListAdapter extends ArrayAdapter<Result>  implements View.OnClickListener
{
    private Context context;
    private List<Result> results;



    public PlacesListAdapter(Context context, List<Result> results) {
        super(context, R.layout.place_row_layout, results);
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        try {

            ViewHolder viewHolder;
            if(view == null) {
                viewHolder = new ViewHolder();

                view = LayoutInflater.from(context).inflate(R.layout.place_row_layout,null,true);
                viewHolder.textViewName = view.findViewById(R.id.textViewName);
                viewHolder.textViewAddress = view.findViewById(R.id.textViewAddress);
                viewHolder.textViewRating = view.findViewById(R.id.textViewRating);
                viewHolder.textViewStatus = view.findViewById(R.id.textViewStatus);
                viewHolder.mCardView =view.findViewById(R.id.card_view);

                view.setTag(viewHolder);
            } else  {
                viewHolder = (ViewHolder) view.getTag();
            }
            Result result = results.get(position);
            viewHolder.textViewName.setText(result.getName());
            viewHolder.textViewAddress.setText("Address: "+result.getVicinity());
            viewHolder.textViewRating.setText("Ratings: "+result.getRating()+"("+result.getUser_rating()+")");
            if(result.getRating()!=null) {
                if (result.getRating() > 0.0 && result.getRating() < 2.0) {
                    viewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#F1948A"));
                } else if (result.getRating() >=2.0 && result.getRating() < 4.0) {
                    viewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#F7DC6F"));

                } else if (result.getRating() >=4.0) {
                    viewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#7DCEA0"));

                }
            }
            OpeningHours op=result.getOpeningHours();
            if(op!=null && op.getOpenNow()!=null){

                if(op.getOpenNow())
                {
                    viewHolder.textViewStatus.setText("Status: OPEN");
                }
                else
                {
                    viewHolder.textViewStatus.setText("Status: Close");
                }
            }
            else
            viewHolder.textViewStatus.setText("");
            viewHolder.mCardView.setTag(position);
          //  viewHolder.mCardView.setTag(MapActivity.class);
            viewHolder.mCardView.setOnClickListener(this);
            //viewHolder.mapbutton.setOnClickListener(this);
            return view;
        } catch (Exception e) {  
            System.out.println(e);
            return null;
        }

    }



    public static class ViewHolder {
        public TextView textViewName;
        public TextView textViewAddress;
        public TextView textViewRating;
        public TextView textViewStatus;

        public CardView mCardView;

    }
    @Nullable
    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();

        Intent myIntent = new Intent(context, MapActivity.class);
        myIntent.putExtra("result", results.get(position));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }


}
