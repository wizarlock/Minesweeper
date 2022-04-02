package com.example.minesweeper.ui;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minesweeper.App;
import com.example.minesweeper.R;
import com.example.minesweeper.data.GameDifficulty;
import com.example.minesweeper.databinding.FragmentSettingsBinding;


public class Settings extends Fragment {

    private FragmentSettingsBinding binding;

    public Settings() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GameDifficulty currentDifficulty = App.getInstance().getDifficulty();

        binding.newbie.setChecked(false);
        binding.amateur.setChecked(false);
        binding.professional.setChecked(false);

        switch (currentDifficulty) {
            case NEWBIE:
                binding.newbie.setChecked(true);
                break;
            case AMATEUR:
                binding.amateur.setChecked(true);
                break;
            case PROFESSIONAL:
                binding.professional.setChecked(true);
                break;
        }

        binding.gameTypeRG.setOnCheckedChangeListener((group, checkId) -> {
            switch (checkId) {
                case R.id.newbie:
                    App.getInstance().setDifficulty(GameDifficulty.NEWBIE);
                    break;
                case R.id.amateur:
                    App.getInstance().setDifficulty(GameDifficulty.AMATEUR);
                    break;
                case R.id.professional:
                    App.getInstance().setDifficulty(GameDifficulty.PROFESSIONAL);
                    break;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding = null;
    }
}