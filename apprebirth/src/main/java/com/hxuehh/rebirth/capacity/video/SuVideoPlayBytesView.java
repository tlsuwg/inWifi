package com.hxuehh.rebirth.capacity.video;

import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.R;

import io.vov.vitamio.MediaFormat;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by suwg on 2015/10/9.
 */
public class SuVideoPlayBytesView extends FaceGetMainViewImp {


    private VideoView mVideoView;
    private ProgressBar pb;
    private TextView downloadRateView, loadpercentRateView;

    public SuVideoPlayBytesView(FaceContextWrapImp context, int Viewkey) {
        super(context, Viewkey);
        initView();
    }

    @Override
    protected void initView() {
        this.mainView = View.inflate(getFaceContext(), R.layout.videobuffer, null);
        mVideoView = (VideoView) mainView.findViewById(R.id.buffer);
        pb = (ProgressBar) mainView.findViewById(R.id.probar);
        pb.setVisibility(View.GONE);
        downloadRateView = (TextView) mainView.findViewById(R.id.download_rate);
        loadpercentRateView = (TextView) mainView.findViewById(R.id.load_rate);
    }

    Uri uri;
    public void initCoreView(int udpPort) {
        isEnd = false;
        uri = Uri.parse("udp://localhost:" + udpPort);
        start();
    }

    private void start() {
        Su.log("start");
        mVideoView.setBufferSize(5);

//        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
//        VIDEOQUALITY_LOW（流畅）、VIDEOQUALITY_MEDIUM（普通）、VIDEOQUALITY_HIGH（高质）。


//        mVideoView.setHardwareDecoder(true);
//        mVideoView.getAudioTrack();
//        mVideoView.setAudioTrack();
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Su.log("Vitamio mp" + what + "  " + extra);
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
//        mVideoView.setMediaController(new MediaController(getFaceContext()));
        mVideoView.requestFocus();
        mVideoView.setVolume(1f, 1f);
        mVideoView.setAudioTrack(1);


//        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Su.log("Vitamio setOnCompletionListener ");
//            }
//        });

//        例如：开始缓冲、缓冲结束、下载速度
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Su.log("Vitamio setOnInfoListener onInfo " + what + "  " + extra +" "+mp.getAudioTrack());
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING://媒体信息视频跟踪滞后
                    {

                    }
                    break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        if (mVideoView.isPlaying()) {
                            mVideoView.pause();
                            pb.setVisibility(View.VISIBLE);
                            downloadRateView.setText("");
                            loadpercentRateView.setText("缓冲中");
                            downloadRateView.setVisibility(View.VISIBLE);
                            loadpercentRateView.setVisibility(View.VISIBLE);
                            showInfo();
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mVideoView.start();
                        pb.setVisibility(View.GONE);
                        downloadRateView.setVisibility(View.GONE);
                        loadpercentRateView.setVisibility(View.GONE);

                        showInfo();
                        break;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        downloadRateView.setText("" + extra + "kb/s" + "  ");
                        showInfo();

                        break;
                }
                return false;

            }
        });
        mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Su.log("Vitamio setOnBufferingUpdateListener:" + percent);
                loadpercentRateView.setText(percent + "%");
            }
        });

        mVideoView.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Su.log("Vitamio setOnSeekCompleteListener");
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // optional need Vitamio 4.0


                Su.log("Vitamio setOnPreparedListener");

//                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });

//        mVideoView.resume();
        mVideoView.setVideoURI(uri);

    }

    private void showInfo() {
        SparseArray<MediaFormat>  mms=mVideoView.getAudioTrackMap("UTF-8");
        Su.log(mVideoView.getMetaEncoding() + " " + mVideoView.getAudioTrack());
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
