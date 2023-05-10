package com.client.utedating.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.client.utedating.R;

public class SupportBottomFragment extends Fragment {
//    LinearLayout linearLayoutUnmatched, linearLayoutReport;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        linearLayoutUnmatched = view.findViewById(R.id.linearLayoutUnmatched);
//        linearLayoutReport = view.findViewById(R.id.linearLayoutReport);
    }
}