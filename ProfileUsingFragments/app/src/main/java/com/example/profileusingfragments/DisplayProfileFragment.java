package com.example.profileusingfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;


public class DisplayProfileFragment extends Fragment {
    private TextView nameTV;
    private TextView studentIdTV;
    private TextView depTV;
    private ImageView profilePic;
    private Button editBtn;

    private OnFragmentInteractionListener mListener;

    public DisplayProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nameTV = getView().findViewById(R.id.userNameTv);
        studentIdTV = getView().findViewById(R.id.studentIdTv);
        depTV = getView().findViewById(R.id.depTv);
        profilePic = getView().findViewById(R.id.imageView7);
        editBtn = getView().findViewById(R.id.editBtn);

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int image = sharedPreferences.getInt("av_image", R.drawable.select_image);
        profilePic.setImageDrawable(getActivity().getDrawable(image));

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String userInfoListJsonString = sharedPref.getString("UserProfile","");
        String[] initialUserInfoList = new String[4];
        Gson gson = new Gson();
        initialUserInfoList = gson.fromJson(userInfoListJsonString, String[].class);

        for (int i = 0; i < initialUserInfoList.length; i++) {
            String str = initialUserInfoList[i];
            if(i==0) nameTV.setText(str);
            if(i==1) nameTV.setText("Name:\t\t"+nameTV.getText() + " " + str);
            if(i==2) studentIdTV.setText("Student ID:\t"+str);
            if(i==3) depTV.setText("Department:\t"+str);
            if(i==4) {
                //profilePic.setImageResource(Integer.parseInt(str));
                switch (str) {
                    case "1":
                        profilePic.setImageResource(R.drawable.avatar_f_1);
                        break;
                    case "2":
                        profilePic.setImageResource(R.drawable.avatar_f_2);
                        break;
                    case "3":
                        profilePic.setImageResource(R.drawable.avatar_f_3);
                        break;
                    case "4":
                        profilePic.setImageResource(R.drawable.avatar_m_1);
                        break;
                    case "5":
                        profilePic.setImageResource(R.drawable.avatar_m_2);
                        break;
                    case "6":
                        profilePic.setImageResource(R.drawable.avatar_m_3);
                        break;
                }

                //profilePic.setImageResource(getContext().getResources().getIdentifier("avatar_m_3","drawable",getContext().getPackageName()));
            }
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.editUserProfile();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_display_profile, container, false);



//        nameTV = view.findViewById(R.id.userNameTv);
//        studentIdTV = view.findViewById(R.id.studentIdTv);
//        depTV = view.findViewById(R.id.depTv);
//        profilePic = view.findViewById(R.id.imageView7);
//        editBtn = view.findViewById(R.id.editBtn);
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


    public interface OnFragmentInteractionListener {
        void editUserProfile();
    }
}
