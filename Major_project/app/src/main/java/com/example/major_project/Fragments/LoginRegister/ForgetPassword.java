package com.example.major_project.Fragments.LoginRegister;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.major_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassword extends Fragment {
      TextInputEditText email;
      TextInputLayout fieldEmail;
      Button send;
      LinearLayout back;

      DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
      FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        email = view.findViewById(R.id.email);
        fieldEmail = view.findViewById(R.id.fieldemail);
        send = view.findViewById(R.id.sendEmail);
        back = view.findViewById(R.id.backBtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LoginRegister,new LogIn()).commit();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validEmail()){return;}
                else {
                    String Email = email.getText().toString();
                    userReference.orderByChild("email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                          if (snapshot.exists()){
                              mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Email Send Successfully", Toast.LENGTH_LONG).show();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LoginRegister,new LogIn()).commit();
                                    }
                                  }
                              });
                          }
                          else {
                             fieldEmail.setError("Enter the Register Email");
                          }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }

            }
        });

        return view;
    }

    private boolean validEmail() {
        String val = email.getText().toString();
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            fieldEmail.setError("Field can't be empty");
            return false;
        } else if (!val.matches(emailpattern)) {
            fieldEmail.setError("Invalide Email Address");
            return false;
        } else {
            fieldEmail.setError(null);
            fieldEmail.setErrorEnabled(false);
            return true;
        }
    }
}