package com.example.exp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;

public class Main2Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Button button;
    Bitmap bitmap;
    EditText f,l,userid,p1,pass;
    TextView t1,t2;
    String email,password,Fname,Lname2,cpassword;
    ImageView upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        link();
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload.animate().rotation(360).setDuration(2000);
                Toast.makeText(getApplicationContext(),"Click",Toast.LENGTH_SHORT).show();

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lname2=l.getText().toString();
                Fname=f.getText().toString();
                email=userid.getText().toString().trim();
                password=pass.getText().toString().trim();
                cpassword=p1.getText().toString().trim();
                if(Fname.isEmpty()){
                    f.setError("Empty Filled");
                    f.requestFocus();

                }
                else if(email.isEmpty()){
                    userid.setError("Please Enter Email");
                    userid.requestFocus();
                }
                else if(password.isEmpty()){
                    pass.setError("please Enter Password");
                    pass.requestFocus();
                }
                else if(!(password.equals(cpassword))){
                    pass.setError("Password not match");
                    pass.requestFocus();
                    p1.setError("Password not match");
                    p1.requestFocus();
                }
                else if(!(Fname.isEmpty()&&email.isEmpty()&&password.isEmpty())){
                    signup();

                }
            }
        });
    }
    public void signup(){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            String username=Fname+" "+Lname2;
                            Toast.makeText(getApplicationContext(),"Hi "+ username, Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference().child("my_user").child(task.getResult().getUser().getUid()).child("Username").setValue(username);
                            Intent i1=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i1);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }
    public void link(){
        mAuth = FirebaseAuth.getInstance();
        button=(Button)findViewById(R.id.btn);
        f=(EditText)findViewById(R.id.name);
        l=(EditText)findViewById(R.id.name4);
        userid=(EditText)findViewById(R.id.edittext4);
        pass=(EditText)findViewById(R.id.edittext5);
        p1=(EditText)findViewById(R.id.edittext6);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
       }
    private void selectImage() {
        if (Build.VERSION.SDK_INT < 23) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1000);
        }
        if (Build.VERSION.SDK_INT >= 23)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);

            } else {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            selectImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            Uri chosenImageData = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageData);
                upload.setImageBitmap(bitmap);
                } catch (Exception e){

        }
        }

    }
}
