package com.mylektop.videomeeting.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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

    companion object {
        var selectedUsers: ArrayList<User> = ArrayList()
    }

    fun getSelectedUsers(): ArrayList<User> {
        return selectedUsers
    }

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
        private val userContainer = itemView.findViewById<ConstraintLayout>(R.id.userContainer)
        private val imageSelected = itemView.findViewById<ImageView>(R.id.imageSelected)

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

            userContainer.setOnLongClickListener {
                if (imageSelected.visibility != View.VISIBLE) {
                    selectedUsers.add(user)
                    imageSelected.visibility = View.VISIBLE
                    imageAudioMeeting.visibility = View.GONE
                    imageVideoMeeting.visibility = View.GONE

                    usersListener.onMultipleUsersAction(true)
                }

                return@setOnLongClickListener true
            }

            userContainer.setOnClickListener {
                if (imageSelected.visibility == View.VISIBLE) {
                    selectedUsers.remove(user)
                    imageSelected.visibility = View.GONE
                    imageAudioMeeting.visibility = View.VISIBLE
                    imageVideoMeeting.visibility = View.VISIBLE

                    if (selectedUsers.size > 0) {
                        usersListener.onMultipleUsersAction(false)
                    }
                } else {
                    if (selectedUsers.size > 0) {
                        selectedUsers.add(user)
                        imageSelected.visibility = View.VISIBLE
                        imageAudioMeeting.visibility = View.GONE
                        imageVideoMeeting.visibility = View.GONE
                    }
                }
            }
        }
    }
}