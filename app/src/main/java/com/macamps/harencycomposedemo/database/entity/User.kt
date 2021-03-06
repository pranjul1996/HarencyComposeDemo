package com.softradix.jetpackcomposedemo.database.entity

import androidx.room.*


@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val name: String,
    val age: Int
)

@Entity
data class Playlist(
    @PrimaryKey
    val playlistId: Int,
    val userCreatorId: Int,
    val playlistName: String
)

@Entity
data class Song(
    @PrimaryKey
    val songId: Int,
    val songName: String,
    val artist: String
)


@Entity(primaryKeys = ["playlistId", "songId"])
data class PlaylistSongCrossRef(
    val playlistId: Int,
    val songId: Int
)


data class PlaylistWithSongs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val song: List<Song>
)


data class UserWithPlaylistAndSongs(
    @Embedded val user: User,
    @Relation(
        entity = Playlist::class,
        parentColumn = "userId",
        entityColumn = "userCreatorId"
    )
    val playlists: List<PlaylistWithSongs>
)

