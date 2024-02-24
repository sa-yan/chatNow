package com.sayan.chatnow

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(val context:Context, val userList:ArrayList<UserModel>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currUser = userList[position]
        holder.name.text = currUser.name

        Picasso
            .get()
            .load(currUser.pp)
            .placeholder(R.drawable.user)
            .into(holder.pp)

        holder.userItem.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name",currUser.name)
            intent.putExtra("id",currUser.uid)
            intent.putExtra("pp",currUser.pp)
            context.startActivity(intent)
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.name_user)
        val pp = itemView.findViewById<CircleImageView>(R.id.profileImage)
        val userItem = itemView.findViewById<RelativeLayout>(R.id.item_person)
    }
}