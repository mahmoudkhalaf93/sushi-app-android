package com.example.zalpia.ui.menu;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.zalpia.OnSwipeTouchListener;
import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentMenuBinding;
import com.example.zalpia.room.CategoryModel;

import java.util.List;
import java.util.Locale;


public class MenuFragment extends Fragment {
//boolean isSwipRight=true ;
    public  static int currentSwip = 0;
    FragmentMenuBinding binding;
    private MenuViewModel menuViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuViewModel.getCatList().observe(getViewLifecycleOwner(), (Observer<List<CategoryModel>>) cats ->
                binding.swipview.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
               // isSwipRight=true;
                if ( currentSwip > 0) {

                    Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_anim_frombig_small);
                    binding.previousCatImage.setImageURI(Uri.parse(cats.get( currentSwip).getImage()));
                    binding.previousCatImage.startAnimation(anim2);
                    currentSwip--;
                    UpdateRecycleView( currentSwip);
                    Animation anim3 = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_anim_fromsmall_to_big);
                    binding.nextCatImage.startAnimation(anim3);

                }

            }

            public void onSwipeLeft() {
                //isSwipRight=false;
                if ( currentSwip< cats.size() - 1) {

                    Animation anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_anim_frombig_small);
                    binding.previousCatImage.setImageURI(Uri.parse(cats.get( currentSwip).getImage()));
                    binding.previousCatImage.startAnimation(anim1);
                    currentSwip++;
                    UpdateRecycleView( currentSwip);
                    Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_anim_fromsmall_to_big);
                    binding.nextCatImage.startAnimation(anim);

                }
            }

            public void onSwipeBottom() {
                //     Toast.makeText(LoginActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        }));
        menuViewModel.setCatArray();
        UpdateRecycleView( currentSwip);


    }

    private void UpdateRecycleView(int catNumber) {
        menuViewModel.getCatList().observe(getViewLifecycleOwner(), (Observer<List<CategoryModel>>) categoryModels -> {

            menuViewModel.getListOfItemsInCatLive().observe(getViewLifecycleOwner(), itemModels -> {
                ProductAdapter productAdapter = new ProductAdapter(getActivity(), itemModels);
                binding.catRv.setAdapter(productAdapter);
            });

            menuViewModel.setListOfItemsInCat(categoryModels.get(catNumber).getFirebaseId());

                    if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
                    {
                        binding.catName.setText(categoryModels.get(catNumber).getNameAr());
                    }
                    else {
                        binding.catName.setText(categoryModels.get(catNumber).getName());
                    }


            binding.nextCatImage.setImageURI(Uri.parse(categoryModels.get(catNumber).getImage()));
//

            binding.SliderDots.removeAllViews();
            for (int i = 0; i < categoryModels.size(); i++) {
           //     int sizes = getResources().getDimensionPixelSize(R.dimen._8sdp);
                ImageView dot = new ImageView(getActivity());
                if (i != catNumber) {
                    dot.setImageResource(R.drawable.nonactive_dot);
                } else {
                    dot.setImageResource(R.drawable.active_dot);
                }
                binding.SliderDots.addView(dot);

            }
        }
        );

    }

}