package com.example.gamebuddy.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.gamebuddy.ModelClasses.Users

import com.example.gamebuddy.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.view.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    var usersReference: DatabaseReference?= null
    var firebaseUser: FirebaseUser?= null
    private var RequestCode = 438
    private var imageUri: Uri? = null
    private var storageRef: StorageReference? = null
    private var coverChecker: String? = null
    private var socialChecker: String?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        usersReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        usersReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user: Users? = p0.getValue(Users::class.java)

                    if(context != null){
                        view.username_settings.text = user!!.getUserName()
                        Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile).into(view.profile_image_settings)
                        Picasso.get().load(user.getCover()).placeholder(R.drawable.cover).into(view.cover_image_settings)
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        view.profile_image_settings.setOnClickListener{
            pickImage()
        }

        view.cover_image_settings.setOnClickListener{
            coverChecker = "cover"
            pickImage()
        }

        view.set_steam.setOnClickListener{
            socialChecker = "steam"
            setSocialLinks()
        }
        view.set_reddit.setOnClickListener{
            socialChecker = "reddit"
            setSocialLinks()
        }
        view.set_twitch.setOnClickListener{
            socialChecker = "twitch"
            setSocialLinks()
        }


        return  view
    }

    private fun setSocialLinks() {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
            builder.setTitle("Kullan??c?? Ad??n??z?? Giriniz.")

            val editText = EditText(context)

            editText.hint = "Kullan??c?? Ad??"

            builder.setView(editText)

        builder.setPositiveButton("Create",DialogInterface.OnClickListener{
            dialog, which ->
            val str =editText.text.toString()

            if(str == null){
                Toast.makeText(context, "L??tfen Bir Kullan??c?? Ad?? Giriniz.", Toast.LENGTH_LONG).show()
            }
            else{
                saveSocialLink(str)
            }
        })
        builder.setNegativeButton("Cancel",DialogInterface.OnClickListener{
                dialog, which ->
            dialog.cancel()
        })
        builder.show()
    }

    private fun saveSocialLink(str: String) {
        val mapSocial = HashMap<String, Any>()
//       usersReference!!.updateChildren(mapSocial)

        when(socialChecker){
            "steam" -> {
                mapSocial["steam"] = "https://steamcommunity.com/id/$str"
            }
            "reddit" -> {
                mapSocial["reddit"] = "https://www.reddit.com/user/$str"
            }
            "twitch" -> {
                mapSocial["twitch"] = "https:://www.twitch.tv/$str"
            }
        }
        usersReference!!.updateChildren(mapSocial).addOnCompleteListener{task ->
            if(task.isSuccessful){
                Toast.makeText(context, "Kaydedildi.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data != null){
            imageUri = data.data
            Toast.makeText(context, "Y??kleniyor...", Toast.LENGTH_LONG).show()
            uploadImageToDatabase()
            }
    }

    private fun uploadImageToDatabase() {
        val progressBar = ProgressDialog(context)
        progressBar.setMessage("Foto??raf Y??kleniyor..")
        progressBar.show()

        if(imageUri != null){
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")

            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{task ->
                if(task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }

                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    if(coverChecker == "cover"){
                        val mapCoverImg = HashMap<String, Any>()
                        mapCoverImg["cover"] = url
                        usersReference!!.updateChildren(mapCoverImg)
                        coverChecker = ""
                    }
                    else
                    {
                        val mapProfileImg = HashMap<String, Any>()
                        mapProfileImg["profile"] = url
                        usersReference!!.updateChildren(mapProfileImg)
                        coverChecker = ""
                    }
                    progressBar.dismiss()
                }
            }
        }
    }
}
