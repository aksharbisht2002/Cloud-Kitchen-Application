package com.example.major_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.major_project.Menu;
import com.example.major_project.Model.CravingLayoutModel;
import com.example.major_project.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CravingLayoutAdapter extends ArrayAdapter<CravingLayoutModel> {

    public CravingLayoutAdapter(@NonNull Context context, ArrayList<CravingLayoutModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.craving_card, parent, false);
        }

        CravingLayoutModel model = getItem(position);
        TextView text = listitemView.findViewById(R.id.foodname);
        ImageView image = listitemView.findViewById(R.id.foodimg);
        CardView card = listitemView.findViewById(R.id.cravingcard_layout);
        ShimmerFrameLayout loader = listitemView.findViewById(R.id.foodimg_loader);
        loader.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        loader.startShimmer();
        text.setText(model.getText());
        Picasso.get().load(model.getImage()).into(image, new Callback() {
            @Override
            public void onSuccess() {
                loader.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                loader.stopShimmer();
            }
            @Override
            public void onError(Exception e) {}
        });
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Menu.class);
                i.putExtra("pos",String.valueOf(position));
                i.putExtra("From","Craving");
                getContext().startActivity(i);
            }
        });
        return listitemView;
    }
}
