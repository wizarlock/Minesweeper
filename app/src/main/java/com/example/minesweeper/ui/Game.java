package com.example.minesweeper.ui;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.minesweeper.model.Solver;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class Game extends Fragment implements ActionListener {

    private static FragmentGameBinding binding;
    private final int fieldLength;
    private final int fieldHeight;
    private final int numOfMines;
    private final boolean solver;
    private boolean gameIsEnd = false;
    private long startTime;
    private Timer mTimer;

    public Game(int fieldLength, int fieldHeight, int numOfMines, boolean solver) {
        this.fieldLength = fieldLength;
        this.fieldHeight = fieldHeight;
        this.numOfMines = numOfMines;
        this.solver = solver;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void init() {
        gameIsEnd = false;
        startTime = System.currentTimeMillis();
        mTimer = new Timer();
        MyTimerTask mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 0, 1000);

        drawBoard();
        binding.possibleFlags.setText(Integer.toString(fieldHeight * fieldLength));
        final Board gameBoard = new Board(fieldLength, fieldHeight, numOfMines);
        gameBoard.board();
        gameBoard.setActionListener(Game.this);

        if (!solver)
            for (int i = 0; i < fieldHeight; i++)
                for (int j = 0; j < fieldLength; j++) {
                    ViewGroup gr = (ViewGroup) binding.desk.getChildAt(i);
                    gr.getChildAt(j).setOnClickListener(gameBoard::startGame);
                    gr.getChildAt(j).setOnLongClickListener(gameBoard::placeFlag);
                }
        else {
            Solver gameSolver = new Solver(fieldLength, fieldHeight);

            new Thread(() -> {
                Map.Entry<Cell, Integer> solution = gameSolver.solve(Board.arrayOfAllCells);
                while (solution != null) {
                    if (solution.getValue() == 0)
                        gameBoard.startGame(getCellLayout(solution.getKey()));
                    else gameBoard.placeFlag(getCellLayout(solution.getKey()));
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (gameIsEnd) break;
                    solution = gameSolver.solve(Board.arrayOfAllCells);
                }
            }).start();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    public static View findWithTag(ViewGroup parent, Object tag) {
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
        binding.desk.removeAllViewsInLayout();
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
        mTimer.cancel();
        mTimer.purge();
        binding = null;
    }

    @Override
    public void mineAdded(Cell cell) {
        requireActivity().runOnUiThread(() -> {

            FrameLayout layout = getCellLayout(cell);
            ImageView mineImage = new ImageView(requireContext());
            mineImage.setImageResource(R.drawable.mine);
            layout.addView(mineImage);
        });
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
        requireActivity().runOnUiThread(() -> layout.addView(digitView));
    }

    @Override
    public void openCell(Cell cell) {
        requireActivity().runOnUiThread(() ->
                getCellLayout(cell).setBackgroundResource(R.drawable.opencell)
        );
    }

    @Override
    public void markCell(Cell cell) {
        requireActivity().runOnUiThread(() -> {
            getCellLayout(cell).setBackgroundResource(R.drawable.flag);
            int newText = Integer.parseInt(binding.possibleFlags.getText().toString()) - 1;
            binding.possibleFlags.setText(Integer.toString(newText));
        });
    }

    @Override
    public void unMarkCell(Cell cell) {
        getCellLayout(cell).setBackgroundResource(R.drawable.cell);
        int newText = Integer.parseInt(binding.possibleFlags.getText().toString()) + 1;
        binding.possibleFlags.setText(Integer.toString(newText));
    }

    private void build(AlertDialog.Builder builder) {
        gameIsEnd = true;
        builder.setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    init();
                })
                .setNegativeButton("No", (dialogInterface, i) -> requireFragmentManager().popBackStackImmediate());
        requireActivity().runOnUiThread(() -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public void defeat() {
        mTimer.cancel();
        mTimer.purge();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("You lost the game. Shall we play again? Time in game: " + getGameTime() + " seconds");
        build(builder);
    }

    @Override
    public void winResult() {
        mTimer.cancel();
        mTimer.purge();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("You won the game. Shall we play again? Time in game: " + getGameTime() + " seconds");
        build(builder);
    }

    private long getGameTime() {
        long endTime = System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            long time = getGameTime();
            if (binding != null)
                binding.timer.setText(Long.toString(time));
        }
    }
}

