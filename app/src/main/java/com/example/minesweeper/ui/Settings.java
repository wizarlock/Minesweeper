package com.example.minesweeper.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.minesweeper.App;
import com.example.minesweeper.R;
import com.example.minesweeper.data.GameDifficulty;
import com.example.minesweeper.databinding.FragmentSettingsBinding;


public class Settings extends Fragment {

    private FragmentSettingsBinding binding;

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
        binding.special.setChecked(false);

        switch (currentDifficulty) {
            case NEWBIE:
                binding.newbie.setChecked(true);
                visOfCustom(View.INVISIBLE);
                break;
            case AMATEUR:
                binding.amateur.setChecked(true);
                visOfCustom(View.INVISIBLE);
                break;
            case PROFESSIONAL:
                binding.professional.setChecked(true);
                visOfCustom(View.INVISIBLE);
                break;
            case SPECIAL:
                binding.special.setChecked(true);
                visOfCustom(View.VISIBLE);
                break;
        }

        binding.gameTypeRG.setOnCheckedChangeListener((group, checkId) -> {
            switch (checkId) {
                case R.id.newbie:
                    App.getInstance().setDifficulty(GameDifficulty.NEWBIE);
                    visOfCustom(View.INVISIBLE);
                    break;
                case R.id.amateur:
                    App.getInstance().setDifficulty(GameDifficulty.AMATEUR);
                    visOfCustom(View.INVISIBLE);
                    break;
                case R.id.professional:
                    App.getInstance().setDifficulty(GameDifficulty.PROFESSIONAL);
                    visOfCustom(View.INVISIBLE);
                    break;
                case R.id.special:
                    App.getInstance().setDifficulty(GameDifficulty.SPECIAL);
                    visOfCustom(View.VISIBLE);
                    break;
            }
        });

        final int[] fieldLengthSpecial = {App.getFieldLength()};
        final int[] fieldWidthSpecial = {App.getFieldWidth()};
        final int[] numOfBombsSpecial = {App.getNumOfBombs()};

        final EditText editLength = binding.fieldLength;
        editLength.setText(Integer.toString(fieldLengthSpecial[0]));
        editLength.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (editLength.getText().toString().equals("")) {
                    editLength.setText("100");
                    fieldLengthSpecial[0] = 100;
                } else if (Integer.parseInt(editLength.getText().toString()) > 100) {
                    editLength.setText("100");
                    fieldLengthSpecial[0] = 100;
                } else if (Integer.parseInt(editLength.getText().toString()) <= 1) {
                    if (fieldWidthSpecial[0] != 1) {
                        editLength.setText("1");
                        fieldLengthSpecial[0] = 1;
                    } else {
                        editLength.setText("2");
                        fieldLengthSpecial[0] = 2;
                    }
                } else fieldLengthSpecial[0] = Integer.parseInt(editLength.getText().toString());
                App.setCustomFieldLength(fieldLengthSpecial[0]);
            }
            return false;
        });

        final EditText editWidth = binding.fieldWidth;
        editWidth.setText(Integer.toString(fieldWidthSpecial[0]));
        editWidth.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (editWidth.getText().toString().equals("")) {
                    editWidth.setText("100");
                    fieldWidthSpecial[0] = 100;
                } else if (Integer.parseInt(editWidth.getText().toString()) > 100) {
                    editWidth.setText("100");
                    fieldWidthSpecial[0] = 100;
                } else if (Integer.parseInt(editWidth.getText().toString()) <= 1) {
                    if (fieldLengthSpecial[0] != 1) {
                        editWidth.setText("1");
                        fieldWidthSpecial[0] = 1;
                    } else {
                        editWidth.setText("2");
                        fieldWidthSpecial[0] = 2;
                    }
                } else fieldWidthSpecial[0] = Integer.parseInt(editWidth.getText().toString());
                App.setCustomFieldWidth(fieldWidthSpecial[0]);
            }
            return false;
        });

        final EditText editBombs = binding.numOfBombs;
        editBombs.setText(Integer.toString(numOfBombsSpecial[0]));
        editBombs.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (editBombs.getText().toString().equals("")) {
                    editBombs.setText("1");
                    numOfBombsSpecial[0] = 1;
                } else if (Integer.parseInt(editBombs.getText().toString()) > (fieldWidthSpecial[0] * fieldLengthSpecial[0] - 1)) {
                    editBombs.setText(Integer.toString(fieldWidthSpecial[0] * fieldLengthSpecial[0] - 1));
                    numOfBombsSpecial[0] = fieldWidthSpecial[0] * fieldLengthSpecial[0] - 1;

                } else if (Integer.parseInt(editBombs.getText().toString()) <= 0) {
                    editBombs.setText("1");
                    numOfBombsSpecial[0] = 1;
                } else numOfBombsSpecial[0] = Integer.parseInt(editBombs.getText().toString());
                App.setCustomNumOfBombs(numOfBombsSpecial[0]);
            }
            return false;
        });
    }

    private void visOfCustom(int visibility) {
        binding.fieldLengthText.setVisibility(visibility);
        binding.fieldLength.setVisibility(visibility);
        binding.fieldWidth.setVisibility(visibility);
        binding.fieldWidthText.setVisibility(visibility);
        binding.numOfBombs.setVisibility(visibility);
        binding.numOfBombsText.setVisibility(visibility);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding = null;
    }
}