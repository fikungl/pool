package com.p
import android.content.Context
import android.graphics.*
import android.media.Image
import android.view.View
import kotlin.math.*
class Aim(c: Context): View(c) {
  private val p=Paint().apply{ color=Color.GREEN; strokeWidth=4f; style=Paint.Style.STROKE }
  private val balls=mutableListOf<FloatArray>() // x,y,r
  private var cue: FloatArray? = null
  fun feed(img: Image){
    val pl=img.planes[0]; val b=pl.buffer; val ps=pl.pixelStride; val rs=pl.rowStride
    val w=img.width; val h=img.height; balls.clear(); cue=null
    val step=8
    var y=0; while(y<h){ var x=0; while(x<w){ val o=y*rs+x*ps
      val R=b.get(o).toInt() and 0xff; val G=b.get(o+1).toInt() and 0xff; val B=b.get(o+2).toInt() and 0xff
      if(R>220&&G>220&&B>220){ cue=floatArrayOf(x.toFloat(),y.toFloat(),20f) }
      else if((R>180&&G<120&&B<120)||(R<120&&G<120&&B>180)||(R>200&&G>150&&B<80)){
        balls.add(floatArrayOf(x.toFloat(),y.toFloat(),20f)) }
      x+=step }; y+=step }
    postInvalidate()
  }
  override fun onDraw(cv: Canvas){ val c=cue?:return
    var best: FloatArray?=null; var bd=Float.MAX_VALUE
    for(t in balls){ val d=hypot(t[0]-c[0],t[1]-c[1]); if(d<bd){bd=d;best=t} }
    best?.let{ cv.drawLine(c[0],c[1],it[0],it[1],p); cv.drawCircle(it[0],it[1],it[2],p) }
  }
}
