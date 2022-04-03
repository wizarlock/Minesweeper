package com.example.minesweeper.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minesweeper.App;
import com.example.minesweeper.R;
import com.example.minesweeper.databinding.FragmentMenuBinding;

import java.util.Objects;

public class Menu extends Fragment {

    private FragmentMenuBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.frPlace, Objects.requireNonNull(fragment));
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rulesButton.setOnClickListener(v -> replaceFragment(new Rules()));
        binding.settingsButton.setOnClickListener(v -> replaceFragment(new Settings()));
        binding.settingsButton.setOnClickListener(v -> replaceFragment(new Settings()));
        binding.startButton.setOnClickListener(v -> replaceFragment(new Game(App.getFieldLength(), App.getFieldWidth(), App.getNumOfBombs())));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding = null;
    }
}