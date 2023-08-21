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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultyUpdateFragment extends Fragment {
    ChipGroup chipGroup;
    Chip chip;
    TextView buttonSubmitFaculty, buttonCancelFaculty;

    MainActivity mainActivity;
    UserApiService userApiService;
    public FacultyUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faculty_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        userApiService = RetrofitClient.getInstance().create(UserApiService.class);

        chipGroup = view.findViewById(R.id.chipGroupFaculty);
        buttonSubmitFaculty = view.findViewById(R.id.buttonSubmitFaculty);
        buttonCancelFaculty = view.findViewById(R.id.buttonCancelFaculty);

        User user = MySharedPreferences.getUserInfo(getActivity(),"user");

        for(int i = 0; i < chipGroup.getChildCount(); i++){
            Chip checkedChip =(Chip) chipGroup.getChildAt(i);
            if(user.getFaculty().equals(checkedChip.getText().toString())){
                checkedChip.setChecked(true);
                break;
            }
        }
        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                buttonSubmitFaculty.setVisibility(View.VISIBLE);
            }
        });
        buttonSubmitFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedChipId = chipGroup.getCheckedChipId();
                chip = view.findViewById(checkedChipId);

                if(chip == null)
                {
                    Toast.makeText(mainActivity, "Bạn chưa chọn khoa", Toast.LENGTH_LONG).show();
                    return;
                }

                user.setFaculty(chip.getText().toString());
                MySharedPreferences.putUserInfo(getActivity(), "user", user);

                userApiService.updateInfo(user.get_id(), user).enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(mainActivity,"Cập nhật thành công", Toast.LENGTH_SHORT).show();

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
        buttonCancelFaculty.setOnClickListener(new View.OnClickListener() {
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