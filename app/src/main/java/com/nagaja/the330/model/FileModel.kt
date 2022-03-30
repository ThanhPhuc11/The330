package com.nagaja.the330.model

import android.os.Parcel
import android.os.Parcelable

class FileModel (
    var id: Int? = null,
    var priority: Int? = null,
    var suffixUrl: String? = null,
    var url: String? = null,
    var fileName: String? = null,
    var uri: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(priority)
        parcel.writeString(suffixUrl)
        parcel.writeString(url)
        parcel.writeString(fileName)
        parcel.writeString(uri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FileModel> {
        override fun createFromParcel(parcel: Parcel): FileModel {
            return FileModel(parcel)
        }

        override fun newArray(size: Int): Array<FileModel?> {
            return arrayOfNulls(size)
        }
    }
}