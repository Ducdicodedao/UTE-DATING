package com.client.utedating.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.client.utedating.R;
import com.client.utedating.activities.MainActivity;
import com.client.utedating.adapters.ViewPagerAdapter;
import com.client.utedating.models.User;
import com.client.utedating.models.UserModel;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.utils.MySharedPreferences;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterestUpdateFragment extends Fragment {
    ChipGroup chipGroup;
    TextView buttonSubmitInterest, buttonCancelInterest;
    List<Integer> chipList = new ArrayList<>();
//    List<Chip> checkedChips = new ArrayList<>();
    List<String> interests = new ArrayList<>();

    MainActivity mainActivity;

    UserApiService userApiService;
    public InterestUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interest_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        userApiService = RetrofitClient.getInstance().create(UserApiService.class);

        User user = MySharedPreferences.getUserInfo(getActivity(),"user");

        chipGroup = view.findViewById(R.id.chipGroupInterest);
        buttonSubmitInterest = view.findViewById(R.id.buttonSubmitInterest);
        buttonCancelInterest = view.findViewById(R.id.buttonCancelInterest);

        chipGroup.setScrollbarFadingEnabled(true);

        for(int i = 0; i < chipGroup.getChildCount(); i++){
            Chip chip =(Chip) chipGroup.getChildAt(i);
            for(int j = 0; j < user.getInterests().size(); j++){
                if(chip.getText().toString().equals(user.getInterests().get(j))){
                    chip.setChecked(true);
                }
            }
        }

        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                buttonSubmitInterest.setVisibility(View.VISIBLE);
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

        buttonSubmitInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chipGroup.getCheckedChipIds().size() == 0)
                {
                    Toast.makeText(mainActivity, "Bạn chưa chọn sở thích", Toast.LENGTH_LONG).show();
                    return;
                }
                for(int i = 0; i < chipGroup.getCheckedChipIds().size(); i++){
                    int checkedChipId = chipGroup.getCheckedChipIds().get(i);
                    Chip chip = view.findViewById(checkedChipId);
                    interests.add(chip.getText().toString());
                }

                user.setInterests(interests);
                MySharedPreferences.putUserInfo(getActivity(),"user", user);

                userApiService.updateInfo(user.get_id(), user).enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(mainActivity,"Cập nhật thành công", Toast.LENGTH_SHORT).show();

//                            mainActivity.recreate();

                            ViewPager viewPager =  mainActivity.findViewById(R.id.view_pager);
                            ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
                            Fragment accountFragment = viewPagerAdapter.getItem(3);
                            accountFragment.onResume();

                            mainActivity.findViewById(R.id.frame_Layout).setVisibility(View.GONE);
                            mainActivity.findViewById(R.id.view_pager).setVisibility(View.VISIBLE);

                            Log.e("TAG", user.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                    }
                });

                Log.e("TAG", user.toString());
            }
        });
        buttonCancelInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager viewPager =  mainActivity.findViewById(R.id.view_pager);
                ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
                Fragment accountFragment = viewPagerAdapter.getItem(3);
                accountFragment.onResume();

                mainActivity.findViewById(R.id.frame_Layout).setVisibility(View.GONE);
                mainActivity.findViewById(R.id.view_pager).setVisibility(View.VISIBLE);
            }
        });
    }
}