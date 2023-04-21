package com.client.utedating.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.models.User;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class InterestFragment extends Fragment {
    ChipGroup chipGroup;
    Button buttonSubmitInterest;
    List<Integer> chipList = new ArrayList<>();
    List<String> interests = new ArrayList<>();
    InitialActivity initialActivity;
    public InterestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialActivity = (InitialActivity) getActivity();
        chipGroup = view.findViewById(R.id.chipGroupInterest);

        buttonSubmitInterest = view.findViewById(R.id.buttonSubmitInterest);

        chipGroup.setScrollbarFadingEnabled(true);

        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if(group.getCheckedChipIds().size() > 5)
                {
                    chipList = group.getCheckedChipIds();
                    group.clearCheck();
                    for(int i = 0; i < chipList.size() - 1;i++){
                        group.check(chipList.get(i));
                    }
                }
            }
        });
//        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
//            @Override
//            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
//                chipList.addAll(checkedIds);
//                if(group.getCheckedChipIds().size() > 5)
//                {
//                    chipList = keepFirstOccurrence(chipList);
//                    group.clearCheck();
//                    for(int i = 0; i < chipList.size() - 1;i++){
//                        group.check(chipList.get(i));
//                    }
//                }
//            }
//        });
        buttonSubmitInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chipGroup.getCheckedChipIds().size() == 0)
                {
                    Toast.makeText(initialActivity, "Bạn chưa chọn sở thích", Toast.LENGTH_LONG).show();
                    return;
                }
                for(int i = 0; i < chipGroup.getCheckedChipIds().size(); i++){
                    int checkedChipId = chipGroup.getCheckedChipIds().get(i);
                    Chip chip = view.findViewById(checkedChipId);
                    interests.add(chip.getText().toString());
                }
                SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
                User user = sharedPreferencesClient.getUserInfo("user");

                user.setInterests(interests);
                sharedPreferencesClient.putUserInfo("user", user);

                initialActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, LocationFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // name can be null
                        .commit();

                Log.e("TAG", user.toString());
            }
        });

    }

    public static List<Integer> keepFirstOccurrence(List<Integer> lst) {
        Map<Integer, Integer> counts = new HashMap<>();
        List<Integer> result = new ArrayList<>();
        for (int num : lst) {
            if (counts.containsKey(num)) {
                counts.put(num, counts.get(num) + 1);
            } else {
                counts.put(num, 1);
            }
            if (counts.get(num) == 1) {
                result.add(num);
            }
        }
        return result;
    }
}