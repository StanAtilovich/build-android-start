/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.codelab.friendlychat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.codelab.friendlychat.databinding.ActivityMainBinding
import com.google.firebase.codelab.friendlychat.model.FriendlyMessage
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    val intentSingInActivity by lazy {
        Intent(this, SignInActivity::class.java)
    }

    private val openDocument = registerForActivityResult(MyOpenDocumentContract()) { uri ->
        uri?.let { onImageSelected(it) }
    }

    // TODO: implement Firebase instance variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This codelab uses View Binding
        // See: https://developer.android.com/topic/libraries/view-binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        auth = Firebase.auth
        checkAuthCurrentUser()
        // Initialize Realtime Database and FirebaseRecyclerAdapter
        // TODO: implement

        // Disable the send button when there's no text in the input field
        // See MyButtonObserver for details
        binding.messageEditText.addTextChangedListener(MyButtonObserver(binding.sendButton))

        // When the send button is clicked, send a text message
        binding.sendButton.setOnClickListener {
            val friendlyMessage = FriendlyMessage()

            messagesRef.child(MESSAGES_CHILD).push().setValue("sdsdgs")
        }

        // When the image button is clicked, launch the image picker
        binding.addMessageImageView.setOnClickListener {
            openDocument.launch(arrayOf("image/*"))
        }
    }

    public override fun onStart() {
        super.onStart()
        checkAuthCurrentUser()

    }


    public override fun onResume() {
        super.onResume()
        checkAuthCurrentUser()
    }

    private fun getPhotoUrl(): String? {
        val user = auth.currentUser
        return user?.photoUrl?.toString()
    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else {
            ANONYMOUS
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                signOut()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onImageSelected(uri: Uri) {
        // TODO: implement
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
        // Upload the image to Cloud Storage
        // TODO: implement
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(this)
        startActivity(intentSingInActivity)
        Toast.makeText(this, "Вышли из аккаунта", Toast.LENGTH_LONG).show()
    }


    private fun checkAuthCurrentUser() {
        if (auth.currentUser == null) {
            startActivity(intentSingInActivity)
            finish()
            return
        }
    }


    companion object {
        private const val TAG = "MainActivity"
        const val MESSAGES_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }
}
