package com.sogia.trainedmodeldemo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val localModel = LocalModel.Builder().setAssetFilePath("newlinear.tflite")
            // or .setAbsoluteFilePath(absolute file path to model file)
            // or .setUri(URI to model file)
            .build()

        val customImageLabelerOptions =
            CustomImageLabelerOptions.Builder(localModel).setConfidenceThreshold(0.5f)
                .setMaxResultCount(5).build()
        val labeler = ImageLabeling.getClient(customImageLabelerOptions)

        var image: InputImage? = null
        try {
            val uri: Uri = Uri.parse("android.resource://com.sogia.trainedmodeldemo/drawable/demo")
            val stream: InputStream? = contentResolver.openInputStream(uri)

            image = InputImage.fromFilePath(this, uri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        image?.let {
            labeler.process(image).addOnSuccessListener { labels ->
                Log.e("GianhTran", labels.toString())
            }.addOnFailureListener { e ->
                Log.e("GianhTran", e.message ?: "")
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}