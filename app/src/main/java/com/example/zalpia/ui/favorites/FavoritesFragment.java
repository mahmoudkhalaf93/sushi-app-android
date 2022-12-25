package com.example.zalpia.ui.favorites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentFavoritesBinding;
import com.example.zalpia.room.ItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FavoritesFragment extends Fragment {
FavoritesViewModel viewModel;
FragmentFavoritesBinding binding;
    FavoritesAdapter favoritesAdapter;
    private static final String TAG = "ItemProfileFragment1";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
List<ItemModel> arrayList =new ArrayList<>();
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorites,container,false);




        viewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);

        viewModel.getFavoritesList().observe(getViewLifecycleOwner(), favorites -> {
            arrayList =favorites;
             favoritesAdapter = new FavoritesAdapter(arrayList,getActivity());
            binding.favoriteRv.setAdapter(favoritesAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView( binding.favoriteRv);
        });
        viewModel.setFavoritesValue();
        return binding.getRoot();
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder,
                              @NotNull RecyclerView.ViewHolder target) {
           // Toast.makeText(requireActivity(), "on Move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getBindingAdapterPosition();
            Toast.makeText(requireActivity(), R.string.Removed, Toast.LENGTH_SHORT).show();
            //Remove swiped item from list and notify the RecyclerView
            firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                    .collection("favorites").document(arrayList.get(position).getFirebaseId()).delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            arrayList.remove(position);
                            favoritesAdapter.notifyDataSetChanged();
                        } else {
                            Log.i(TAG, "onComplete: ", task.getException());
                        }
                    });

        }
    };



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addMore.setOnClickListener(view1 -> {
//
            NavController navController= Navigation.findNavController(view);
            navController.navigate(R.id.action_nav_favorites_to_nav_menu);

        });

    }




}