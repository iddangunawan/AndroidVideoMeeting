package com.mylektop.videomeeting.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mylektop.videomeeting.R
import com.mylektop.videomeeting.lisneters.UsersListener
import com.mylektop.videomeeting.models.User

/**
 * Created by iddangunawan on 30/07/20
 */
class UsersAdapter(
    private var users: List<User>,
    private val usersListener: UsersListener
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_container_user, parent, false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setUserData(users[position], usersListener)
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView) {
        private val textFirstChar = itemView.findViewById<TextView>(R.id.textFirstChar)
        private val textUserName = itemView.findViewById<TextView>(R.id.textUserName)
        private val textEmail = itemView.findViewById<TextView>(R.id.textEmail)
        private val imageAudioMeeting = itemView.findViewById<ImageView>(R.id.imageAudioMeeting)
        private val imageVideoMeeting = itemView.findViewById<ImageView>(R.id.imageVideoMeeting)

        fun setUserData(user: User, usersListener: UsersListener) {
            textFirstChar.text = user.firstName?.substring(0, 1)
            textUserName.text = String.format("%s %s", user.firstName, user.lastName)
            textEmail.text = user.email

            imageAudioMeeting.setOnClickListener {
                usersListener.initiateAudioMeeting(user)
            }

            imageVideoMeeting.setOnClickListener {
                usersListener.initiateVideoMeeting(user)
            }
        }
    }
}