package com.example.checkers.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.checkers.R;
import com.example.checkers.data.HintsManager;
import com.example.checkers.data.SoundsManager;
import com.example.checkers.data.OnCheckerActionListener;
import com.example.checkers.databinding.FragmentGameBinding;
import com.example.checkers.model.Cell;
import com.example.checkers.model.CellsForEating;
import com.example.checkers.model.Checker;
import com.example.checkers.model.CheckersDesk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Game extends Fragment implements OnCheckerActionListener {
    private final SoundsManager soundsManager;
    private final HintsManager hintsManager;
    private static FragmentGameBinding binding;
    public final CheckersDesk gameDesk = new CheckersDesk();
    private final List<View> viewsForClear = new ArrayList<>();
    long startTime;
    long endTime;

    public Game(SoundsManager soundsManager, HintsManager hintsManager) {
        this.soundsManager = soundsManager;
        this.hintsManager = hintsManager;
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
        animationAppearance(binding.desk, 2000, () -> {
            gameDesk.checkersDesk();
            gameDesk.setOnCheckerActionListener(Game.this);
            gameDesk.initDesk();
            for (int i = 0; i <= 7; i++)
                for (int j = 0; j <= 7; j++) {
                    ViewGroup gr = (ViewGroup) binding.desk.getChildAt(i);
                    gr.getChildAt(j).setOnClickListener(gameDesk::startGame);
                }
            startTime = System.currentTimeMillis();
        });
    }

    private void animationAppearance(View view, Integer duration, Runnable onAnimationEnd) {
        view.animate()
                .alpha(0f)
                .setDuration(0)
                .withEndAction(() -> view.animate().alpha(1f).setDuration(duration).withEndAction(onAnimationEnd));
    }

    private static View findWithTag(ViewGroup parent, Object tag) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).getTag().equals(tag))
                return parent.getChildAt(i);
        }
        return null;
    }

    public static LinearLayout getCheckerLayout(Cell cell) {
        LinearLayout row = (LinearLayout) findWithTag(binding.desk, String.valueOf(cell.getY()));
        return Objects.requireNonNull(row).findViewWithTag(String.valueOf(cell.getX()));
    }

    private void addImage(Cell cell, Integer white, Integer black) {
        ImageView checkerImage = new ImageView(requireContext());
        checkerImage.setImageResource(cell.getChecker().getColor() == Checker.Colors.WHITE ? white : black);
        getCheckerLayout(cell).addView(checkerImage);
    }

    @Override
    public void onCheckerAdded(Cell cell) {
        ImageView checkerImage = new ImageView(requireContext());
        checkerImage.setImageResource(cell.getChecker().getColor() == Checker.Colors.WHITE ? R.drawable.white : R.drawable.black);
        getCheckerLayout(cell).addView(checkerImage);
        animationAppearance(checkerImage, 100, () -> {
            soundsManager.setDeskSoundEnabled(soundsManager.isSoundsEnabled());
        });
    }

    @Override
    public void onQueenAdded(Cell cell) {
        addImage(cell, R.drawable.queen1, R.drawable.queen0);
    }

    @Override
    public void onCheckerMoved(Cell from, Cell to) {
        View checkerImage = getCheckerLayout(from).getChildAt(0);
        getCheckerLayout(from).removeView(checkerImage);
        getCheckerLayout(to).addView(checkerImage);
        soundsManager.setDeskSoundEnabled(soundsManager.isSoundsEnabled());
    }

    @Override
    public void onCheckerRemoved(Cell cell) {
        View checkerImage = getCheckerLayout(cell).getChildAt(0);
        getCheckerLayout(cell).removeView(checkerImage);
    }

    @Override
    public void colorForPick(View view) {
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pick));
        viewsForClear.add(view);
    }

    @Override
    public void colorForMoves(List<Cell> cells) {
        if (hintsManager.isHintsEnabled())
            for (Cell cell : cells) {
                getCheckerLayout(cell).setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.variants));
                viewsForClear.add(getCheckerLayout(cell));
            }
    }

    @Override
    public void colorForEat(List<CellsForEating> cells, Cell requiredCell) {
        if (hintsManager.isHintsEnabled())
            for (CellsForEating list : cells) {
                if (list.getRequiredCell().equals(requiredCell)) {
                    getCheckerLayout(list.getMoving()).setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.variants));
                    viewsForClear.add(getCheckerLayout(list.getMoving()));
                }
            }
    }

    @Override
    public void boardClear() {
        for (View view : viewsForClear)
            view.setBackgroundResource(R.drawable.brownwood);
        viewsForClear.clear();
    }

    private long getGameTime () {
        endTime = System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
    }

    @Override
    public void finish(boolean blackMoves) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        if (blackMoves) builder.setMessage("Whites win! Time spent in the game: " + getGameTime() + " seconds");
        else builder.setMessage("Blacks win! Time spent in the game: " + getGameTime() + " seconds");
        builder.setCancelable(false)
                .setPositiveButton("Restart game", (dialogInterface, i) -> {
                    FragmentTransaction ft = requireFragmentManager().beginTransaction();
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                    ft.replace(R.id.frPlace, new Game(soundsManager, hintsManager));
                    ft.addToBackStack(null);
                    ft.commit();
                })
                .setNegativeButton("Menu", (dialogInterface, i) -> {
                    requireFragmentManager().popBackStackImmediate();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void scoreChange(Integer newScoreForBlack, Integer newScoreForWhite, boolean blackMoves) {
        if (blackMoves) binding.scoreForBlack.setText("Score: " + newScoreForBlack);
        else binding.scoreForWhite.setText("Score: " + newScoreForWhite);
    }
}

