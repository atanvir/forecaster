package com.Bforecaster.Utility;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;

public class VideoViewHelper extends VideoView {
        private int mVideoWidth;
        private int mVideoHeight;

    public VideoViewHelper(Context context) {
        super(context);
    }

    public VideoViewHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoViewHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoViewHelper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


        @Override
        public void setVideoURI(Uri uri) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this.getContext(), uri);
            mVideoWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            mVideoHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            super.setVideoURI(uri);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // Log.i("@@@", "onMeasure");
            int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
            int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
            if (mVideoWidth > 0 && mVideoHeight > 0) {
                if (mVideoWidth * height > width * mVideoHeight) {
                    // Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                } else if (mVideoWidth * height < width * mVideoHeight) {
                    // Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else {
                    // Log.i("@@@", "aspect ratio is correct: " +
                    // width+"/"+height+"="+
                    // mVideoWidth+"/"+mVideoHeight);
                }
            }
            // Log.i("@@@", "setting size: " + width + 'x' + height);
            setMeasuredDimension(width, height);
        }
    }

