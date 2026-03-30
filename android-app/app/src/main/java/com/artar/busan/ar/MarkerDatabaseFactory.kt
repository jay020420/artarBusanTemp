package com.artar.busan.ar

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session

object MarkerDatabaseFactory {
    fun build(session: Session): AugmentedImageDatabase {
        val database = AugmentedImageDatabase(session)
        database.addImage("marker_001", createMarkerBitmap("ARTAR 001"))
        database.addImage("marker_002", createMarkerBitmap("ARTAR 002"))
        return database
    }

    fun configure(session: Session): Config {
        return session.config.apply {
            augmentedImageDatabase = build(session)
            planeFindingMode = Config.PlaneFindingMode.DISABLED
            updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
        }
    }

    private fun createMarkerBitmap(label: String): Bitmap {
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val border = Paint().apply {
            color = Color.BLACK
            strokeWidth = 18f
            style = Paint.Style.STROKE
        }
        canvas.drawRect(20f, 20f, 492f, 492f, border)

        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 56f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
        canvas.drawText(label, 256f, 280f, textPaint)
        return bitmap
    }
}
