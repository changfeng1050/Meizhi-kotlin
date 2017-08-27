package me.zjl.meizhi.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.widget.ImageView
import com.squareup.picasso.Picasso
import me.zjl.meizhi.R
import me.zjl.meizhi.ui.base.ToolBarActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor
import uk.co.senab.photoview.PhotoViewAttacher

/**
 * Created by chang on 2017-08-23.
 */
class PictureActivity : ToolBarActivity() {

    companion object {
        val EXTRA_IMAGE_URL = "image_url"
        val EXTRA_IMAGE_TITLE = "image_title"
        val TRANSIT_PIC = "picture"

        fun newIntent(context: Context, url: String, desc: String): Intent {
            return context.intentFor<PictureActivity>(EXTRA_IMAGE_URL to url, EXTRA_IMAGE_TITLE to desc)
        }
    }

    private lateinit var imageView: ImageView

    private lateinit var photoViewAttacher: PhotoViewAttacher

    private lateinit var imageUrl: String
    private lateinit var imageTitle: String

    override fun provideContentViewId(): Int {
        return R.layout.activity_picture
    }

    override fun canBack() = true

    private fun parseIntent() {
        imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        imageTitle = intent.getStringExtra(EXTRA_IMAGE_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
        initView()
        ViewCompat.setTransitionName(imageView, TRANSIT_PIC)
        Picasso.with(this).load(imageUrl).into(imageView)
        setAppBarAlpha(0.7f)
        title = imageTitle
        setupPhotoAttacker()

    }

    private fun initView() {
        imageView = find(R.id.picture)
    }

    private fun setupPhotoAttacker() {
        photoViewAttacher = PhotoViewAttacher(imageView)
        photoViewAttacher.onViewTapListener = PhotoViewAttacher.OnViewTapListener { _, _, _ ->
            hideOrShowToolBar()
        }
        photoViewAttacher.setOnLongClickListener {
            alert {
                message(R.string.ask_saving_picture)
                yesButton {
                    saveImageToGallery()
                }
                noButton {

                }
            }.show()

            true
        }
    }

    private fun saveImageToGallery() {

    }

    override fun onDestroy() {
        super.onDestroy()
        photoViewAttacher.cleanup()
    }

}