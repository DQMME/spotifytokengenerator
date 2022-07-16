package dataclass

object SpotifyScope {
    //Images
    const val UGC_IMAGE_UPLOAD = "ugc-image-upload"

    //Spotify Connect
    const val USER_MODIFY_PLAYBACK_STATE = "user-modify-playback-state"
    const val USER_READ_PLAYBACK_STATE = "user-read-playback-state"
    const val USER_READ_CURRENTLY_PLAYING = "user-read-currently-playing"

    //Follow
    const val USER_FOLLOW_MODIFY = "user-follow-modify"
    const val USER_FOLLOW_READ = "user-follow-read"

    //Listening History
    const val USER_READ_RECENTLY_PLAYED = "user-read-recently-played"
    const val USER_READ_PLAYBACK_POSITION = "user-read-playback-position"
    const val USER_TOP_READ = "user-top-read"

    //Playlists
    const val PLAYLIST_READ_COLLABORATIVE = "playlist-read-collaborative"
    const val PLAYLIST_MODIFY_PUBLIC = "playlist-modify-public"
    const val PLAYLIST_READ_PRIVATE = "playlist-read-private"
    const val PLAYLIST_MODIFY_PRIVATE = "playlist-modify-private"

    //Playback
    const val APP_REMOTE_CONTROL = "app-remote-control"
    const val STREAMING = "streaming"

    //Users
    const val USER_READ_EMAIL = "user-read-email"
    const val USER_READ_PRIVATE = "user-read-private"

    //Library
    const val USER_LIBRARY_MODIFY = "user-library-modify"
    const val USER_LIBRARY_READ = "user-library-read"

    fun allScopes() = mutableListOf(
        UGC_IMAGE_UPLOAD,
        USER_MODIFY_PLAYBACK_STATE,
        USER_READ_PLAYBACK_STATE,
        USER_READ_CURRENTLY_PLAYING,
        USER_FOLLOW_MODIFY,
        USER_FOLLOW_READ,
        USER_READ_RECENTLY_PLAYED,
        USER_READ_PLAYBACK_POSITION,
        USER_TOP_READ,
        PLAYLIST_READ_COLLABORATIVE,
        PLAYLIST_MODIFY_PUBLIC,
        PLAYLIST_READ_PRIVATE,
        PLAYLIST_MODIFY_PRIVATE,
        APP_REMOTE_CONTROL,
        STREAMING,
        USER_READ_EMAIL,
        USER_READ_PRIVATE,
        USER_LIBRARY_MODIFY,
        USER_LIBRARY_READ
    )

    fun descriptions() = hashMapOf(
        UGC_IMAGE_UPLOAD to "Write access to user-provided images.",
        USER_MODIFY_PLAYBACK_STATE to "Write access to a user’s playback state",
        USER_FOLLOW_MODIFY to "Write/delete access to the list of artists and other users that the user follows.",
        USER_READ_RECENTLY_PLAYED to "Read access to a user’s recently played tracks.",
        USER_READ_PLAYBACK_POSITION to "Read access to a user’s playback position in a content.",
        PLAYLIST_READ_COLLABORATIVE to "Include collaborative playlists when requesting a user's playlists.",
        APP_REMOTE_CONTROL to "Remote control playback of Spotify. This scope is currently available to Spotify iOS and Android SDKs.",
        USER_READ_PLAYBACK_STATE to "Read access to a user’s player state.",
        USER_READ_EMAIL to "Read access to user’s email address.",
        STREAMING to "Control playback of a Spotify track. This scope is currently available to the Web Playback SDK. The user must have a Spotify Premium account.",
        USER_TOP_READ to "Read access to a user's top artists and tracks.",
        PLAYLIST_MODIFY_PUBLIC to "Write access to a user's public playlists.",
        USER_LIBRARY_MODIFY to "Write/delete access to a user's \"Your Music\" library.",
        USER_FOLLOW_READ to "Read access to the list of artists and other users that the user follows.",
        USER_READ_CURRENTLY_PLAYING to "Read access to a user’s currently playing content.",
        USER_LIBRARY_READ to "Read access to a user's library.",
        PLAYLIST_READ_PRIVATE to "Read access to user's private playlists.",
        USER_READ_PRIVATE to "Read access to user’s subscription details (type of user account).",
        PLAYLIST_MODIFY_PRIVATE to "Write access to a user's private playlists."
    )

    fun getDescription(scope: String): String? = descriptions().getOrElse(scope) { null }
}
