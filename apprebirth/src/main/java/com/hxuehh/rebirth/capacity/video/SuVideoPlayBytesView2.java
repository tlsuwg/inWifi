package com.hxuehh.rebirth.capacity.video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.R;

/**
 * Created by suwg on 2015/10/9.
 */
public class SuVideoPlayBytesView2 extends FaceGetMainViewImp {


    private VideoView mVideoView;
    private ProgressBar pb;
    private TextView downloadRateView, loadRateView;

    public SuVideoPlayBytesView2(FaceContextWrapImp context, int Viewkey) {
        super(context, Viewkey);
        initView();
    }

    @Override
    protected void initView() {
        this.mainView = View.inflate(getFaceContext(), R.layout.videobuffer2, null);
        mVideoView = (VideoView) mainView.findViewById(R.id.buffer);
        pb = (ProgressBar) mainView.findViewById(R.id.probar);
        pb.setVisibility(View.GONE);
        downloadRateView = (TextView) mainView.findViewById(R.id.download_rate);
        loadRateView = (TextView) mainView.findViewById(R.id.load_rate);
    }

    Uri uri;
    public void initCoreView(int udpPort) {
        isEnd = false;
        uri = Uri.parse("udp://localhost:" + udpPort);
        start();
    }

    private void start() {
        Su.log("start");
        mVideoView.resume();

        mVideoView.setVideoURI(uri);

//        mVideoView.setHardwareDecoder(true);
//        mVideoView.getAudioTrack();
//        mVideoView.setAudioTrack();
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Su.log("mp" + what + "  " + extra);
                if (extra != -5) {
                    if (!isEnd) {
                        end();
                        start();
                    }
                } else {
                    downloadRateView.setText("-5;端口占用");
                }
                return true;
            }
        });


        pb.setVisibility(View.VISIBLE);
        mVideoView.setMediaController(new MediaController(getFaceContext()));
        mVideoView.requestFocus();



//        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                return false;
//            }
//        });
//
//        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                switch (what) {
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                        if (mVideoView.isPlaying()) {
//                            mVideoView.pause();
//                            pb.setVisibility(View.VISIBLE);
//                            downloadRateView.setText("");
//                            loadRateView.setText("");
//                            downloadRateView.setVisibility(View.VISIBLE);
//                            loadRateView.setVisibility(View.VISIBLE);
//                            showInfo();
//                        }
//                        break;
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                        mVideoView.start();
//                        pb.setVisibility(View.GONE);
//                        downloadRateView.setVisibility(View.GONE);
//                        loadRateView.setVisibility(View.GONE);
//
//                        showInfo();
//                        break;
//
//                }
//                return true;
//
//            }
//        });


        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // optional need Vitamio 4.0
//                mediaPlayer.start();
            }
        });
    }

    private void showInfo() {

    }


    boolean isEnd;

    public void end() {
        Su.log("end");
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }

        isEnd = true;
    }
}
