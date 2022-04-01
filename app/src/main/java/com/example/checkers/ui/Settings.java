package com.example.checkers.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.checkers.data.HintsManager;
import com.example.checkers.data.SoundsManager;
import com.example.checkers.databinding.FragmentSettingsBinding;
import com.example.checkers.data.MusicManager;


public class Settings extends Fragment {

    private FragmentSettingsBinding binding;
    public static final String APP_PREFERENCES = "mSettings";
    public static final String MUSIC_SETTINGS = "music";
    public static final boolean DEFAULT_MUSIC_ENABLED_KEY = true;
    public static final String SOUNDS_SETTINGS = "settings";
    public static final boolean DEFAULT_SOUNDS_ENABLED_KEY = true;
    public static final String HINTS_SETTINGS = "hints";
    public static final boolean DEFAULT_HINTS_ENABLED_KEY = true;

    private final MusicManager musicManager;
    private final SoundsManager soundsManager;
    private final HintsManager hintsManager;

    public Settings(MusicManager musicManager, SoundsManager soundsManager, HintsManager hintsManager) {
        this.musicManager = musicManager;
        this.soundsManager = soundsManager;
        this.hintsManager = hintsManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.music.setChecked(musicManager.isMusicEnabled());
        binding.music.setOnCheckedChangeListener((switchView, checked) -> {
            soundsManager.setButtonSoundEnabled(soundsManager.isSoundsEnabled());
            musicManager.setMusicEnabled(checked);
        });
        binding.sounds.setChecked(soundsManager.isSoundsEnabled());
        binding.sounds.setOnCheckedChangeListener((switchView, checked) -> soundsManager.setButtonSoundEnabled(checked));
        binding.hints.setChecked(hintsManager.isHintsEnabled());
        binding.hints.setOnCheckedChangeListener((switchView, checked) -> {
            soundsManager.setButtonSoundEnabled(soundsManager.isSoundsEnabled());
            hintsManager.setHintsEnabled(checked);
        });
        binding.back.setOnClickListener(v -> {
            soundsManager.setButtonSoundEnabled(soundsManager.isSoundsEnabled());
            requireFragmentManager().popBackStackImmediate();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding = null;
    }
}