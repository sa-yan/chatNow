package com.sayan.chatnow

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sayan.chatnow.databinding.ActivityChatBinding

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

        binding.chatRecycler.layoutManager=LinearLayoutManager(this)
        binding.chatRecycler.adapter=messageAdapter

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
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        Log.d("MESSAGE",messageList.toString())


        binding.sendBtn.setOnClickListener {
            val message = binding.messageEt.text.toString()
            val messageObject = MessageModel(message, senderUid!!)

            dbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    dbRef.child("chats").child(receiverRoom!!)
                        .child("messages")
                        .push()
                        .setValue(messageObject)
                }
            binding.messageEt.setText("")

        }
    }

    //function to add messages in recyclerview


}