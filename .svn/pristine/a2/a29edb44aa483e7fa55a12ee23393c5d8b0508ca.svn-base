package com.unfpa.appsistenciamaternaunfpa.utils

import android.content.Context
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.unfpa.appsistenciamaternaunfpa.Mhealth.Companion.database
import com.unfpa.appsistenciamaternaunfpa.models.KnowledgeablePerson
import com.unfpa.appsistenciamaternaunfpa.models.User
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.OnKnowledgeablePersonFetched

/**
 * Created by KhyatiShah on 11/22/2019.
 */
class FirestoreUtil {
    /*private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document(
            "users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is null.")}"
        )

    private val chatChannelsCollectionRef = firestoreInstance.collection("chatChannels")

    fun updateCurrentUser(name: String = "", bio: String = "", profilePicturePath: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        if (profilePicturePath != null)
            userFieldMap["profilePicturePath"] = profilePicturePath
        currentUserDocRef.update(userFieldMap)
    }

    fun getOrCreateChatChannel(
        otherUserId: String,
        onComplete: (channelId: String) -> Unit
    ) {
        currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserId).get().addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }

                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                val newChannel = chatChannelsCollectionRef.document()
                // newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                currentUserDocRef
                    .collection("engagedChatChannels")
                    .document(otherUserId)
                    .set(mapOf("channelId" to newChannel.id))

                firestoreInstance.collection("users").document(otherUserId)
                    .collection("engagedChatChannels")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)
            }
    }
*/
    companion object {
        /*fun writeNewUser(userId: String) {
            val user = User(userId)
            database.child("Users").child(userId).setValue(user)

        }*/

        fun writeNewUser(user: User, knowledgeablePersonId: String) {
            //database.child("Users").child(knowledgeablePersonId).child(user.uId!!).setValue(user)
            database.child(Constant.USERS_NODE).child(Constant.QUEUE_NODE).child(knowledgeablePersonId)
                .child(user.uId!!).setValue(user)
            //database.child("Users").child(knowledgeablePersonId).setValue(user)

        }

        fun getKnowledgeablePersons(onKnowledgeablePersonFetched: OnKnowledgeablePersonFetched) {

            val lstKnowledgeablePerson = ArrayList<KnowledgeablePerson>()
            val knowledgeablePersonReferance = database.child(Constant.KNOWLEDGEABLE_PERSON_NODE)
            knowledgeablePersonReferance.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (childDataSnapshot in dataSnapshot.getChildren()) {
                        //Log.v("", "" + childDataSnapshot.getKey()) //displays the key for the node
                        /*val knowledgeablePerson = KnowledgeablePerson(
                            childDataSnapshot.child("UID").value.toString(),
                            childDataSnapshot.child("Name").value.toString(),
                            childDataSnapshot.child("CountryCode").value.toString()
                        )*/
                        try {
                            val objMain =
                                childDataSnapshot.value as java.util.HashMap<String, HashMap<String, String>>
                            for ((key, value) in objMain) {
                                val knowledgeablePerson = KnowledgeablePerson(
                                    value[Constant.KNOWLEDGEABLE_PERSON_UID],
                                    value[Constant.KNOWLEDGEABLE_PERSON_NAME],
                                    value[Constant.KNOWLEDGEABLE_PERSON_COUNTRY_CODE]
                                )
                                lstKnowledgeablePerson.add(knowledgeablePerson)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    knowledgeablePersonReferance.removeEventListener(this)
                    onKnowledgeablePersonFetched.onComplete(lstKnowledgeablePerson)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }

        fun setKPListener(childEventListenerKP: ChildEventListener) {
            val knowledgeablePerson = database.child(Constant.KNOWLEDGEABLE_PERSON_NODE)
            knowledgeablePerson.addChildEventListener(childEventListenerKP)
        }

        fun createChatChannel(
            userId: String,
            otherUserId: String,
            context: Context,
            childEventListener: ChildEventListener
        ) {
            val chatChannel = database.child(Constant.MESSAGES_NODE).child(userId + "_" + otherUserId)
            chatChannel.addChildEventListener(childEventListener)
        }

        fun removeChannel(userId: String, otherUserId: String) {
            val chatChannel = database.child(Constant.MESSAGES_NODE).child(userId + "_" + otherUserId)
            chatChannel.removeValue()
        }

        fun removeUser(userId: String, knowledgeablePersonId: String) {
            //deleting user from que
            val userNode = database.child(Constant.USERS_NODE).child(Constant.QUEUE_NODE).child(knowledgeablePersonId)
                .child(userId)
            //deleting user from conversassion
            val userNodeCon =
                database.child(Constant.USERS_NODE).child(Constant.CONVERSATION_NODE).child(knowledgeablePersonId)
                    .child(userId)
            userNode?.removeValue()
            userNodeCon?.removeValue()
        }

        fun sendMessage(userId: String, userName: String, otherUserId: String, message: String, stimestemp: Long) {
            val chatChannel = database.child(Constant.MESSAGES_NODE).child(userId + "_" + otherUserId)
            val map = HashMap<String, String>()
            map["message"] = message
            map["user"] = userId
            map["user_name"] = userName
            map["createdAt"] = stimestemp.toString()
            chatChannel.push().setValue(map)
        }

        fun updateMessageCount(userId: String, otherUserId: String) {
            var count = 0L
            val messageCount =
                database.child(Constant.MESSAGES_NODE).child(userId + "_" + otherUserId).child(Constant.MESSAGE_COUNT)
            messageCount.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    if (dataSnapshot.value != null) {
                        count = dataSnapshot.value as Long
                    }
                    messageCount.removeEventListener(this)
                    count++
                    messageCount.setValue(count)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message

                    // ...
                }
            })


        }
        //endregion FCM
    }
}