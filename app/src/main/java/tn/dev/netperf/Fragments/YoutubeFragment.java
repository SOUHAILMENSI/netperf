package tn.dev.netperf.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;

import tn.dev.netperf.R;
import tn.dev.netperf.Utils.Config;

public class YoutubeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = YoutubeFragment.class.getSimpleName();
    private YouTubePlayer youTubePlayer;

    MaterialButton startButton;
    TextInputEditText editText;
    YouTubePlayerSupportFragmentX youTubePlayerFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.fragment_youtube, container, false);

        startButton = myView.findViewById(R.id._start_);
        startButton.setOnClickListener(this);
        editText = myView.findViewById(R.id.input_text);
        initializeYoutubePlayer();
        return myView;
    }

    private void initializeYoutubePlayer() {

        youTubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_player_, youTubePlayerFragment, TAG);
        transaction.commit();

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(Config.getYoutubeApiKey(), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                    youTubePlayer.cueVideo("USFf4TTc_ok");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });
    }

    public void onClick(View arg0) {

        if (arg0.getId() == R.id._start_) {

            if (String.valueOf(editText.getText()).equals("")) {
                youTubePlayer.loadVideo("USFf4TTc_ok");
            } else {
                youTubePlayer.loadVideo(String.valueOf(editText.getText()));
            }
        }
    }

}