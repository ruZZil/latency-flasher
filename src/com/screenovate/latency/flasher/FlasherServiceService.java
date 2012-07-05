package com.screenovate.latency.flasher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class FlasherServiceService extends Service {

	private View timeView;

	private class TimeView extends View {
		private Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					updateDisplay();
					Message m = obtainMessage(1);
					sendMessageDelayed(m, 100);
				}
			}
		};

		private int counter;
		private Paint paintWhite;
		private Paint paintBlack;

		TimeView(Context c) {
			super(c);

			paintWhite = new Paint();
			paintWhite.setColor(android.graphics.Color.WHITE);
			paintBlack = new Paint();
			paintBlack.setColor(android.graphics.Color.BLACK);
			counter = 0;

			updateDisplay();
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
			mHandler.sendEmptyMessage(1);
		}

		@Override
		protected void onDetachedFromWindow() {
			super.onDetachedFromWindow();
			mHandler.removeMessages(1);
		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			if (getWidth() < getHeight()) {
				canvas.drawRect(150 - 64, 0, 150 + 64, 128,
						(counter % 10) > 4 ? paintWhite : paintBlack);
				canvas.drawRect(getWidth() - 150 - 64, 0,
						getWidth() - 150 + 64, 128,
						((counter - 1) % 10) > 4 ? paintWhite : paintBlack);
			} else {
				canvas.drawRect(0, getHeight() - 150 - 64, 128,
						getHeight() - 150 + 64, (counter % 10) > 4 ? paintWhite
								: paintBlack);
				canvas.drawRect(0, 150 - 64, 128, 150 + 64,
						((counter - 1) % 10) > 4 ? paintWhite : paintBlack);
			}

			counter++;

			// int width = 16;
			// int height = 16;

			// long currentTime = android.os.SystemClock.uptimeMillis();
			// for (int i= 0; i < 32; i++) {
			// canvas.drawRect(i*width, 0, (i+1)*width, height,
			// ((currentTime & (1L << i)) != 0) ? paintWhite: paintBlack);
			// }
		}

		void updateDisplay() {
			invalidate();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		timeView = new TimeView(this);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SECURE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.RIGHT | Gravity.TOP;
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.addView(timeView, params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(timeView);
		timeView = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}