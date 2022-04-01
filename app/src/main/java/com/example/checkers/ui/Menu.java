package com.example.checkers.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.checkers.R;
import com.example.checkers.data.HintsManager;
import com.example.checkers.data.SoundsManager;
import com.example.checkers.data.MusicManager;
import com.example.checkers.databinding.FragmentMenuBinding;

import java.util.Objects;

public class Menu extends Fragment {

    private FragmentMenuBinding binding;

    private final MusicManager musicManager;
    private final SoundsManager soundsManager;
    private final HintsManager hintsManager;

    public Menu(MusicManager musicManager, SoundsManager soundsManager, HintsManager hintsManager) {
        this.musicManager = musicManager;
        this.soundsManager = soundsManager;
        this.hintsManager = hintsManager;
    }

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
        binding.rulesButton.setOnClickListener(v -> {
            soundsManager.setButtonSoundEnabled(soundsManager.isSoundsEnabled());
            replaceFragment(new Rules(soundsManager));
        });
        binding.startButton.setOnClickListener(v -> {
            soundsManager.setButtonSoundEnabled(soundsManager.isSoundsEnabled());
            replaceFragment(new Game(soundsManager, hintsManager));
        });
        binding.settingsButton.setOnClickListener(v -> {
            soundsManager.setButtonSoundEnabled(soundsManager.isSoundsEnabled());
            replaceFragment(new Settings(musicManager, soundsManager, hintsManager));
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding = null;
    }
}