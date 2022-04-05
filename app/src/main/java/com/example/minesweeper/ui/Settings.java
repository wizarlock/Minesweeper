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
        final int[] numOfMinesSpecial = {App.getNumOfMines()};

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

        final EditText editMines = binding.numOfMines;
        editMines.setText(Integer.toString(numOfMinesSpecial[0]));
        editMines.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (editMines.getText().toString().equals("")) {
                    editMines.setText("1");
                    numOfMinesSpecial[0] = 1;
                } else if (Integer.parseInt(editMines.getText().toString()) > (fieldWidthSpecial[0] * fieldLengthSpecial[0] - 1)) {
                    editMines.setText(Integer.toString(fieldWidthSpecial[0] * fieldLengthSpecial[0] - 1));
                    numOfMinesSpecial[0] = fieldWidthSpecial[0] * fieldLengthSpecial[0] - 1;

                } else if (Integer.parseInt(editMines.getText().toString()) <= 0) {
                    editMines.setText("1");
                    numOfMinesSpecial[0] = 1;
                } else numOfMinesSpecial[0] = Integer.parseInt(editMines.getText().toString());
                App.setCustomNumOfMines(numOfMinesSpecial[0]);
            }
            return false;
        });
    }

    private void visOfCustom(int visibility) {
        binding.fieldLengthText.setVisibility(visibility);
        binding.fieldLength.setVisibility(visibility);
        binding.fieldWidth.setVisibility(visibility);
        binding.fieldWidthText.setVisibility(visibility);
        binding.numOfMines.setVisibility(visibility);
        binding.numOfMinesText.setVisibility(visibility);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding = null;
    }
}