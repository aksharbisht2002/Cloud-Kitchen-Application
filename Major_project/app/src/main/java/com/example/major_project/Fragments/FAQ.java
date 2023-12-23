package com.example.major_project.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.major_project.Adapter.QuestionAdapter;
import com.example.major_project.Helper.QuestionHelper;
import com.example.major_project.Home;
import com.example.major_project.R;

import java.util.ArrayList;
import java.util.List;


public class FAQ extends Fragment {
    ImageView imageView1 , imageView2,backBtn;
    RecyclerView recyclerView;
    List<QuestionHelper> questionList;
    private  String phone = "8505959753";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_f_a_q, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        imageView1 = view.findViewById(R.id.call);
        imageView2 = view.findViewById(R.id.mail);
        backBtn = view.findViewById(R.id.menu_backBtn);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Home.class));
            }
        });


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!=
                        PackageManager.PERMISSION_GRANTED
                ){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
                }else {
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:"+phone));
                    getActivity().startActivity(i);
                }

            }
        });


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }

            private void sendEmail() {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"delhigarden@gmail.com"});

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "app not found", Toast.LENGTH_SHORT).show();
                }
            }
        });



        initData();
        setRecyclerView();
        return view;
    }


    private void setRecyclerView() {
        QuestionAdapter questionAdapter = new QuestionAdapter(questionList);
        recyclerView.setAdapter(questionAdapter);
        recyclerView.setHasFixedSize(true);


    }

    private void initData() {

        questionList = new ArrayList<>();
        questionList.add(new QuestionHelper( "01. I don't remember my password?" ,"\n" +
                "Answer: You have already created an account but you can't remember your password? Click on 'Login/Sign Up' at the top of the page. Then click on 'Forgot Password?'. Fill out your phone number and a password recovery will be sent to you by phone."));
        questionList.add(new QuestionHelper( "02. How can I create an account at DelhiGarden.com?" ,"Answer: Click on 'Login & Sign up' at the top of the page. Then fill out your information in the 'Create an account' section and click the 'Sign Up' button. You can also create an account directly after placing an order. Your delivery address and the order details will then be saved in your account."));
        questionList.add(new QuestionHelper( "03. What are your delivery hours?" ,"Answer: Our delivery hour is from 10:00 AM to 08:00 PM."));
        questionList.add(new QuestionHelper( "04. How much time it takes to deliver the order?" ,"\n" +
                "Answer: Generally it takes between 45 minutes to 1 hour time to deliver the order. Due to long distance or heavy traffic, delivery might take few extra minutes."));
        questionList.add(new QuestionHelper( " 05. How do I know status of my order?" ,"\n" +
                "Answer: After you place your order, it is sent immediately to the bliss kitchen, which starts preparing it without any delay. The kitchen does everything to process your order as quickly as possible. However, sometimes Blissmeal receives large amount of orders, or drivers get stuck in heavy traffic this might cause little delays. If the amount of time you've waited has exceeded the estimated delivery time, you may please contact us. You will a receive an SMS as soon as your order is dispatched."));
        questionList.add(new QuestionHelper( "06. How to order online at DelhiGarden " ,"Answer: It is really easy, as easy as 1, 2, 3...\n" +
                "\n" +
                " (I) Tell us where you are: enter your location so that we know your address to deliver your order.\n" +
                "(II) Choose what you would like: select items you'd like to order. You can search by cuisine type, dish name.\n" +
                "(III) Checkout: Enter your exact delivery address, payment method and your phone number. Always make sure that you enter the correct phone number to help us contact you regarding your order if needed. Now sit back, relax, and we'll get your food delivered to your doorstep."));
        questionList.add(new QuestionHelper( " 07. I need to cancel or change my order! How can I do this?" ,"Answer: Please contact helpline Number as soon as possible, we can let the kitchen know before it starts preparing your order. Once the order goes in the process, it can not be changed. With regard to any refund of a payment you have made online, please contact Bliss Meal delivery.\n" +
                "Please contact Blissful Meal delivery.\n" +
                "\n" +
                "+91 -9821764483 Or customercare@DelhiGarden.com"));
        questionList.add(new QuestionHelper( "08. Do I have to create an account to place an order?" ,"\n" +
                "Answer: Creating an account is mandatory, but you can see order without having to sign up. We make sure that ordering food online at Bliss Meal delivery is quick and fuss-free. After placing your order, you will have the option of creating an account."));
        questionList.add(new QuestionHelper( "09. Can I edit my order?" ,"Answer: Your order can be edited before it reaches the kitchen. You could contact the customer support team via a call to do so. Once an order is placed and the kitchen starts preparing your food, you may not edit its contents."));
        questionList.add(new QuestionHelper( "10. Can I order from any location?" ,"Answer: We will deliver to any limited listed area of Bhopal Initially, we will be serving at the main Bhopal area and not to old Bhopal area.\n" +
                "\n"));
        questionList.add(new QuestionHelper( "11. Do you support bulk orders?" ,"Answer: In order to provide all customers with a great selection and to ensure on-time delivery of your meal, we request you to order bulk quantity at least 24 hours in advance."));
        questionList.add(new QuestionHelper( "12. Did not receive order confirmation SMS?" ,"Answer: Please check if your text message. If not, please share the details via customer Support team.\n" +
                "\n" +
                "Mail Id: customercare@DelhiGarden.com Phone No: +91 - 9821764483\n"));
        questionList.add(new QuestionHelper( "13. I want an invoice for my order?" ,"Answer: Invoice Copy is sent via email & Text Message."));


    }


}