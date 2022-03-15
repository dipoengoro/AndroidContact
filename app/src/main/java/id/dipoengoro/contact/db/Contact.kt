package id.dipoengoro.contact.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_data_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contact_id")
    var id: Int,
    @ColumnInfo(name = "contact_name")
    var name: String,
    @ColumnInfo(name = "contact_phone")
    var phone: String,
    @ColumnInfo(name = "contact_phone_w_region")
    var phoneWRegion: String
)
