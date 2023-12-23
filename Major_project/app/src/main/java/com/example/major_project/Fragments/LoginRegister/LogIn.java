package com.example.major_project.Fragments.LoginRegister;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.major_project.Home;
import com.example.major_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends Fragment {
TextInputEditText email,pass;
TextInputLayout fieldemail,fieldpassword;
TextView regbtn,forgetPass;
ImageView google,facebook;
Button login;
ProgressBar progressBar;
SharedPreferences preferences;

DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        email = view.findViewById(R.id.email);
        pass = view.findViewById(R.id.password);
        fieldemail = view.findViewById(R.id.fieldemail);
        fieldpassword = view.findViewById(R.id.fieldpass);
        regbtn=view.findViewById(R.id.textView6);
        google = view.findViewById(R.id.imageView3);
        facebook = view.findViewById(R.id.imageView4);
        login = view.findViewById(R.id.logintohome);
        forgetPass = view.findViewById(R.id.forgpass);
        progressBar = view.findViewById(R.id.processbar);


        preferences = getActivity().getSharedPreferences("DelhiGarden", Context.MODE_PRIVATE);


        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LoginRegister,new SignUp()).commit();
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LoginRegister,new ForgetPassword()).commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                  ComfirmLogin(view);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return  view;
    }

    private void ComfirmLogin(View view) {
        if (!validEmail() | !validPassword()){
            return;
        }else {
            String Email = email.getText().toString();
            String Password = pass.getText().toString();
            userReference.orderByChild("email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.exists()){
                     fieldemail.setError(null);
                     fieldpassword.setError(null);
                     //Toast.makeText(getActivity(), CustomerId, Toast.LENGTH_SHORT).show();
                    mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                 if (task.isSuccessful()){
                                     preferences.edit().putBoolean("Login",true).apply();

                                     userReference.addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                                             for (DataSnapshot data:
                                                     snapshot.getChildren()) {
                                                 if ((data.child("email").getValue()).equals(Email)){
                                                     login.setVisibility(View.VISIBLE);
                                                     progressBar.setVisibility(View.INVISIBLE);
                                                     String CustomerId = data.child("cid").getValue().toString();
                                                     String name = data.child("name").getValue(String.class);
                                                     String phone = data.child("phone").getValue(String.class);
                                                     String email = data.child("email").getValue(String.class);
                                                     if (data.child("address").exists()){
                                                         String address = data.child("address").getValue(String.class);
                                                         preferences.edit().putString("address",address).apply();
                                                     }else {
                                                         preferences.edit().putString("address",null).apply();
                                                     }
                                                     preferences.edit().putString("CustomerId",CustomerId).apply();
                                                     preferences.edit().putString("name",name).apply();
                                                     preferences.edit().putString("email",email).apply();
                                                     preferences.edit().putString("phone",phone).apply();
                                                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getActivity(),Home.class));
                                                     break;
                                                 }
                                             }
                                         }
                                         @Override
                                         public void onCancelled(@NonNull DatabaseError error) {}
                                     });

                                 }else {
                                     login.setVisibility(View.VISIBLE);
                                     progressBar.setVisibility(View.INVISIBLE);
                                     fieldpassword.setError("Wrong Password");
                                 }
                        }
                    });
                 }else{
                     fieldemail.setError("Email does not registered");
                     login.setVisibility(View.VISIBLE);
                     progressBar.setVisibility(View.INVISIBLE);
                 }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });

        }
    }

    private boolean validPassword() {
        String val = pass.getText().toString();
        if (val.isEmpty()) {
            fieldpassword.setError("Field can't be empty");
            return false;
        } else {
            fieldpassword.setError(null);
            fieldpassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validEmail() {
        String val = email.getText().toString();
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            fieldemail.setError("Field can't be empty");
            return false;
        } else if (!val.matches(emailpattern)) {
            fieldemail.setError("Invalide Email Address");
            return false;
        } else {
            fieldemail.setError(null);
            fieldemail.setErrorEnabled(false);
            return true;
        }
    }
}