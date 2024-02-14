    package com.sayan.chatnow.activities

    import android.content.Intent
    import android.opengl.Visibility
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.view.Menu
    import android.view.MenuItem
    import android.view.View
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.facebook.shimmer.Shimmer
    import com.facebook.shimmer.ShimmerFrameLayout
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.database.DataSnapshot
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.DatabaseReference
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.database.ValueEventListener
    import com.sayan.chatnow.R
    import com.sayan.chatnow.UserAdapter
    import com.sayan.chatnow.UserModel

    class MainActivity : AppCompatActivity() {
        private lateinit var auth:FirebaseAuth
        private lateinit var userList:ArrayList<UserModel>
        private lateinit var adapater:UserAdapter
        private lateinit var dbRef:DatabaseReference
        private lateinit var shimmer: ShimmerFrameLayout

        private lateinit var recyclerView:RecyclerView
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            shimmer=findViewById<ShimmerFrameLayout>(R.id.shimmerEffect)

            auth = FirebaseAuth.getInstance()
            dbRef=FirebaseDatabase.getInstance().getReference()
            recyclerView=findViewById(R.id.userRecycler)

            userList= ArrayList()
            adapater= UserAdapter(this, userList)
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager=layoutManager
            recyclerView.adapter=adapater

            shimmer.startShimmer()
            shimmer.visibility=View.VISIBLE
            dbRef.child("users").addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    for(postsnapshot in snapshot.children){
                        val currentUser = postsnapshot.getValue(UserModel::class.java)

                         if(auth.currentUser?.uid != currentUser?.uid){
                             userList.add(currentUser!!)
                         }

                        adapater.notifyDataSetChanged()

                        shimmer.stopShimmer()
                        shimmer.visibility=View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu, menu)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if(item.itemId==R.id.logout){
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            return true


        }
    }