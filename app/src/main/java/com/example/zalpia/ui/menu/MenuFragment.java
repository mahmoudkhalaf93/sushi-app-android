package com.example.zalpia.ui.menu;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MenuFragment extends Fragment {

    private MenuViewModel menuViewModel;

FragmentMenuBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_menu, container, false);
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


menuViewModel.getCatList().observe(getViewLifecycleOwner(), new Observer<ArrayList<CatModel>>() {
    @Override
    public void onChanged(ArrayList<CatModel> cats) {

        final int[] currentSwip = {0};
        binding.swipview.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {

            }
            public void onSwipeRight() {

                if(currentSwip[0] >0){
                    currentSwip[0]--;

                    Animation anim2= AnimationUtils.loadAnimation(getActivity(),R.anim.imager_anim_fromshow_to_hidetorightside);
                    binding.catImage.startAnimation(anim2);
                    Completable
                            .timer(200, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                }

                                @Override
                                public void onComplete() {
                                    binding.catImage.startAnimation(anim2);
                                    UpdateRecycleView(currentSwip[0]);
                                    Animation anim3= AnimationUtils.loadAnimation(getActivity(),R.anim.imager_anim_fromhideleft_to_show);
                                    binding.catImage.startAnimation(anim3);
                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                }
                            });


                }

            }
            public void onSwipeLeft() {
                if(currentSwip[0] < cats.size()-1)
                {
                    currentSwip[0]++;

                    Animation anim1= AnimationUtils.loadAnimation(getActivity(),R.anim.image_anim_fromshowing_to_hidetoleftside);
                    binding.catImage.startAnimation(anim1);
                    Completable
                            .timer(200, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                }
                                @Override
                                public void onComplete() {
                                    UpdateRecycleView(currentSwip[0]);
                                    Animation anim= AnimationUtils.loadAnimation(getActivity(),R.anim.image_anim_fromhideright_to_show);
                                    binding.catImage.startAnimation(anim);
                                }
                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                }
                            });

                }
            }
            public void onSwipeBottom() {
                //     Toast.makeText(LoginActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

    }
});
        UpdateRecycleView(0);





    }
    private void UpdateRecycleView(int catNumber){
        menuViewModel.getCatList().observe(getViewLifecycleOwner(), new Observer<ArrayList<CatModel>>() {
            @Override
            public void onChanged(ArrayList<CatModel> cats) {

                ProductAdapter productAdapter=new ProductAdapter(getActivity(),cats.get(catNumber).getProductsList());
                binding.catRv.setAdapter(productAdapter);


                binding.catName.setText(cats.get(catNumber).getName());
                binding.catImage.setImageResource(cats.get(catNumber).getImage());


                binding.SliderDots.removeAllViews();
                for (int i=0;i<cats.size();i++){
                    if(i!=catNumber)
                    { int sizes = getResources().getDimensionPixelSize(R.dimen._8sdp);
                        ImageView dot=new ImageView(getActivity());
                        dot.setImageResource(R.drawable.nonactive_dot);
                        binding.SliderDots.addView(dot);
                    }
                    else {
                        int sizes = getResources().getDimensionPixelSize(R.dimen._8sdp);
                        ImageView dot=new ImageView(getActivity());
                        dot.setImageResource(R.drawable.active_dot);
                        binding.SliderDots.addView(dot);
                    }

                }
            }
        });


    }

}