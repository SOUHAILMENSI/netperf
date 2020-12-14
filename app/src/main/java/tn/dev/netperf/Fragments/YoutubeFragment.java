package tn.dev.netperf.Fragments;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import tn.dev.netperf.R;
import tn.dev.netperf.Utils.Config;

public class YoutubeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = YoutubeFragment.class.getSimpleName();
    private YouTubePlayer youTubePlayer;
    private Context mContext;
    private MaterialButton startButton;
    private TextInputEditText editText;
    private YouTubePlayerSupportFragmentX youTubePlayerFragment;
    private long _initialTime_, _lV_Time_, _sV_Time_, _buff_sV_Time_, _buff_fV_Time_, buff_Time, t_start_first_pic, t_finish_first_pic;

    private TextView tv1, tv2, tv3, tv4;

    int buffCount;
    private String videoId = "";


    private AudioManager audioManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.fragment_youtube, container, false);

        startButton = myView.findViewById(R.id._start_);
        startButton.setOnClickListener(this);
        editText = myView.findViewById(R.id.input_text);
        tv1 = myView.findViewById(R.id.tv_first);
        tv2 = myView.findViewById(R.id.tv_load_delay);
        tv3 = myView.findViewById(R.id.tv_start_delay);
        tv4 = myView.findViewById(R.id.tv_buff_count);

        CueVideo();
        mContext = getActivity().getApplicationContext();
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
                        tv2.setText(String.valueOf(_lV_Time_ - _initialTime_));
                    }

                    @Override
                    public void onAdStarted() {
                        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
                    }

                    @Override
                    public void onVideoStarted() {
                        _sV_Time_ = System.currentTimeMillis();
                        Log.e(TAG, "Started after : " + (_sV_Time_ - _initialTime_) + " ms");
                        tv3.setText(String.valueOf(_sV_Time_ - _initialTime_));}

                    @Override
                    public void onVideoEnded() {
                        Log.e(TAG, "details \n : " + (_lV_Time_ - _initialTime_) + " ms \n"
                                + (_sV_Time_ - _initialTime_) + " ms \n"
                                + buffCount + " times");}

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

                        buffCount++;
                        long buff_Time_ = _buff_fV_Time_ - _buff_sV_Time_;
                        buff_Time = buff_Time + buff_Time_;
                        Log.e(TAG, "buffered : " + buffCount + " times \n" + buff_Time + " ms");

                        tv4.setText(String.valueOf(buffCount));
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

    public void CueVideo() {

        youTubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_player_, youTubePlayerFragment, TAG);
        transaction.commit();

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(Config.getYoutubeApiKey(), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayerX, boolean b) {
                if (!b) {
                    youTubePlayer = youTubePlayerX;
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                    youTubePlayer.cueVideo("USFf4TTc_ok");

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }


    private void initializeYoutubePlayer2() {

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
                        tv2.setText(String.valueOf(_lV_Time_ - _initialTime_));
                    }

                    @Override
                    public void onAdStarted() {
                    }

                    @Override
                    public void onVideoStarted() {
                        _sV_Time_ = System.currentTimeMillis();
                        Log.e(TAG, "Started after : " + (_sV_Time_ - _initialTime_) + " ms");

                        tv3.setText(String.valueOf(_sV_Time_ - _initialTime_));
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

                        if (youTubePlayer != null) {
                            if (youTubePlayer.isPlaying()) {
                                long currentPlayerTime = youTubePlayer.getCurrentTimeMillis();
                                youTubePlayer.pause();
                            }
                        }
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

                        tv4.setText(String.valueOf(buffCount));
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
                    youTubePlayer.loadVideo(editText.getText().toString());

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

            picassoPic();
            if (String.valueOf(editText.getText()).equals("")) {
                initializeYoutubePlayer();
            } else {
                initializeYoutubePlayer2();
            }
        }
    }

    public void picassoPic() {

        t_finish_first_pic = 0;
        t_start_first_pic = 0;

        if (editText.getText().toString().equals("")) {
            videoId = "USFf4TTc_ok";
        } else {
            videoId = String.valueOf(editText.getText());
        }
        String img_url = "http://img.youtube.com/vi/" + videoId + "/0.jpg";

        t_start_first_pic = System.currentTimeMillis();
        LruCache lruCache = new LruCache(mContext);
        lruCache.clear();
        Picasso picasso = new Picasso.Builder(mContext).memoryCache(lruCache).build();
        picasso.load(img_url);

        t_finish_first_pic = System.currentTimeMillis();
        //Picasso.get().invalidate(img_url);
        tv1.setText(String.valueOf(t_finish_first_pic - t_start_first_pic));


    }

}