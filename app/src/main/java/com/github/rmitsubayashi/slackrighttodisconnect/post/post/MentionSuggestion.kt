package com.github.rmitsubayashi.slackrighttodisconnect.post.post

import android.os.Parcel
import android.os.Parcelable
import com.linkedin.android.spyglass.mentions.Mentionable

class MentionSuggestion : Mentionable {
    private val suggestion: String
    constructor(suggestion: String) {
        this.suggestion = suggestion
    }
    constructor(parcel: Parcel) {
        this.suggestion = parcel.readString()?:""
    }
    override fun describeContents(): Int = 0
    override fun writeToParcel(destination: Parcel, flag: Int) {
        destination.writeString(suggestion)
    }

    override fun getSuggestibleId(): Int = suggestion.hashCode()
    override fun getSuggestiblePrimaryText(): String = suggestion
    override fun getDeleteStyle(): Mentionable.MentionDeleteStyle = Mentionable.MentionDeleteStyle.PARTIAL_NAME_DELETE
    override fun getTextForDisplayMode(mode: Mentionable.MentionDisplayMode?): String =
        when (mode) {
            Mentionable.MentionDisplayMode.FULL -> suggestion
            else -> ""
        }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MentionSuggestion> = object: Parcelable.Creator<MentionSuggestion> {
            override fun createFromParcel(inParcel: Parcel): MentionSuggestion =
                MentionSuggestion(
                    inParcel
                )
            override fun newArray(size: Int): Array<MentionSuggestion?> = arrayOfNulls(size)
        }
    }
}