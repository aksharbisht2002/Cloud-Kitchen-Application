package com.example.major_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.major_project.Helper.CartHelper;
import com.example.major_project.Interfaces.UserChoosenFood;
import com.example.major_project.Model.CartModel;
import com.example.major_project.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Stack;

public class CartExtraAdapter extends  RecyclerView.Adapter<CartExtraAdapter.CardViewHolder> {
    Context context;
    ArrayList<CartModel> list;
    UserChoosenFood userChoosenFood;
    CartAdapter adapter;
  LinearLayout loader;
    public CartExtraAdapter(Context context, ArrayList<CartModel> list, UserChoosenFood userChoosenFood,CartAdapter adapter,LinearLayout loader) {
        this.context = context;
        this.list = list;
        this.userChoosenFood = userChoosenFood;
        this.adapter = adapter;
        this.loader = loader;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_extralayout,parent,false);
        CardViewHolder view = new CardViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
            CartModel model = list.get(position);
        holder.addedBtn.setVisibility(View.INVISIBLE);
        holder.addBtn.setVisibility(View.VISIBLE);

        holder.name.setText(model.getName());
        holder.price.setText("$ "+model.getPrice());
        holder.image.setVisibility(View.GONE);
        holder.loader.setVisibility(View.VISIBLE);
        holder.loader.startShimmer();


        if (model.getType().equals("veg")){
            holder.type.setImageResource(R.drawable.veg_icon);
        }
        if (model.getType().equals("non veg")){
            holder.type.setImageResource(R.drawable.nonveg_icon);
        }

        Picasso.get().load(model.getImg()).error(R.drawable.baseline_image_not_supported_24).into(holder.image, new Callback() {
            @Override
            public void onSuccess() {
                loader.setVisibility(View.GONE);
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
                String name = model.getName();
                String price = model.getPrice();
                String img = model.getImg();
                String type = model.getType();
                ((UserChoosenFood) CartHelper.getmyContext()).getDataInfo(model);
                adapter.notifyDataSetChanged();
                holder.addedBtn.setVisibility(View.VISIBLE);
                holder.addBtn.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        ImageView image,type;
        TextView name,price;
        LinearLayout addBtn,addedBtn;
        ShimmerFrameLayout loader;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foodname);
            price = itemView.findViewById(R.id.foodprice);
            image = itemView.findViewById(R.id.foodimg);
            type = itemView.findViewById(R.id.typelogo);
            loader = itemView.findViewById(R.id.foodimg_loader);
            addBtn = itemView.findViewById(R.id.addBtn);
            addedBtn = itemView.findViewById(R.id.addedBtn);
        }
    }
}
