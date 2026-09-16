package com.p
import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.ImageReader
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.WindowManager
class OverlayService: Service() {
  private lateinit var aim: Aim
  override fun onBind(i: Intent?): IBinder? = null
  override fun onStartCommand(i: Intent?, f:Int, id:Int): Int {
    val ch="p"; if(Build.VERSION.SDK_INT>=26){ (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(NotificationChannel(ch,"p",NotificationManager.IMPORTANCE_LOW)) }
    startForeground(1, Notification.Builder(this, ch).setContentTitle("P").build())
    val wm=getSystemService(WINDOW_SERVICE) as WindowManager
    val dm=DisplayMetrics(); wm.defaultDisplay.getRealMetrics(dm)
    aim=Aim(this); wm.addView(aim, WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT,
      if(Build.VERSION.SDK_INT>=26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
      WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT))
    val mpm=getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    val mp=mpm.getMediaProjection(i!!.getIntExtra("c",0), i.getParcelableExtra("d")!!)
    val r=ImageReader.newInstance(dm.widthPixels,dm.heightPixels,1,2)
    mp.createVirtualDisplay("p",dm.widthPixels,dm.heightPixels,dm.densityDpi,DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,r.surface,null,null)
    r.setOnImageAvailableListener({ ir -> ir.acquireLatestImage()?.let{ aim.feed(it); it.close() } }, null)
    return START_STICKY
  }
}
