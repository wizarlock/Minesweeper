package com.example.minesweeper.ui;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.minesweeper.R;
import com.example.minesweeper.data.ActionListener;
import com.example.minesweeper.databinding.FragmentGameBinding;
import com.example.minesweeper.model.Board;
import com.example.minesweeper.model.Cell;

import java.util.Objects;


public class Game extends Fragment implements ActionListener {

    private static FragmentGameBinding binding;
    private int fieldLength;
    private int fieldWidth;
    private int numOfMines;

    public Game(int fieldLength, int fieldWidth, int numOfMines) {
        this.fieldLength = fieldLength;
        this.fieldWidth = fieldWidth;
        this.numOfMines = numOfMines;
    }

    public final Board gameBoard = new Board(fieldLength, fieldWidth, numOfMines);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawBoard();
        gameBoard.board();
        gameBoard.setActionListener(Game.this);
    }

    private static View findWithTag(ViewGroup parent, Object tag) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).getTag().equals(tag))
                return parent.getChildAt(i);
        }
        return null;
    }

    private static LinearLayout getCellLayout(Cell cell) {
        LinearLayout row = (LinearLayout) findWithTag(binding.desk, String.valueOf(cell.getY()));
        return Objects.requireNonNull(row).findViewWithTag(String.valueOf(cell.getX()));
    }

    private void drawBoard() {
        LinearLayout desk = binding.desk;
        for (int i = 0; i < fieldWidth; i++) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setTag(i);
            desk.addView(linearLayout);
            for (int j = 0; j < fieldLength; j++) {
                LinearLayout layout = new LinearLayout(getContext());
                layout.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                layout.setBackgroundResource(R.drawable.cell);
                layout.setTag(j);
                linearLayout.addView(layout);
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        binding = null;
    }

    @Override
    public void mineAdded(Cell cell) {
        LinearLayout layout = getCellLayout(cell);
        ImageView mineImage = new ImageView(requireContext());
        mineImage.setImageResource(R.drawable.mine);
        layout.addView(mineImage);
    }

    @Override
    public void numOfNearbyMinesAdded(Cell cell, Integer digit) {
        LinearLayout layout = getCellLayout(cell);
        TextView digitView = new TextView(requireContext());
        digitView.setText(Integer.toString(digit));
        digitView.setWidth(100);
        digitView.setHeight(100);
        digitView.setGravity(Gravity.CENTER);
        digitView.setTextSize(23);
        digitView.setTypeface(null, Typeface.BOLD);
        switch (digit) {
            case 1:
                digitView.setTextColor(getResources().getColor(R.color.one));
                break;
            case 2:
                digitView.setTextColor(getResources().getColor(R.color.two));
                break;
            case 3:
                digitView.setTextColor(getResources().getColor(R.color.three));
                break;
            case 4:
                digitView.setTextColor(getResources().getColor(R.color.four));
                break;
            case 5:
                digitView.setTextColor(getResources().getColor(R.color.five));
                break;
            case 6:
                digitView.setTextColor(getResources().getColor(R.color.six));
                break;
            case 7:
                digitView.setTextColor(getResources().getColor(R.color.seven));
                break;
            case 8:
                digitView.setTextColor(getResources().getColor(R.color.eight));
                break;
        }
        layout.addView(digitView);
    }

    @Override
    public void openCell(Cell cell) {
        getCellLayout(cell).setBackgroundResource(R.drawable.opencell);
    }

    @Override
    public void markCell(Cell cell) {
        getCellLayout(cell).setBackgroundResource(R.drawable.flag);
    }

    @Override
    public void unMarkCell(Cell cell) {
        getCellLayout(cell).setBackgroundResource(R.drawable.cell);
    }
}

