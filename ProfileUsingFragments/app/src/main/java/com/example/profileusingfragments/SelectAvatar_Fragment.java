package com.example.profileusingfragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class SelectAvatar_Fragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private Map<Integer, Integer> imageMap = new HashMap<Integer, Integer>() {{
        put(R.id.firstAvatar, R.drawable.avatar_f_1);
        put(R.id.secondAvatar, R.drawable.avatar_f_2);
        put(R.id.thirdAvatar, R.drawable.avatar_f_3);
        put(R.id.fourthAvatar, R.drawable.avatar_m_1);
        put(R.id.fifthAvatar, R.drawable.avatar_m_2);
        put(R.id.sixthAvatar, R.drawable.avatar_m_3);
    }};

    ImageView firstIv;
    ImageView secondIv;
    ImageView thirdIv;
    ImageView fourthIv;
    ImageView fifthIv;
    ImageView sixthIv;

    public SelectAvatar_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_avatar_, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firstIv = getView().findViewById(R.id.firstAvatar);
        secondIv = getView().findViewById(R.id.secondAvatar);
        thirdIv = getView().findViewById(R.id.thirdAvatar);
        fourthIv = getView().findViewById(R.id.fourthAvatar);
        fifthIv = getView().findViewById(R.id.fifthAvatar);
        sixthIv = getView().findViewById(R.id.sixthAvatar);

        firstIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAvatarClicked(imageMap.get(view.getId()));
            }
        });

        secondIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAvatarClicked(imageMap.get(view.getId()));
            }
        });

        thirdIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAvatarClicked(imageMap.get(view.getId()));
            }
        });

        fourthIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAvatarClicked(imageMap.get(view.getId()));
            }
        });

        fifthIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAvatarClicked(imageMap.get(view.getId()));
            }
        });

        sixthIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAvatarClicked(imageMap.get(view.getId()));
            }
        });
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


//    public void onAvatarClicked(View view) {
//        Integer iv = imageMap.get(view.getId());
//        Log.d("avatar id: ", String.valueOf(view.getId()));
//        mListener.onAvatarClicked(imageMap.get(view.getId()));
//    }

    public void onAvatarClicked(int image) {
        mListener.onAvatarClicked(image);
    }

    public interface OnFragmentInteractionListener {
        void onAvatarClicked(int image);
    }
}
