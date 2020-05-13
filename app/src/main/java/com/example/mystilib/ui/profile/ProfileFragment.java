package com.example.mystilib.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mystilib.R;
import com.example.mystilib.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private static final int CHOOSE_IMAGE = 101;
    ImageView imageView;
    EditText editText;
    Uri uriProfileImage;
    TextView text_email;
    private ProgressDialog progressDialog;

    String profileImageUrl;
    FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        editText = (EditText) root.findViewById(R.id.DisplayName);
        text_email = (TextView) root.findViewById(R.id.text_email);
        imageView = (ImageView) root.findViewById(R.id.imageView);

        Button save = (Button) imageView.findViewById(R.id.button_save);
        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showImageChooser();
            }
        });

        root.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveUserInformation();
            }
        });
        // load User Information
        loadUserInformation();
        return root;
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user !=null) {
            if (user.getPhotoUrl() != null) {
                String photoUrl = user.getPhotoUrl().toString();
                Glide.with(this).load(photoUrl).into(imageView);
            }
            if (user.getDisplayName() != null) {
                editText.setText(user.getDisplayName());
            }
            if (user.getEmail() != null) {
                text_email.setText(user.getEmail());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()== null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    private void saveUserInformation() {
        String displayName = editText.getText().toString().trim();
        if(displayName.isEmpty()){
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        // Update image && || displayname
        if(user!=null && profileImageUrl != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(Uri.parse(profileImageUrl)).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(),"Profile updated",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        // Update only displayname
        else if(user!=null && user.getPhotoUrl() != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(user.getPhotoUrl()).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(),"Profile updated",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity(),"Fail to updated",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        // First update -> upload a default profile image
        else{
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(Uri.parse("http://www.eliterawtalent.com/wp-content/uploads/2018/10/default-profile-picture-gmail-2.png")).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(),"Profile updated",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        return;
    }
    // after choosing an image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);
                uploadImageToFireBaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFireBaseStorage() {
        // create ref for firebase storage to store the profile pic
        FirebaseUser user = mAuth.getCurrentUser();
        // Path of the image location
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+user.getUid()+".jpg");
        if(uriProfileImage != null){
            progressDialog.setMessage("Uploading picture please wait...");
            progressDialog.show();
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            Uri downloadUrl = uri;
                            profileImageUrl = downloadUrl.toString();
                            Toast.makeText(getActivity(),"Profile picture uploaded",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Profile image"),CHOOSE_IMAGE);
    }
}
