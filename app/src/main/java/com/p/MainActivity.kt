package com.p
import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
class MainActivity: Activity() {
  override fun onCreate(s: Bundle?) { super.onCreate(s)
    if (!Settings.canDrawOverlays(this)) startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
    val m = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    startActivityForResult(m.createScreenCaptureIntent(), 1)
  }
  override fun onActivityResult(r:Int,c:Int,d:Intent?){ if(r==1&&c==RESULT_OK&&d!=null){
    val i=Intent(this,OverlayService::class.java).putExtra("c",c).putExtra("d",d)
    if(Build.VERSION.SDK_INT>=26) startForegroundService(i) else startService(i); finish() } }
}
