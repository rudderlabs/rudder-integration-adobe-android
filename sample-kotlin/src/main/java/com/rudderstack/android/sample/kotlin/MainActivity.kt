package com.rudderstack.android.sample.kotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rudderlabs.android.sample.kotlin.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val events = ECommerceEvents()

        findViewById<Button>(R.id.navigateToVideoActivity)?.setOnClickListener { moveToVideoActivity() }
        findViewById<Button>(R.id.productViewed)?.setOnClickListener {events.productViewed()}         // Product Viewed and Product List Viewed events are same
        findViewById<Button>(R.id.productListViewed)?.setOnClickListener {events.productViewed()}     // Product Viewed and Product List Viewed events are same

        findViewById<Button>(R.id.productAdded)?.setOnClickListener {events.productAdded()}
        findViewById<Button>(R.id.productRemoved)?.setOnClickListener {events.productRemoved()}
        findViewById<Button>(R.id.orderCompleted)?.setOnClickListener {events.orderCompleted()}
        findViewById<Button>(R.id.cartViewed)?.setOnClickListener {events.cartViewed()}
        findViewById<Button>(R.id.checkoutStarted)?.setOnClickListener {events.checkoutStarted()}
        findViewById<Button>(R.id.cartOpened)?.setOnClickListener {events.cartOpened()}
        findViewById<Button>(R.id.identify)?.setOnClickListener {events.identify()}
        findViewById<Button>(R.id.customTrackWithProperties)?.setOnClickListener {events.customTrackWithProperties()}
        findViewById<Button>(R.id.customTrackWithoutProperties)?.setOnClickListener {events.customTrackWithoutProperties()}
        findViewById<Button>(R.id.screen)?.setOnClickListener {events.screen()}
        findViewById<Button>(R.id.reset)?.setOnClickListener {events.reset()}
        findViewById<Button>(R.id.flush)?.setOnClickListener {events.flush()}
//        findViewById<Button>(R.id.)?.setOnClickListener {}
//        findViewById<Button>(R.id.)?.setOnClickListener {}
//        findViewById<Button>(R.id.)?.setOnClickListener {}
//        findViewById<Button>(R.id.)?.setOnClickListener {}
//        findViewById<Button>(R.id.)?.setOnClickListener {}
//        findViewById<Button>(R.id.)?.setOnClickListener {}
//        findViewById<Button>(R.id.)?.setOnClickListener {}


    }

    fun moveToVideoActivity() {
        val i = Intent(this@MainActivity, VideoActivity::class.java)
        startActivity(i)
    }
}
