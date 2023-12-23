package com.example.major_project.Adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewTreeViewModelKt;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CardViewHolder> {
    Activity activity;
    Stack<CartModel> list;
    UserChoosenFood userChoosenFood;
    TextView subprice,grandprice,counter,total;
    ConstraintLayout dataLayout,nodataLayout;
    LinearLayout loader;
    boolean isvisible=false;

    public CartAdapter(Activity activity, TextView subprice, TextView grandprice,TextView counter,TextView total,ConstraintLayout dataLayout,ConstraintLayout nodataLayout,LinearLayout loader,UserChoosenFood userChoosenFood) {
        this.activity = activity;
        this.userChoosenFood = userChoosenFood;
        list = ((UserChoosenFood) CartHelper.getmyContext()).getItem();
        this.subprice = subprice;
        this.grandprice = grandprice;
        this.counter =counter;
        this.total = total;
        this.dataLayout = dataLayout;
        this.nodataLayout = nodataLayout;
        this.loader = loader;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_cardlayout,parent,false);
        CardViewHolder viewHolder = new CardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CartModel model = list.get(position);
        if (!isvisible){
            loader.setVisibility(View.VISIBLE);
             isvisible=true;
        }
        int number = Integer.parseInt(holder.itemcount.getText().toString());
        if (list.size()==0){
            nodataLayout.setVisibility(View.VISIBLE);
            dataLayout.setVisibility(View.GONE);
            subprice.setText("$ 0.00");

        }else {
            nodataLayout.setVisibility(View.GONE);
            dataLayout.setVisibility(View.VISIBLE);

           // holder.layout.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.cart_card_popin));
            model.setQnt(number);
            counter.setText(list.size()+"  items in the cart");
            float price = ((UserChoosenFood) CartHelper.getmyContext()).getTotalPrice();
            float  finalprice = (float) ((Float) price + (price * 0.035));

            subprice.setText("$ "+String.format("%.2f",price));
            grandprice.setText("$ "+String.format("%.2f",finalprice));
            total.setText("$ "+String.format("%.2f",finalprice));

             if ((total.getText().toString()).equals(null)){
                 total.setText("$ "+String.format("%.2f",finalprice));
                 notifyDataSetChanged();
             }


            holder.image.setVisibility(View.GONE);
            holder.loader.setVisibility(View.VISIBLE);
            holder.loader.startShimmer();

            holder.name.setText(model.getName());
            holder.price.setText("$ "+model.getPrice());
            if (model.getType().equals("veg")){
                holder.type.setImageResource(R.drawable.veg_icon);
            }
            if (model.getType().equals("non veg")){
                holder.type.setImageResource(R.drawable.nonveg_icon);
            }
            Picasso.get().load(model.getImg()).error(R.drawable.baseline_image_not_supported_24).into(holder.image, new Callback() {
                @Override
                public void onSuccess() {
                    holder.image.setVisibility(View.VISIBLE);
                    holder.loader.setVisibility(View.GONE);
                    holder.loader.stopShimmer();
                }
                @Override
                public void onError(Exception e) {}
            });

            holder.itemadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.itemcount.setText(String.valueOf(number+1));
                    model.setQnt(number+1);
                    notifyDataSetChanged();
                }
            });
            holder.itemsub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (number==1){
                        holder.layout.animate().translationX(-1000).setStartDelay(100).setDuration(700);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(model);
                                if (list.size()==0){
                                    nodataLayout.setVisibility(View.VISIBLE);
                                    dataLayout.setVisibility(View.GONE);
                                }else {
                                    nodataLayout.setVisibility(View.GONE);
                                    dataLayout.setVisibility(View.VISIBLE);
                                }
                                notifyDataSetChanged();
                            }
                        },700);
                    }else {
                        holder.itemcount.setText(String.valueOf(number-1));
                        model.setQnt(number-1);
                        notifyDataSetChanged();

                    }
                }
            });
            holder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.layout.animate().translationX(-1000).setStartDelay(100).setDuration(700);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            list.remove(model);
                            if (list.size()==0){
                                nodataLayout.setVisibility(View.VISIBLE);
                                dataLayout.setVisibility(View.GONE);
                            }else {
                                nodataLayout.setVisibility(View.GONE);
                                dataLayout.setVisibility(View.VISIBLE);
                            }
                            notifyDataSetChanged();
                        }
                    },700);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        ImageView image,removeBtn,itemadd,type;
        TextView name,price,itemcount;
        LinearLayout itemsub;
        ShimmerFrameLayout loader;
        CardView layout;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foodname);
            price = itemView.findViewById(R.id.foodprice);
            itemcount =itemView.findViewById(R.id.ItemNumner);
            image = itemView.findViewById(R.id.foodimg);
            type = itemView.findViewById(R.id.typelogo);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            itemadd = itemView.findViewById(R.id.ItemAdd);
            itemsub = itemView.findViewById(R.id.ItemSub);
            loader = itemView.findViewById(R.id.foodimg_loader);
            layout = itemView.findViewById(R.id.cartCard);
        }
    }
}
