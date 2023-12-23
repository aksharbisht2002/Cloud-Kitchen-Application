package com.example.major_project.Fragments.LoginRegister;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.major_project.Helper.LoginHelper;
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


public class SignUp extends Fragment {
TextView login;
 Button regBtn;
 LinearLayout layout;
 View v;
 CheckBox ch;
 TextInputLayout fieldname,fieldemail,fieldpass,fieldphone;
 TextInputEditText name,email,pass,phone;

 DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
  FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        v=view;
         login = view.findViewById(R.id.textView9);
         regBtn = view.findViewById(R.id.Registerbutton);
         ch = view.findViewById(R.id.checkbox);
         layout = view.findViewById(R.id.aggrementLayout);

         fieldname = view.findViewById(R.id.fieldname);
         fieldemail = view.findViewById(R.id.fieldemail);
         fieldpass = view.findViewById(R.id.fieldpass);
         fieldphone = view.findViewById(R.id.fieldphone);

         name = view.findViewById(R.id.username);
         email = view.findViewById(R.id.email);
         pass = view.findViewById(R.id.password);
         phone = view.findViewById(R.id.phone);

         regBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (ch.isChecked()){
                     RegisterUser(view);
                 }else{
                     Toast.makeText(getActivity(), "Read Term & Condition", Toast.LENGTH_SHORT).show();
                 }
             }
         });

         login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LoginRegister,new LogIn()).commit();
             }
         });

        return view;
    }

    private void RegisterUser(View view) {
        if (!validName() | !validEmail() | !validPassword() | !validPhone()){
            return;
        }else {
            String Email = email.getText().toString();
            userReference.orderByChild("email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        fieldemail.setError("Email Already Registered");
                    }else {
                        fieldemail.setError(null);
                        fieldemail.setErrorEnabled(false);
                        String Name = name.getText().toString();
                        String Phone = phone.getText().toString();
                        String Email = email.getText().toString();
                        String PassWord = pass.getText().toString();
                        String CustomerId=Name.substring(0,4)+""+Phone.substring(6,10);
                        LoginHelper loginHelper = new LoginHelper(CustomerId,Name,Email,Phone);
                        userReference.child(CustomerId).setValue(loginHelper);
                        CreateUserInFirebaseAuth(Email,PassWord,CustomerId);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void CreateUserInFirebaseAuth(String email, String passWord,String CustomerId) {
        mAuth.createUserWithEmailAndPassword(email,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()){
                  Toast.makeText(getActivity(),"Registration Complete",Toast.LENGTH_LONG).show();
                  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LoginRegister, new LogIn()).commit();
              }else{
                  userReference.child(CustomerId).setValue(null);
              }
            }
        });
    }

    private boolean validPhone(){

        String val = phone.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            fieldphone.setError("Field can't be empty");
            return false;
        } else if (val.length()!=10) {
            fieldphone.setError("Enter a valid number");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            fieldphone.setError("WhiteSpace Not Allow");
            return  false;
        } else {
            fieldphone.setError(null);
            fieldphone.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validPassword() {
        String val = pass.getText().toString();
        String passwordVerification ="^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";
        if (val.isEmpty()) {
            fieldpass.setError("Field can't be empty");
            return false;
        } else if (!val.matches(passwordVerification)) {
            fieldpass.setError("Password is weak");
            return false;
        }else {
            fieldpass.setError(null);
            fieldpass.setErrorEnabled(false);
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

    private boolean validName() {
        String val = name.getText().toString();
        if (val.isEmpty()) {
            fieldname.setError("Field can't be empty");
            return false;
        } else if (val.length()< 4) {
            fieldname.setError("Atleast 4 Characters");
            return false;

        } else {
            fieldname.setError(null);
            fieldname.setErrorEnabled(false);
            return true;
        }
    }
}