package com.example.profileusingfragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    ImageView avatarIv;
    EditText NameEt;
    EditText lastNameEt;
    EditText studentIdEt;
    RadioGroup departmentRg;
    RadioButton otherRb;
    Button saveBtn;
    String SelectedDept;
    private final String SIMPLE_VALIDATION_REGEX = "[a-zA-Z0-9\\-!\\.\\'\\?\\s]{0,1000}";

    public MyProfileFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        avatarIv = getView().findViewById(R.id.avatarIv);
        NameEt = getView().findViewById(R.id.nameEt);
        lastNameEt = getView().findViewById(R.id.lastNameEt);
        studentIdEt = getView().findViewById(R.id.studentIdEt);
        departmentRg = getView().findViewById(R.id.departmentRg);
        saveBtn = getView().findViewById(R.id.saveBtn);
        otherRb = getView().findViewById(R.id.otherRb);

        //avatarIv.setImageResource(R.drawable.avatar_f_1);
        avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToSelectAvatar();
            }
        });

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String userInfoListJsonString = sharedPreferences.getString("UserProfile", "");

        int image = sharedPreferences.getInt("av_image", R.drawable.select_image);
        avatarIv.setImageDrawable(getActivity().getDrawable(image));

        if (userInfoListJsonString != null && !userInfoListJsonString.equals("")) {

            String[] initialUserInfoList = new String[4];
            Gson gson = new Gson();
            initialUserInfoList = gson.fromJson(userInfoListJsonString, String[].class);

            for (int i = 0; i < initialUserInfoList.length; i++) {
                String str = initialUserInfoList[i];
                if (i == 0) NameEt.setText(str);
                if (i == 1) lastNameEt.setText(str);
                if (i == 2) studentIdEt.setText(str);
                if (i == 3) {
                    switch (str) {
                        case "BIO":
                            RadioButton rb = getActivity().findViewById(R.id.bioRb);
                            rb.setChecked(true);
                            SelectedDept = "BIO";
                            break;
                        case "CS":
                            RadioButton cs = getActivity().findViewById(R.id.csRb);
                            cs.setChecked(true);
                            SelectedDept = "CS";
                            break;
                        case "SIS":
                            RadioButton sis = getActivity().findViewById(R.id.sisRb);
                            sis.setChecked(true);
                            SelectedDept = "SIS";
                            break;
                        case "Other":
                            RadioButton other = getActivity().findViewById(R.id.otherRb);
                            other.setChecked(true);
                            SelectedDept = "Other";
                            break;
                    }
                }

            }

        }

        departmentRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("radio button", "radio button changed");
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.csRb:
                        SelectedDept = "CS";
                        break;
                    case R.id.sisRb:
                        SelectedDept = "SIS";
                        break;
                    case R.id.bioRb:
                        SelectedDept = "BIO";
                        break;
                    case R.id.otherRb:
                        SelectedDept = "OTHER";
                        break;
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NameEt.setError(null);
                lastNameEt.setError(null);
                studentIdEt.setError(null);
                otherRb.setError(null);

                String name = NameEt.getText().toString().trim();
                String lastName = lastNameEt.getText().toString().trim();
                String StudentId = studentIdEt.getText().toString().trim();

                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                int imageId = sharedPreferences.getInt("av_image", R.drawable.select_image);

                //int image = avatarIv.()

                boolean validFirstName = (!name.equals("") && Pattern.compile(SIMPLE_VALIDATION_REGEX).matcher(name).matches()) ? true : false;
                boolean validLastName = (!lastName.equals("") && Pattern.compile(SIMPLE_VALIDATION_REGEX).matcher(lastName).matches()) ? true : false;
                boolean validStudId = !StudentId.equals("") && !StudentId.contains("-") && !StudentId.contains(".");
                boolean validDept = (SelectedDept != null) ? true : false;
                boolean validImage = !(imageId== R.drawable.select_image);

                if (validFirstName && validLastName && validStudId && validDept && validImage) {
                    mListener.saveEnteredProfile(name, lastName, StudentId, SelectedDept);
                } else {
                    if (!validDept)
                        otherRb.setError("Invalid Input");

                    if (!validLastName)
                        lastNameEt.setError("Invalid Input");

                    if (!validFirstName)
                        NameEt.setError("Invalid Input");

                    if (!validStudId)
                        studentIdEt.setError("Invalid Input");
                    
                    if(!validImage)
                        Toast.makeText(getContext(), "Please Choose an avatar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        //avatarIv = (ImageView)view.findViewById(R.id.avatarIv);
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setAvatarIv(int image) {

        //ImageView iv = (ImageView)context.findViewById(R.id.avatarIv);
        avatarIv.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_f_1));
        Toast.makeText(getActivity().getApplicationContext(), "" + image, Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void goToSelectAvatar();

        void saveEnteredProfile(String name, String lastName, String studentId, String selectedDept);   // int image
    }


}
