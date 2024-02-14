package com.sayan.chatnow

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sayan.chatnow.databinding.ActivityChatBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ChatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatBinding
    private lateinit var messageList:ArrayList<MessageModel>
    private lateinit var messageAdapter:MessageAdapter
    private lateinit var dbRef:DatabaseReference

    var receiverRoom:String? =  null
     var senderRoom:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("id")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference()

        messageList= ArrayList()
        messageAdapter= MessageAdapter(this, messageList)

        val manager = LinearLayoutManager(this)
        binding.chatRecycler.layoutManager=manager
        binding.chatRecycler.adapter=messageAdapter
//        binding.chatRecycler.smoothScrollToPosition(messageList.size-1)

        senderRoom = receiverUid+senderUid
        receiverRoom=senderUid+receiverUid

        supportActionBar?.title=name



        dbRef.child("chats")
            .child(senderRoom!!)
            .child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapShot in snapshot.children){
                        val message = postSnapShot.getValue(MessageModel::class.java)
                        messageList.add(message!!)
                    }

                    messageAdapter.notifyDataSetChanged()

                    binding.chatRecycler.post {
                        binding.chatRecycler.scrollToPosition(messageList.size - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })



//        Log.d("TIME",formattedDateTime)




        binding.sendBtn.setOnClickListener {
            val message = binding.messageEt.text.toString()
            val currentDateTime = LocalTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val formattedDateTime = currentDateTime.format(formatter)
            val messageObject = MessageModel(message, senderUid!!, formattedDateTime)

            if((binding.messageEt.text.toString())!="") {
                dbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        dbRef.child("chats").child(receiverRoom!!)
                            .child("messages")
                            .push()
                            .setValue(messageObject)
                    }
                binding.messageEt.setText("")
            }else{
                Toast.makeText(applicationContext, "Write something!", Toast.LENGTH_SHORT).show()
            }
        }



    }




}