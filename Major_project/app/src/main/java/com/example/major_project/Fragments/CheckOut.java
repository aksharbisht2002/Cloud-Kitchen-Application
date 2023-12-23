package com.example.major_project.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.major_project.Helper.CartHelper;
import com.example.major_project.Helper.OrderHelper;
import com.example.major_project.Helper.OrderItemsHelper;
import com.example.major_project.Helper.SetAddressHelper;
import com.example.major_project.Home;
import com.example.major_project.Interfaces.UserChoosenFood;
import com.example.major_project.Model.CartModel;
import com.example.major_project.R;
import com.example.major_project.TrackOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Random;
import java.util.Stack;

public class CheckOut extends Fragment {
    ImageView backBtn;
    Button payBtn;
    LinearLayout cancelBtn,checkoutLoader,checkoutData;
    TextView orderno,itemcount,time,price,name,address,phone,type;
    RadioGroup deliveryGroup,takeawayGroup;
    RadioButton radioButton;
    CardView orderBtn,userBtn;
    SharedPreferences preferences;
    String character = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    String ClientId="AfJ4nez6y3b906LUUhiUZ8rbT0ooPxvXT0wdGOUmDON5RdmXtY58MjZwHpDMDUKpjKzZi6GHiFNefjmZ";
    private int PAYPAL_REQUEST_CODE=123;
    private float totalPrice;
    public static PayPalConfiguration configuration;
    Random random=new Random();
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference userorderReference = FirebaseDatabase.getInstance().getReference("UserOrders");
    DatabaseReference managerorderRefernce = FirebaseDatabase.getInstance().getReference("ManagerOrders");
    DatabaseReference previusorderReference = FirebaseDatabase.getInstance().getReference("Previusorder");
    int type_of_delivery,radioId;
    String Name,Phone,Addre,CustomerId,Mode="Cash",Time,Price,Counter,Type;
    String OrderId ;
    Calendar calendar;
    Dialog confirmOrderDialog,setAddress,updatePersonalDetails;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_check_out, container, false);
        backBtn = view.findViewById(R.id.backBtn);
        cancelBtn = view.findViewById(R.id.cancel);
        payBtn = view.findViewById(R.id.payment);
        checkoutData = view.findViewById(R.id.checkoutData);
        checkoutLoader = view.findViewById(R.id.checkoutLoader);

        deliveryGroup = view.findViewById(R.id.Deiveryradiogrp);
        takeawayGroup = view.findViewById(R.id.Takeawayradiogrp);

        orderBtn = view.findViewById(R.id.ordereditBtn);
        userBtn = view.findViewById(R.id.userdetailsBtn);

        orderno = view.findViewById(R.id.orderid);
        itemcount = view.findViewById(R.id.count);
        time = view.findViewById(R.id.time);
        price = view.findViewById(R.id.totalprice);
        type = view.findViewById(R.id.type_of_delivery);

        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        phone = view.findViewById(R.id.phone);

        configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(ClientId);

        confirmOrderDialog = new Dialog(getContext());
        confirmOrderDialog.setContentView(R.layout.checkoutpopup_ok);
        confirmOrderDialog.setCanceledOnTouchOutside(false);

        setAddress = new Dialog(getContext());
        setAddress.setContentView(R.layout.set_address_card);
        setAddress.setCanceledOnTouchOutside(false);

        updatePersonalDetails = new Dialog(getContext());
        updatePersonalDetails.setContentView(R.layout.modify_user_details);
        updatePersonalDetails.setCanceledOnTouchOutside(false);

        preferences = getActivity().getSharedPreferences("DelhiGarden", Context.MODE_PRIVATE);
        type_of_delivery = preferences.getInt("DeliveryType",0);
        CustomerId = preferences.getString("CustomerId",null);
        Name = preferences.getString("name",null);
        Phone = preferences.getString("phone",null);
        Addre = preferences.getString("address",null);
        Price = getArguments().getString("price");
      Counter = getArguments().getString("count");
        if (type_of_delivery != 0){
            if (type_of_delivery ==1){
                Type = "Take Away";
                takeawayGroup.setVisibility(View.VISIBLE);
                deliveryGroup.setVisibility(View.GONE);
                takeawayGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i==R.id.radioButton4){
                            radioButton = view.findViewById(R.id.radioButton4);
                            radioId=R.id.radioButton4;
                            Mode ="Cash";
                        }if (i==R.id.radioButton5){
                            radioButton = view.findViewById(R.id.radioButton5);
                            Mode ="Online Payment";
                            radioId=R.id.radioButton5;
                        }
                    }
                });
            }
            if (type_of_delivery==2){
                Type = "Home Delivery";
                takeawayGroup.setVisibility(View.GONE);
                deliveryGroup.setVisibility(View.VISIBLE);
                deliveryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i==R.id.radioButton){
                            radioButton = view.findViewById(R.id.radioButton);
                            radioId = R.id.radioButton;
                            Mode = "COD";
                        }if (i==R.id.radioButton2){
                            radioButton = view.findViewById(R.id.radioButton2);
                            Mode = "Card Swipe";
                            radioId = R.id.radioButton2;
                        }if (i==R.id.radioButton3){
                            radioButton = view.findViewById(R.id.radioButton3);
                            Mode = "Online Payment";
                            radioId=R.id.radioButton3;
                        }
                    }
                });

            }
        }
        getOrderId();
        getCurrentTime();

        orderno.setText(OrderId);
        time.setText(Time);
        price.setText(Price);
        type.setText(Type);
        itemcount.setText(Counter);

        name.setText(Name);
        phone.setText("+91-"+Phone);
        if (Addre != null) {
            address.setText(Addre);
        } else {
            address.setText("No Address register yet");
        }


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton = view.findViewById(radioId);
                if (Addre==null){
                    setAddress.show();
                    setAddress.getWindow();
                    Button btn = setAddress.findViewById(R.id.save);
                    EditText add = setAddress.findViewById(R.id.address);
                  btn.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          String val = add.getText().toString();
                          if (val.isEmpty()){add.setError("Field can't be empty");}
                          else {
                              String s = preferences.getString("address",null);
                              if (s==null){
                                  Addre =val;
                                  address.setText(Addre);
                                  preferences.edit().putString("address",val).apply();
                                  setAdressToDB(val);
                              }else {Addre=val;}
                              setAddress.dismiss();
                          }

                      }
                  });
                }
                else if (radioId == R.id.radioButton3 | radioId == R.id.radioButton5){
                    goToPaymentGateway();
                }else{
                      goToPopup();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getParentFragmentManager().findFragmentById(R.id.cartLayout);
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePersonalDetails.show();
                EditText add = updatePersonalDetails.findViewById(R.id.address);
                EditText username = updatePersonalDetails.findViewById(R.id.name);
                EditText phonenumber = updatePersonalDetails.findViewById(R.id.phone);
                Button btn = updatePersonalDetails.findViewById(R.id.save);
                ImageView close = updatePersonalDetails.findViewById(R.id.close);
                username.setHint(Name);
                phonenumber.setHint(Phone);
                if (Addre==null) {
                    add.setHint("Enter Address");
                } else {
                    add.setHint(Addre);
                }
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updatePersonalDetails.dismiss();
                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       String val = add.getText().toString();
                       String va1 = username.getText().toString();
                       String val2 = phonenumber.getText().toString();
                       if (!val.isEmpty() && !va1.isEmpty() && !val2.isEmpty()){
                          address.setText(val);
                          name.setText(va1);
                          phone.setText("+91-"+val2);
                          String s = preferences.getString("address",null);
                          if (s==null){
                              Addre =val;
                              preferences.edit().putString("address",val).apply();
                              setAdressToDB(val);
                          }else { Addre=val;}
                          updatePersonalDetails.dismiss();
                       }else {
                           if (val.isEmpty()){add.setError("Field can't be empty");}
                           if (va1.isEmpty()){username.setError("Field can't be empty");}
                           if (val2.isEmpty()){phonenumber.setError("Field can't be empty");}
                       }
                    }
                });
            }
        });
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getParentFragmentManager().findFragmentById(R.id.cartLayout);
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), Home.class));
            }
        });

        return view;
    }

    private void setAdressToDB(String val) {
        userReference.child(CustomerId).child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (!snapshot.exists()){
                   SetAddressHelper model = new SetAddressHelper(val);
                   userReference.child(CustomerId).child("address").setValue(val);
               } else return;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void getCurrentTime() {
        calendar = Calendar.getInstance();
        int d,m,h,mi,t;
        d=calendar.get(Calendar.DAY_OF_MONTH);
        m=calendar.get(Calendar.MONTH);
        h=calendar.get(Calendar.HOUR);
        mi=calendar.get(Calendar.MINUTE);
        t=calendar.get(Calendar.AM_PM);
        Time=d+"/"+m+"-"+h+":"+mi+" "+((t==0)?"AM":"PM");
    }

    private void goToPopup() {
        confirmOrderDialog.show();
        SendDataToDB();
        SetPreviusOrderDb();
        Button btn = confirmOrderDialog.findViewById(R.id.lottiebtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmOrderDialog.dismiss();

                ((UserChoosenFood) CartHelper.getmyContext()).clearList();
                Intent intent = new Intent(getActivity(), TrackOrder.class);
                preferences.edit().putString("orderId",OrderId).apply();
                startActivity(intent);
            }
        });
    }

    private void SetPreviusOrderDb() {
       Stack<CartModel> list= ((UserChoosenFood) CartHelper.getmyContext()).getItem();
       previusorderReference.child(CustomerId).removeValue();
       for (int i=0;i<list.size();i++){
           CartModel mode = list.get(i);
           previusorderReference.child(CustomerId).child(mode.getFid()).setValue(mode.getFid());
       }
    }

    private void SendDataToDB() {
        Stack<CartModel> list = ((UserChoosenFood) CartHelper.getmyContext()).getItem();
        OrderHelper orderHelper = new OrderHelper(OrderId,CustomerId,Name,Phone,Addre,Price,Time,Mode,Type,"confirming");
        userorderReference.child(CustomerId).child(OrderId.substring(1)).setValue(orderHelper);
        managerorderRefernce.child(OrderId.substring(1)).setValue(orderHelper);
        for (int i=0;i<list.size();i++){
            CartModel model = list.get(i);
            String foodid = model.getFid();
            String itemid = "Item"+(i+1);
            String foodname = model.getName();
            String foodqnt = String.valueOf(model.getQnt());
            OrderItemsHelper items = new OrderItemsHelper(itemid,foodname,foodid,foodqnt);
            userorderReference.child(CustomerId).child(OrderId.substring(1)).child("Items").child(itemid).setValue(items);
            managerorderRefernce.child(OrderId.substring(1)).child("Items").child(itemid).setValue(items);
        }
    }

    private void goToPaymentGateway() {
        totalPrice = Float.parseFloat(Price.substring(2));
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(totalPrice)),"USD","Delhi Garden",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE && data!=null && resultCode==-1){
            PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (paymentConfirmation!=null){
                try {
                    String paymentDetails = paymentConfirmation.toJSONObject().toString();
                    JSONObject object = new JSONObject(paymentDetails);

                }catch (JSONException e){
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Error : ", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(getActivity(), "Invalid payment", Toast.LENGTH_SHORT).show();
        }else {
            goToPopup();
        }
    }
    private void getOrderId() {
         String str="#";
        for(int i=1;i<=7;i++){
             int pos = random.nextInt(character.length());
             str+= (char) character.charAt(pos);
        }
        OrderId = str;
    }

}