package com.example.major_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EditProfile extends AppCompatActivity {
    TextInputEditText name_ev,phone_ev,address_ev;
    Button savebtn;
    ImageView backBtn,profilePic;
    CardView editpicBtn;
    ShimmerFrameLayout loader;
    String CustomerId,name,phone,address,imageBytes;
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
    StorageReference profilepicStorage = FirebaseStorage.getInstance().getReference("ProfilePic");
    SharedPreferences preferences;
    Uri imageUri;
    boolean isAuth;
    Switch Biometric;
    int selectedPic=1;
    private int profilepicCode;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_edit);


        name_ev = findViewById(R.id.name);
        phone_ev =findViewById(R.id.phone);
        address_ev = findViewById(R.id.address);
        savebtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.navIcon);
        editpicBtn =findViewById(R.id.picEdit);
        profilePic=findViewById(R.id.profilepic);
        Biometric= findViewById(R.id.biometricSwitch);
        loader = findViewById(R.id.Shimmerloader);

        preferences = getSharedPreferences("DelhiGarden",MODE_PRIVATE);
        isAuth = preferences.getBoolean("Auth",false);
        CustomerId=preferences.getString("CustomerId",null);
        name= preferences.getString("name",null);
        phone=preferences.getString("phone",null);
        address = preferences.getString("address",null);
        profilepicCode = Integer.parseInt(preferences.getString("profilepic",String.valueOf(R.drawable.person1)));
        if (isAuth){Biometric.setChecked(true);}else {Biometric.setChecked(false);}
        if (address==null){address_ev.setHint("No Address Provided");}else {address_ev.setText(address);}


        name_ev.setText(name);
        phone_ev.setText(phone);
        profilePic.setImageResource(profilepicCode);

        editpicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto(view);
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri!=null){
                    SetProfileImage();
                }
                String new_name,new_phone,new_address;
                new_name = name_ev.getText().toString();
                new_phone = phone_ev.getText().toString();
                new_address = address_ev.getText().toString();
                if (new_name.isEmpty()) {
                    userReference.child(CustomerId).child("name").setValue(name);
                    preferences.edit().putString("name",new_name).apply();
                    name_ev.setText(new_name);
                } else {
                    userReference.child(CustomerId).child("name").setValue(new_name);
                    name_ev.setText(new_name);
                    preferences.edit().putString("name",new_name).apply();
                }
                if (new_phone.isEmpty()) {
                    userReference.child(CustomerId).child("phone").setValue(phone);
                    phone_ev.setText(new_phone);
                    preferences.edit().putString("phone",new_phone).apply();
                } else {
                    userReference.child(CustomerId).child("phone").setValue(new_phone);
                    phone_ev.setText(new_phone);
                    preferences.edit().putString("phone",new_phone).apply();
                }
                if (new_address.isEmpty()) {
                    userReference.child(CustomerId).child("address").setValue(address);
                    preferences.edit().putString("address",new_address).apply();
                } else {
                    userReference.child(CustomerId).child("address").setValue(new_address);
                    address_ev.setText(new_address);
                    preferences.edit().putString("address",new_address).apply();
                }
                Toast.makeText(EditProfile.this, "Change Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditProfile.this, Profile.class));
            }
        });

        Biometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth){
                    preferences.edit().putBoolean("Auth",false).apply();
                    isAuth = preferences.getBoolean("Auth",false);
                    Biometric.setChecked(isAuth);
                    Toast.makeText(EditProfile.this, "FingerPrint Disable", Toast.LENGTH_SHORT).show();
                }else{
                    preferences.edit().putBoolean("Auth",true).apply();
                    isAuth = preferences.getBoolean("Auth",false);
                    Biometric.setChecked(isAuth);
                    Toast.makeText(EditProfile.this, "FingerPrint Enable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(EditProfile.this, Profile.class));
            }
        });
        userReference.child(CustomerId).child("profileImg").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loader.startShimmer();
                    loader.setVisibility(View.VISIBLE);
                    profilePic.setVisibility(View.GONE);
                    Picasso.get().load(snapshot.getValue(String.class)).placeholder(profilepicCode).into(profilePic, new Callback() {
                        @Override
                        public void onSuccess() {
                            loader.stopShimmer();
                            loader.setVisibility(View.GONE);
                            profilePic.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onError(Exception e) {}
                    });
                }  else {
                    return;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    private void setPhoto() {
        if (selectedPic==1){
            profilePic.setImageResource(R.drawable.person1);
            preferences.edit().putString("profilepic",String.valueOf(R.drawable.person1)).apply();
        }
        if (selectedPic==2){
            profilePic.setImageResource(R.drawable.person2);
            preferences.edit().putString("profilepic",String.valueOf(R.drawable.person2)).apply();
        }
        if (selectedPic==3){
            profilePic.setImageResource(R.drawable.person3);
            preferences.edit().putString("profilepic",String.valueOf(R.drawable.person3)).apply();
        }
        if (selectedPic==4){
            profilePic.setImageResource(R.drawable.person4);
            preferences.edit().putString("profilepic",String.valueOf(R.drawable.person4)).apply();
        }
        if (selectedPic==5){
            profilePic.setImageResource(R.drawable.person5);
            preferences.edit().putString("profilepic",String.valueOf(R.drawable.person5)).apply();
        }
        if (selectedPic==6){
            profilePic.setImageResource(R.drawable.person6);
            preferences.edit().putString("profilepic",String.valueOf(R.drawable.person6)).apply();
        }
        if (selectedPic==7){
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i,0);
        }
    }

    private void selectPhoto(View view) {
        CardView cardView = view.findViewById(R.id.profile_pic_layout);
        View newview = LayoutInflater.from(EditProfile.this).inflate(R.layout.profile_pic_select_layout,cardView);
        CardView pic1,pic2,pic3,pic4,pic5,pic6;
        AppCompatButton btn;
        pic1 =newview.findViewById(R.id.pic1);
        pic2 =newview.findViewById(R.id.pic2);
        pic3 =newview.findViewById(R.id.pic3);
        pic4 =newview.findViewById(R.id.pic4);
        pic5 =newview.findViewById(R.id.pic5);
        pic6 =newview.findViewById(R.id.pic6);
        btn = newview.findViewById(R.id.custom_choose);

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setView(newview);
        final  AlertDialog alertDialog = builder.create();
        alertDialog.show();

        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPic!=1){
                    pic1.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.orange));
                    pic2.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic3.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic4.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic5.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic6.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    selectedPic=1;
                    userReference.child(CustomerId).child("profileImg").removeValue();
                    setPhoto();
                    alertDialog.dismiss();

                }
            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPic!=2){
                    pic1.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic2.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.orange));
                    pic3.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic4.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic5.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic6.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    selectedPic=2;
                    userReference.child(CustomerId).child("profileImg").removeValue();
                    setPhoto();
                    alertDialog.dismiss();

                }
            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPic!=3){
                    pic1.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic2.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic3.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.orange));
                    pic4.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic5.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic6.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    selectedPic=3;
                    userReference.child(CustomerId).child("profileImg").removeValue();
                    setPhoto();
                    alertDialog.dismiss();

                }
            }
        });
        pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPic!=4){
                    pic1.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic2.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic3.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic4.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.orange));
                    pic5.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic6.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    selectedPic=4;
                    userReference.child(CustomerId).child("profileImg").removeValue();
                    setPhoto();
                    alertDialog.dismiss();

                }
            }
        });
        pic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPic!=5){
                    pic1.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic2.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic3.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic4.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic5.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.orange));
                    pic6.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    selectedPic=5;
                    userReference.child(CustomerId).child("profileImg").removeValue();
                    setPhoto();
                    alertDialog.dismiss();

                }
            }
        });
        pic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPic!=6){
                    pic1.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic2.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic3.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic4.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic5.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.white));
                    pic6.setCardBackgroundColor(ContextCompat.getColor(EditProfile.this,R.color.orange));
                    selectedPic=6;
                    userReference.child(CustomerId).child("profileImg").removeValue();
                    setPhoto();
                    alertDialog.dismiss();

                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPic!=7){
                    selectedPic=7;
                    setPhoto();
                    alertDialog.dismiss();
                }
            }
        });

    }

    private void SetProfileImage() {

        StorageReference fillref = profilepicStorage.child(CustomerId+"."+getFileExtansion(imageUri));
        fillref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fillref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String image = uri.toString();
                        userReference.child(CustomerId).child("profileImg").setValue(image);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Uploading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtansion(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
        }
    }

}