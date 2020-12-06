package tn.dev.netperf.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    long _initialTime_, _lV_Time_, _sV_Time_, _buff_sV_Time_, _buff_fV_Time_, buff_Time;

    int buffCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.fragment_youtube, container, false);

        startButton = myView.findViewById(R.id._start_);
        startButton.setOnClickListener(this);
        editText = myView.findViewById(R.id.input_text);
        CueVideo();
        return myView;
    }

    private void initializeYoutubePlayer() {

        youTubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_player_, youTubePlayerFragment, TAG);
        transaction.commit();

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(Config.getYoutubeApiKey(), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

                _initialTime_ = System.currentTimeMillis();

                final YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(String s) {
                        _lV_Time_ = System.currentTimeMillis();
                        Log.e(TAG, "loaded after : " + (_lV_Time_ - _initialTime_) + " ms");
                    }

                    @Override
                    public void onAdStarted() {
                    }

                    @Override
                    public void onVideoStarted() {
                        _sV_Time_ = System.currentTimeMillis();
                        Log.e(TAG, "Started after : " + (_sV_Time_ - _initialTime_) + " ms");
                    }

                    @Override
                    public void onVideoEnded() {
                        Log.e(TAG, "details \n : " + (_lV_Time_ - _initialTime_) + " ms \n"
                                + (_sV_Time_ - _initialTime_) + " ms \n"
                                + buffCount + " times");
                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {
                    }
                };

                final YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {
                    }

                    @Override
                    public void onPaused() {
                    }

                    @Override
                    public void onStopped() {
                    }

                    @Override
                    public void onBuffering(boolean b) {
                        if (!b) {
                            _buff_sV_Time_ = System.currentTimeMillis();
                        } else {
                            _buff_fV_Time_ = System.currentTimeMillis();
                        }

                        buffCount++;
                        long buff_Time_ = _buff_fV_Time_ - _buff_sV_Time_;
                        buff_Time = buff_Time + buff_Time_;
                        Log.e(TAG, "buffered : " + buffCount + " times \n" + buff_Time + " ms");
                    }

                    @Override
                    public void onSeekTo(int i) {
                    }
                };

                player.setPlayerStateChangeListener(mPlayerStateChangeListener);
                player.setPlaybackEventListener(mPlaybackEventListener);
                if (!wasRestored) {
                    youTubePlayer = player;
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                    youTubePlayer.loadVideo("USFf4TTc_ok");

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }
        });
    }

    public void onClick(View arg0) {

        if (arg0.getId() == R.id._start_) {
            _initialTime_ = 0;
            _lV_Time_ = 0;
            _sV_Time_ = 0;
            _buff_sV_Time_ = 0;
            _buff_fV_Time_ = 0;
            buffCount = 0;
            buff_Time = 0;


            if (String.valueOf(editText.getText()).equals("")) {
                initializeYoutubePlayer();
            } else {
                youTubePlayer.loadVideo(String.valueOf(editText.getText()));
            }
        }
    }


    public void CueVideo() {

        youTubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_player_, youTubePlayerFragment, TAG);
        transaction.commit();

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(Config.getYoutubeApiKey(), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    youTubePlayer = youTubePlayer;
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                    youTubePlayer.cueVideo("USFf4TTc_ok");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

}