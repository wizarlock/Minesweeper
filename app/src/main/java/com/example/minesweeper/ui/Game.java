package com.example.minesweeper.ui;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.minesweeper.R;
import com.example.minesweeper.data.ActionListener;
import com.example.minesweeper.databinding.FragmentGameBinding;
import com.example.minesweeper.model.Board;
import com.example.minesweeper.model.Cell;



public class Game extends Fragment implements ActionListener {

    private static FragmentGameBinding binding;
    private final int fieldLength;
    private final int fieldHeight;
    private final int numOfMines;

    public Game(int fieldLength, int fieldHeight, int numOfMines) {
        this.fieldLength = fieldLength;
        this.fieldHeight = fieldHeight;
        this.numOfMines = numOfMines;
    }

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
        final Board gameBoard = new Board(fieldLength, fieldHeight, numOfMines);
        gameBoard.board();
        gameBoard.setActionListener(Game.this);
        for (int i = 0; i < fieldHeight; i++)
            for (int j = 0; j < fieldLength; j++) {
                ViewGroup gr = (ViewGroup) binding.desk.getChildAt(i);
                gr.getChildAt(j).setOnClickListener(gameBoard::startGame);
                gr.getChildAt(j).setOnLongClickListener(gameBoard::placeFlag);
            }
        binding.possibleFlags.setText(Integer.toString(fieldHeight * fieldLength));
    }

    private static View findWithTag(ViewGroup parent, Object tag) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).getTag().equals(tag))
                return parent.getChildAt(i);
        }
        return null;
    }

    private static FrameLayout getCellLayout(Cell cell) {
        LinearLayout row = (LinearLayout) findWithTag(binding.desk, cell.getY());
        return (FrameLayout) findWithTag(row, cell.getX());
    }

    private void drawBoard() {
        LinearLayout desk = binding.desk;
        for (int i = 0; i < fieldHeight; i++) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setTag(i);
            for (int j = 0; j < fieldLength; j++) {
                FrameLayout layout = new FrameLayout(getContext());
                layout.setLayoutParams(new FrameLayout.LayoutParams(100, 100));
                layout.setBackgroundResource(R.drawable.cell);
                layout.setTag(j);
                linearLayout.addView(layout);
            }
            desk.addView(linearLayout);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        binding = null;
    }

    @Override
    public void mineAdded(Cell cell) {
        FrameLayout layout = getCellLayout(cell);
        ImageView mineImage = new ImageView(requireContext());
        mineImage.setImageResource(R.drawable.mine);
        layout.addView(mineImage);
    }

    @Override
    public void numOfNearbyMinesAdded(Cell cell, Integer digit) {
        FrameLayout layout = getCellLayout(cell);
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
        int newText = Integer.parseInt(binding.possibleFlags.getText().toString()) - 1;
        binding.possibleFlags.setText(Integer.toString(newText));
    }

    @Override
    public void unMarkCell(Cell cell) {
        getCellLayout(cell).setBackgroundResource(R.drawable.cell);
        int newText = Integer.parseInt(binding.possibleFlags.getText().toString()) + 1;
        binding.possibleFlags.setText(Integer.toString(newText));
    }

    private void build (AlertDialog.Builder builder) {
        builder.setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    FragmentTransaction ft = requireFragmentManager().beginTransaction();
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                    ft.replace(R.id.frPlace, new Game(fieldLength, fieldHeight, numOfMines));
                    ft.addToBackStack(null);
                    ft.commit();
                })
                .setNegativeButton("No", (dialogInterface, i) -> requireFragmentManager().popBackStackImmediate());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void defeat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("You lost the game. Shall we play again?");
        build(builder);
    }

    @Override
    public void winResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("You won the game. Shall we play again?");
        build(builder);
    }
}

