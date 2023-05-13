package com.origins.predictor.base.coreui.utils.sharing

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.origins.predictor.R
import com.origins.predictor.base.core.logger.predictorLog
import com.origins.predictor.base.coreui.utils.PredictorNumberItemDecoration
import com.origins.predictor.base.coreui.utils.extensions.loadBitmap
import com.origins.predictor.databinding.PredictorShareViewBinding
import com.origins.predictor.features.prediction.ui.ShareUi
import com.origins.predictor.features.prediction.views.PredictionScorePickerAdapter
import com.origins.resources.entity.extensions.setImageKMM
import com.origins.resources.entity.extensions.setLabelKMM
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


internal class PredictorShareManager(
    private val app: Application,
    private val qrCode: String?,
    private val bgDispatcher: CoroutineDispatcher
) {
    suspend fun share(item: ShareUi): Intent {

        return withContext(bgDispatcher) {

            val shareBinding = PredictorShareViewBinding.inflate(LayoutInflater.from(app), FrameLayout(app), false)

            val predictionHomeScorePickerAdapter = PredictionScorePickerAdapter(
                isHomeTeam = true,
                isShare = true
            ).apply { range = item.range }
            val predictionAwayScorePickerAdapter = PredictionScorePickerAdapter(
                isHomeTeam = false,
                isShare = true
            ).apply {
                range = item.range
            }

            shareBinding.homeTeamNumbers.adapter = predictionHomeScorePickerAdapter
            shareBinding.awayTeamNumbers.adapter = predictionAwayScorePickerAdapter
            val homeTeamScoreIndex = predictionHomeScorePickerAdapter.numbers.indexOf(
                item.score.homeTeamScore.text.getString(app).toInt()
            )
            val awayTeamScoreIndex = predictionAwayScorePickerAdapter.numbers.indexOf(
                item.score.awayTeamScore.text.getString(app).toInt()
            )
            predictionHomeScorePickerAdapter.selectedPosition = homeTeamScoreIndex
            predictionAwayScorePickerAdapter.selectedPosition = awayTeamScoreIndex

            shareBinding.homeTeamNumbers.scrollToPosition(homeTeamScoreIndex)
            shareBinding.awayTeamNumbers.scrollToPosition(awayTeamScoreIndex)

            shareBinding.awayTeamNumbers.addItemDecoration(
                PredictorNumberItemDecoration(
                    app.applicationContext,
                    predictionAwayScorePickerAdapter,
                    false
                )
            )
            shareBinding.homeTeamNumbers.addItemDecoration(
                PredictorNumberItemDecoration(
                    app.applicationContext,
                    predictionHomeScorePickerAdapter,
                    false
                )
            )

            shareBinding.setBitmapData(app, item, bgDispatcher)

            val layoutParams = shareBinding.root.layoutParams
            val rowWidthSpec: Int
            val rowHeightSpec: Int
            if (layoutParams.width > 0 && layoutParams.height > 0) {
                rowWidthSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.width, View.MeasureSpec.EXACTLY)
                rowHeightSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.height, View.MeasureSpec.EXACTLY)
            } else {
                rowWidthSpec = View.MeasureSpec.makeMeasureSpec(
                    Resources.getSystem().displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
                )
                rowHeightSpec = View.MeasureSpec.makeMeasureSpec(
                    ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED
                )
            }
            val isQrCodeVisible = !qrCode.isNullOrBlank()
            shareBinding.qrCodeContainer.isVisible = isQrCodeVisible

            shareBinding.root.measure(rowWidthSpec, rowHeightSpec)
            shareBinding.root.layout(0, 0, shareBinding.root.measuredWidth, shareBinding.root.measuredHeight)


            if (isQrCodeVisible) {
                shareBinding.qrCodeTopCorner.setImageKMM(item.qrCodeCorner)
                shareBinding.qrCodeBottomCorner.setImageKMM(item.qrCodeCorner)
                shareBinding.qrCodeImage.setImageDrawable(
                    createQRCodeBitmap(
                        app.applicationContext,
                        BarcodeFormat.QR_CODE,
                        qrCode!!,
                        shareBinding.qrCodeImage.measuredWidth,
                        shareBinding.qrCodeImage.measuredHeight
                    )
                )
            }

            val bitmap = Bitmap.createBitmap(
                shareBinding.root.measuredWidth,
                shareBinding.root.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            shareBinding.root.draw(canvas)

            val share = Intent(Intent.ACTION_SEND)
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            val photoUri = FileProvider.getUriForFile(
                app,
                "${app.packageName}.share.provider",
                getImagePath(app, bitmap)
            )
            share.type = "image/jpeg"
            share.putExtra(Intent.EXTRA_STREAM, photoUri)
            val intent = Intent.createChooser(share, item.chooserTitle.text.getString(app.applicationContext))
            val resInfoList: List<ResolveInfo> =
                app.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName: String = resolveInfo.activityInfo.packageName
                app.grantUriPermission(
                    packageName,
                    photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            intent
        }
    }
}

private fun getImagePath(app: Application, bitmap: Bitmap): File {
    val cacheDir = File(app.cacheDir, "share")

    if (!cacheDir.exists() || cacheDir.isFile) {
        cacheDir.delete()
        cacheDir.mkdirs()
    }

    val cacheFile = File(cacheDir, "share.jpeg")
    cacheFile.createNewFile()

    FileOutputStream(cacheFile).use { fos ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    }

    return cacheFile
}

private suspend fun PredictorShareViewBinding.setBitmapData(
    app: Application,
    shareUi: ShareUi,
    bgDispatcher: CoroutineDispatcher
): PredictorShareViewBinding {
    val iconSize = app.resources.getDimensionPixelSize(R.dimen.team_logo_icon_size)
    return this.apply {
        withContext(bgDispatcher) {
            try {
                homeTeamLogo.setImageBitmap(
                    withContext(bgDispatcher) {
                        loadBitmap(
                            shareUi.homeTeamLogo,
                            iconSize,
                            R.drawable.predictor_share_team_logo_placeholder,
                            app
                        )
                    })
            } catch (ex: Exception) {
                predictorLog("Failed to load homeTeamLogo")
            }
            try {
                awayTeamLogo
                    .setImageBitmap(withContext(bgDispatcher) {
                        loadBitmap(
                            shareUi.awayTeamLogo, iconSize, R.drawable.predictor_share_team_logo_placeholder,
                            app
                        )
                    })
            } catch (ex: Exception) {
                predictorLog("Failed to load awayTeamLogo")
            }
            root.setBackgroundResource(shareUi.background.imageResId)
            title.setLabelKMM(shareUi.title)
            stadium.setLabelKMM(shareUi.stadium)
            league.setLabelKMM(shareUi.league)
            homeTeamName.setLabelKMM(shareUi.homeTeamName)
            awayTeamName.setLabelKMM(shareUi.awayTeamName)
            scores.score = shareUi.score
            appLogo.setImageKMM(shareUi.appLogo)
            shareUi.date?.let {
                date.setLabelKMM(it)
                date.isVisible = true
            } ?: run { date.isVisible = false }
            time.setLabelKMM(shareUi.time)
        }
    }
}

private fun createQRCodeBitmap(
    context: Context,
    format: BarcodeFormat,
    barcode: String,
    width: Int,
    height: Int
): BitmapDrawable {
    val bitMatrix = MultiFormatWriter().encode(barcode, format, width, height, null)
    val bitMatrixWidth = bitMatrix.width
    val bitMatrixHeight = bitMatrix.height
    val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_8888)
    for (x in 0 until bitMatrixWidth) {
        for (y in 0 until bitMatrixHeight) {
            bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
        }
    }

    val croppedBitmap =
        Bitmap.createBitmap(bitmap, 20, 20, bitmap.width - 40, bitmap.height - 40)

    val drawable = BitmapDrawable(context.resources, croppedBitmap)
    drawable.setAntiAlias(true)
    drawable.isFilterBitmap = false
    return drawable
}
