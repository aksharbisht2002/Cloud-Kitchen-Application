package com.example.major_project.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.major_project.Helper.CartHelper;
import com.example.major_project.Interfaces.UserChoosenFood;
import com.example.major_project.Model.CartModel;
import com.example.major_project.Model.FavouriteModel;
import com.example.major_project.Model.FoodModel;
import com.example.major_project.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerAdapter.cardViewHolder> {
    Context context;
    ArrayList<FoodModel> list;
    ArrayList<String> flist;
    ArrayList<String> olist;
    String CustomerId;
    DatabaseReference likeReference;
    UserChoosenFood userChoosenFood;

    public BestSellerAdapter(Context context, ArrayList<FoodModel> list,ArrayList<String> flist,ArrayList<String> olist,String CustomerId,DatabaseReference likeReference,UserChoosenFood userChoosenFood) {
        this.context = context;
        this.list = list;
        this.CustomerId= CustomerId;
        this.likeReference = likeReference;
        this.flist = flist;
        this.olist= olist;
        this.userChoosenFood = userChoosenFood;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bestseller_card,parent,false);
        cardViewHolder viewHolder = new cardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        final FoodModel model = list.get(position);
                  if (olist.contains(model.getFid())){ holder.outofstock.setVisibility(View.VISIBLE); }
                  else{ holder.outofstock.setVisibility(View.GONE);}
                    holder.addedBtn.setVisibility(View.INVISIBLE);
                    holder.btn.setVisibility(View.VISIBLE);
                    holder.name.setText(model.getName());
                    holder.desc.setText(model.getDesc());
                    holder.price.setText("$ "+model.getPrice());
                    if (model.getType().equals("veg")){
                        holder.type.setImageResource(R.drawable.veg_icon);
                    }
                    if (model.getType().equals("non veg")){
                        holder.type.setImageResource(R.drawable.nonveg_icon);
                    }
                    holder.container.setVisibility(View.INVISIBLE);
                    holder.loader.setVisibility(View.VISIBLE);
                    holder.loader.startShimmer();
                    Picasso.get().load(model.getImgUri()).error(R.drawable.baseline_image_not_supported_24).into(holder.image, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.container.setVisibility(View.VISIBLE);
                            holder.loader.setVisibility(View.INVISIBLE);
                            holder.loader.stopShimmer();
                        }
                        @Override
                        public void onError(Exception e) {}
                    });
                    holder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String fid = model.getFid();
                            String name = model.getName();
                            String price = model.getPrice();
                            String img = model.getImgUri();
                            String type = model.getType();
                            ((UserChoosenFood) CartHelper.getmyContext()).getDataInfo(new CartModel(fid,name,price,img,type));

                            holder.addedBtn.setVisibility(View.VISIBLE);
                            holder.btn.setVisibility(View.GONE);
                        }
                    });

                    if ( flist.contains(model.getFid())){
                        holder.like.setImageResource(R.drawable.fillheart);
                    }else{
                        holder.like.setImageResource(R.drawable.redheart);
                    }

                    holder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FavouriteModel model1 = new FavouriteModel(model.getFid(),model.getName(),model.getPrice(),model.getImgUri(),model.getDesc(),model.getType());
                            likeReference.child(CustomerId).child(model.getFid()).setValue(model1);
                            holder.like.setImageResource(R.drawable.fillheart);
                        }
                    });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class cardViewHolder extends RecyclerView.ViewHolder{
        ImageView type,image,like;
        TextView name,desc,price;
        LinearLayout btn,outofstock,addedBtn;
        CardView container;
        ShimmerFrameLayout loader;
        public cardViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.typelogo);
            image=itemView.findViewById(R.id.foodimg);
            name= itemView.findViewById(R.id.foodname);
            desc = itemView.findViewById(R.id.fooddesc);
            price = itemView.findViewById(R.id.foodprice);
            container = itemView.findViewById(R.id.foodheading);
            loader = itemView.findViewById(R.id.cardloader);
            btn = itemView.findViewById(R.id.addBtn);
            addedBtn = itemView.findViewById(R.id.addedBtn);
            outofstock=itemView.findViewById(R.id.outofstock);
            like = itemView.findViewById(R.id.like);
        }

    }
}
