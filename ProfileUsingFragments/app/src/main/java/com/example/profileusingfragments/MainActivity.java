package com.example.profileusingfragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyProfileFragment.OnFragmentInteractionListener, SelectAvatar_Fragment.OnFragmentInteractionListener, DisplayProfileFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//
//        editor.putInt("av_image", R.drawable.select_image);
//        editor.commit();

        getSupportFragmentManager().beginTransaction().add(R.id.container, new MyProfileFragment(), "my_profile").commit();
    }

    @Override
    public void goToSelectAvatar() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SelectAvatar_Fragment(), "select_avatar").addToBackStack(null).commit();
    }

    @Override
    public void saveEnteredProfile(String name, String lastName, String studentId, String selectedDept) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        ArrayList<String> initialUserInfoList = new ArrayList<>();
        initialUserInfoList.add(name);
        initialUserInfoList.add(lastName);
        initialUserInfoList.add(studentId);
        initialUserInfoList.add(selectedDept);
        //initialUserInfoList.add(imageId);

        Gson gson = new Gson();
        String userInfoListJsonString = gson.toJson(initialUserInfoList);

        editor.putString("UserProfile", userInfoListJsonString);
        editor.commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new DisplayProfileFragment(), "display_profile").commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onAvatarClicked(int image) {
        //onBackPressed();
        //Toast.makeText(this, "image"+image, Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("av_image", image);
        editor.commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyProfileFragment(), "my_profile").commit();
        MyProfileFragment myProfileFragment = (MyProfileFragment) getSupportFragmentManager().findFragmentByTag("my_profile");
    }

    @Override
    public void editUserProfile() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MyProfileFragment())
                .commit();
    }
}
