package com.example.recipeapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipeapp.R;
import com.example.recipeapp.utils.Step;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class StepViewerFragment extends Fragment {
    private Step step;
    private Context context;
    private List<Step> stepList;
    SimpleExoPlayer simpleExoPlayer;
    private TextView stepDetailsTv;

    public StepViewerFragment() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("step", step);
        outState.putLong("playerPosition", simpleExoPlayer.getCurrentPosition());
        outState.putBoolean("startAutoPlay", simpleExoPlayer.getPlayWhenReady());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_step_viewer, container, false);

        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable("step");
        }


        stepDetailsTv = rootView.findViewById(R.id.step_viewer_details_tv);
        stepDetailsTv.setText(step.getDescription());

        if (simpleExoPlayer == null) {

            PlayerView playerView = rootView.findViewById(R.id.player);

            simpleExoPlayer = new SimpleExoPlayer.Builder(context).build();
            playerView.setPlayer(simpleExoPlayer);

            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(step.getVideoURL()));
            simpleExoPlayer.setMediaItem(mediaItem);
            simpleExoPlayer.setPlayWhenReady(true);
            if (savedInstanceState != null) {
                simpleExoPlayer.seekTo(savedInstanceState.getLong("playerPosition"));
                simpleExoPlayer.setPlayWhenReady(savedInstanceState.getBoolean("startAutoPlay"));
            }

            simpleExoPlayer.prepare();
            simpleExoPlayer.play();

        }
        return rootView;
    }


    private void releasePlayer() {
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        simpleExoPlayer = null;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            // initialize player
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23)) {

            // initialize player
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public void setStep(Step step) {
        this.step = step;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }
}
