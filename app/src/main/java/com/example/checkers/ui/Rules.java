package com.example.checkers.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.checkers.data.SoundsManager;
import com.example.checkers.databinding.FragmentRulesBinding;


public class Rules extends Fragment {

    private FragmentRulesBinding binding;
    private final SoundsManager soundsManager;

    public Rules(SoundsManager buttonSoundManager) {
        this.soundsManager = buttonSoundManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRulesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.back.setOnClickListener(v -> {
            soundsManager.setButtonSoundEnabled(soundsManager.isSoundsEnabled());
            requireFragmentManager().popBackStackImmediate();
        });
        binding.textView4.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding = null;
    }
}