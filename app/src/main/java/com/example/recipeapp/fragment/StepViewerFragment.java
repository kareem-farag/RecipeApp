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
    private PlayerView playerView;
    private TextView stepDetailsTv;
    private Long playerPosition = null;
    private Boolean startAutoPlay = null;

    public StepViewerFragment() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("step", step);
        outState.putLong("playerPosition", playerPosition);
        outState.putBoolean("startAutoPlay", startAutoPlay);


        super.onSaveInstanceState(outState);

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
        playerView = rootView.findViewById(R.id.player);

        if (simpleExoPlayer == null) {
            if (savedInstanceState != null) {


                playerPosition = savedInstanceState.getLong("playerPosition");
                startAutoPlay = savedInstanceState.getBoolean("startAutoPlay");
            }
            initiatePlayer();


        }
        return rootView;
    }

    private void initiatePlayer() {
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer = new SimpleExoPlayer.Builder(context).build();
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(step.getVideoURL()));
        if (playerPosition != null) {
            simpleExoPlayer.seekTo(playerPosition);
        }
        if (startAutoPlay != null) {
            simpleExoPlayer.setPlayWhenReady(startAutoPlay);
        }


        simpleExoPlayer.setMediaItem(mediaItem);
        simpleExoPlayer.setPlayWhenReady(true);


        simpleExoPlayer.prepare();
        simpleExoPlayer.play();
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
            initiatePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23)) {

            initiatePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        playerPosition = simpleExoPlayer.getCurrentPosition();
        startAutoPlay = simpleExoPlayer.getPlayWhenReady();

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        playerPosition = simpleExoPlayer.getCurrentPosition();
        startAutoPlay = simpleExoPlayer.getPlayWhenReady();
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
