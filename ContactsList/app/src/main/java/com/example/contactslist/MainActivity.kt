package com.example.contactslist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val userPermissionConstantContacts : Int = 404
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            userPermissionConstantContacts -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeContactsList()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.permission_error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return

            }
        }
    }
    fun makeContactsList(){
        val viewManager = LinearLayoutManager(this)
        val contacts = fetchAllContacts()
        Toast.makeText(
            this@MainActivity,
            resources.getQuantityString(R.plurals.contact_plurals, contacts.size, contacts.size),
            Toast.LENGTH_SHORT
        ).show()

        myRecyclerView.apply {
            layoutManager = viewManager
            print("ASA")
            adapter = ContactAdapter(contacts) {
                val intent: Intent =
                    Intent(Intent.ACTION_DIAL)
                with(intent){
                    data = Uri.parse("tel:${it.phoneNumber}")
                }
                startActivity(intent)

            }
        }
    }
    fun onStartClickEvent(view: View?) {
        if (view is Button) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), userPermissionConstantContacts)
            }
            else {
                makeContactsList()
            }
        }
    }
}