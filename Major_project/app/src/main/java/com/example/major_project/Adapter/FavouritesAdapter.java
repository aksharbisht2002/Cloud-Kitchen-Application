package com.example.major_project.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.major_project.Helper.CartHelper;
import com.example.major_project.Interfaces.UserChoosenFood;
import com.example.major_project.Model.CartModel;
import com.example.major_project.Model.FavouriteModel;
import com.example.major_project.Model.FoodModel;
import com.example.major_project.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.CardViewHolder> {
    Context context;
    ArrayList<FavouriteModel> list;
    ArrayList<String> olist;
    DatabaseReference reference;
    UserChoosenFood userChoosenFood;
    String CustomerId;

    public FavouritesAdapter(Context context,String CustomerId,ArrayList<FavouriteModel> list,ArrayList<String> olist, DatabaseReference reference, UserChoosenFood userChoosenFood) {
        this.context = context;
        this.list = list;
        this.reference = reference;
        this.userChoosenFood = userChoosenFood;
        this.olist = olist;
        this.CustomerId = CustomerId;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favouritefood_card,parent,false);
      CardViewHolder cardViewHolder = new CardViewHolder(view);
      return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FavouriteModel model = list.get(position);
            if (olist.contains(model.getFid())){
                holder.outofstock.setVisibility(View.VISIBLE);
            } else{
                holder.outofstock.setVisibility(View.GONE);
            }

        holder.addedBtn.setVisibility(View.INVISIBLE);
        holder.addBtn.setVisibility(View.VISIBLE);

        holder.name.setText(model.getFname());
        holder.price.setText("$ "+model.getFprice());
        if (model.getFtype().equals("veg")){
            holder.type.setImageResource(R.drawable.veg_icon);
        }
        if (model.getFtype().equals("non veg")){
            holder.type.setImageResource(R.drawable.nonveg_icon);
        }

        holder.image.setVisibility(View.GONE);
        holder.loader.setVisibility(View.VISIBLE);
        holder.loader.startShimmer();
        Picasso.get().load(model.getFpic()).error(R.drawable.baseline_image_not_supported_24).into(holder.image, new Callback() {
            @Override
            public void onSuccess() {
                holder.image.setVisibility(View.VISIBLE);
                holder.loader.setVisibility(View.GONE);
                holder.loader.stopShimmer();
            }
            @Override
            public void onError(Exception e) {}
        });

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fid = model.getFid();
                String name = model.getFname();
                String price = model.getFprice();
                String img = model.getFpic();
                String type = model.getFtype();
                ((UserChoosenFood) CartHelper.getmyContext()).getDataInfo(new CartModel(fid,name,price,img,type));
                holder.addedBtn.setVisibility(View.VISIBLE);
                holder.addBtn.setVisibility(View.GONE);
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.layout.animate().translationX(-1000).setStartDelay(100).setDuration(700);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reference.child(CustomerId).child(model.getFid()).removeValue();
                        list.clear();
                        notifyDataSetChanged();
                    }
                },700);

            }
        });

    }

    @Override
    public int getItemCount() {
       return list.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        ShimmerFrameLayout loader;
        ImageView image,type;
        TextView name,price;
        LinearLayout addBtn,addedBtn,removeBtn,outofstock;
        CardView layout;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            loader = itemView.findViewById(R.id.foodimg_loader);
            image = itemView.findViewById(R.id.foodimg);
            type = itemView.findViewById(R.id.typelogo);
            name = itemView.findViewById(R.id.foodname);
            price = itemView.findViewById(R.id.foodprice);
            addBtn = itemView.findViewById(R.id.addBtn);
            addedBtn = itemView.findViewById(R.id.addedBtn);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            outofstock = itemView.findViewById(R.id.outofstock);
            layout = itemView.findViewById(R.id.favouriteLayout);
        }
    }


}
