package com.example.major_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.major_project.Fragments.CheckOut;
import com.example.major_project.Model.CravingLayoutModel;
import com.example.major_project.Model.FoodModel;
import com.example.major_project.Model.OutOfStockModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SendMenuDataInDB extends AppCompatActivity {
    ImageView image;
    EditText name,type,price,fid,desc;
    AppCompatButton upload;
    Uri imageUri;
    SharedPreferences preferences;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference("CravingMenu");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Outofstock");
    private String CustomerId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_menu_data_in_db);
        image = findViewById(R.id.imageView);
        name = findViewById(R.id.foodname);
        price = findViewById(R.id.price);
        type = findViewById(R.id.type);
        fid=findViewById(R.id.foodid);
        desc = findViewById(R.id.fooddesc);
        upload = findViewById(R.id.upload);
        image.setImageResource(R.drawable.gallery);

        preferences =getSharedPreferences("DelhiGarden", Context.MODE_PRIVATE);
        CustomerId = preferences.getString("CustomerId",null);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,0);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        String foodid = fid.getText().toString();
                        String foodname = name.getText().toString();
                        String foodprice = price.getText().toString();
                         String foodtype = type.getText().toString();
                       String fooddesc = desc.getText().toString();
                        String foodcatagory = "Drinks";
                           databaseReference.child(foodid).setValue(foodid);
                          fid.setText("");

//                           databaseReference.addValueEventListener(new ValueEventListener() {
//                               @Override
//                               public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                   for (DataSnapshot data:
//                                        snapshot.getChildren()) {
//                                       String fid = data.child("fid").getValue(String.class);
//                                       Toast.makeText(SendMenuDataInDB.this, fid, Toast.LENGTH_LONG).show();
//                                   }
//                                }
//                               @Override
//                               public void onCancelled(@NonNull DatabaseError error) {
//
//                               }
//                           });


//                        StorageReference fillref = storageReference.child(foodid+"."+getFileExtansion(imageUri));
//                        fillref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                fillref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//
//                                        String foodImage = uri.toString();
//
//                                       // FoodModel model = new FoodModel(foodid, foodcatagory, foodtype, foodname, fooddesc, foodprice, foodImage);
//                                        CravingLayoutModel model = new CravingLayoutModel(foodid,foodImage,foodname);
//                                        databaseReference.child(foodid).setValue(foodid);
//                                        Toast.makeText(SendMenuDataInDB.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
//                                        fid.setText("");
//                                        name.setText("");
//                                        price.setText("");
//                                        type.setText("");
//                                        desc.setText("");
//                                        image.setImageResource(R.drawable.gallery);
//
//                                    }
//                                });
//                            }
//                        }).
//                                addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(SendMenuDataInDB.this, "Uploading failed", Toast.LENGTH_SHORT).show();
//                            }
//                        });

            }
        });

    }

    private String getFileExtansion(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }
}