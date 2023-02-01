package com.example.testapplication

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchLinkCreateListener
import io.branch.referral.util.LinkProperties
import java.util.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{

            val buo = BranchUniversalObject()
                .setCanonicalIdentifier("content/12345")
                .setTitle("My Deep Link Test")
                .setContentDescription("Dummy content to test deep linking")
                .setContentImageUrl("https://lorempixel.com/400/400")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                //.setContentMetadata(ContentMetadata().addCustomMetadata("key1", "value1"))

            val lp = LinkProperties()
                .setChannel("testing deep link")
                .setFeature("sharing")
                .setCampaign("deep link launch")
                .setStage("new user")
                .addControlParameter("deep_link_test","other")

            buo.generateShortUrl(this, lp,
                BranchLinkCreateListener { url, error ->
                    if (error == null) {
                        Log.i("MyApp", "got my Branch link to share: $url")

                        // Copy link to clipboard
                        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("deep_link", url)
                        clipboardManager.setPrimaryClip(clipData)
                        Toast.makeText(this,"Link Copied", Toast.LENGTH_SHORT).show()

                    } else {
                        Log.e("error", error.toString())
                    }
                })
        }
    }

    override fun onStart() {
        super.onStart()
        Branch.sessionBuilder(this).withCallback { branchUniversalObject, linkProperties, error ->
            if (error != null) {
                Log.e("BranchSDK_Tester", "branch init failed. Caused by -" + error.message)
            } else {
                Log.i("BranchSDK_Tester", "branch init complete!")
                if (branchUniversalObject != null) {
                    Log.i("BranchSDK_Tester", "title " + branchUniversalObject.title)
                    Log.i(
                        "BranchSDK_Tester",
                        "CanonicalIdentifier " + branchUniversalObject.canonicalIdentifier
                    )
                    Log.i(
                        "BranchSDK_Tester",
                        "metadata " + branchUniversalObject.contentMetadata.convertToJson()
                    )
                    Log.i(
                        "LaunchFrom_BUO",
                        "metadata " + branchUniversalObject.contentMetadata.customMetadata["deep_link_test"]
                    )

                    // Launch OtherActivity for deep_link_test
                    if(branchUniversalObject.contentMetadata.customMetadata["deep_link_test"] == "other"){
                        val intent = Intent(this, OtherActivity::class.java)
                        startActivity(intent)
                    }

                }

                if (linkProperties != null) {
                    Log.i("BranchSDK_Tester", "Channel " + linkProperties.channel)
                    Log.i("BranchSDK_Tester", "control params " + linkProperties.controlParams)
                }
            }
        }.withData(this.intent.data).init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Branch.sessionBuilder(this).withCallback { referringParams, error ->
            if (error != null) {
                Log.e("BranchSDK_Tester", error.message)
            } else if (referringParams != null) {
                Log.e("BranchSDK_Tester", referringParams.toString())
            }
        }.reInit()
    }
}