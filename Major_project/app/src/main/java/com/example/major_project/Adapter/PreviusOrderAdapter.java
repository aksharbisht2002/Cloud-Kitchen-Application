package com.example.major_project.Adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.major_project.Helper.CartHelper;
import com.example.major_project.Interfaces.UserChoosenFood;
import com.example.major_project.Model.CartModel;
import com.example.major_project.Model.FoodModel;
import com.example.major_project.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PreviusOrderAdapter extends RecyclerView.Adapter<PreviusOrderAdapter.cardViewHolder>{

    Context context;
    ArrayList<FoodModel> list ;
    ArrayList<String> olist;
    UserChoosenFood userChoosenFood;

    public PreviusOrderAdapter(Context context , ArrayList<FoodModel> list,ArrayList<String> olist,UserChoosenFood userChoosenFood) {
        this.context = context;
        this.list = list;
        this.olist =olist;
        this.userChoosenFood = userChoosenFood;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.previousorder_card,parent,false);
        cardViewHolder viewHolder = new cardViewHolder(view);
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        FoodModel model = list.get(position);
        if (olist.contains(model.getFid())){
            holder.outofstock.setVisibility(View.VISIBLE);
        }
        else {
            holder.outofstock.setVisibility(View.GONE);
        }
            holder.addedBtn.setVisibility(View.INVISIBLE);
            holder.addBtn.setVisibility(View.VISIBLE);
            holder.name.setText(model.getName());
            holder.desc.setText(model.getDesc());
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

            Picasso.get().load(model.getImgUri()).error(R.drawable.baseline_image_not_supported_24).into(holder.image, new Callback() {
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
                    String name = model.getName();
                    String price = model.getPrice();
                    String img = model.getImgUri();
                    String type = model.getType();
                     ((UserChoosenFood) CartHelper.getmyContext()).getDataInfo(new CartModel(fid,name,price,img,type));
                    holder.addedBtn.setVisibility(View.VISIBLE);
                    holder.addBtn.setVisibility(View.GONE);
                }
            });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class cardViewHolder extends RecyclerView.ViewHolder{
     ImageView type,image;
     ShimmerFrameLayout loader;
     TextView name,desc,price;
    LinearLayout addBtn,outofstock,addedBtn;
        public cardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foodname);
            desc = itemView.findViewById(R.id.fooddesc);
            price = itemView.findViewById(R.id.foodprice);
            type = itemView.findViewById(R.id.typelogo);
            image = itemView.findViewById(R.id.foodimg);
            loader = itemView.findViewById(R.id.foodimg_loader);
            addBtn = itemView.findViewById(R.id.addBtn);
            addedBtn = itemView.findViewById(R.id.addedBtn);
            outofstock=itemView.findViewById(R.id.outofstock);

        }
    }
}
