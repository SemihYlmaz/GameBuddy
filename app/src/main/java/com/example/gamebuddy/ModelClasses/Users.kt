package com.example.gamebuddy.ModelClasses

class Users {
    private var uid: String = ""
    private var username: String = ""
    private var profile: String = ""
    private var cover: String = ""
    private var status: String = ""
    private var search: String = ""
    private var steam: String = ""
    private var reddit: String = ""
    private var twitch: String = ""

    constructor()


    constructor(
        uid: String,
        username: String,
        profile: String,
        cover: String,
        status: String,
        search: String,
        steam: String,
        reddit: String,
        twitch: String
    ) {
        this.uid = uid
        this.username = username
        this.profile = profile
        this.cover = cover
        this.status = status
        this.search = search
        this.steam = steam
        this.reddit = reddit
        this.twitch = twitch
    }

    fun getUID(): String?{
        return uid
    }
    fun setUID(uid: String){
        this.uid = uid
    }
     fun getUserName(): String?{
        return username
    }
    fun setUserName(username: String){
        this.username = username
    }
     fun getProfile(): String?{
        return profile
    }
    fun setProfile(profile: String){
        this.profile = profile
    }
     fun getCover(): String?{
        return cover
    }
    fun setCover(cover: String){
        this.cover = cover
    }
     fun getStatus(): String?{
        return status
    }
    fun setStatus(status: String){
        this.status = status
    }
     fun getSearch(): String?{
        return search
    }
    fun setSearch(search: String){
        this.search = search
    }
     fun getSteam(): String?{
        return steam
    }
    fun setSteam(steam: String){
        this.steam = steam
    }
     fun getReddit(): String?{
        return reddit
    }
    fun setReddit(reddit: String){
        this.reddit = reddit
    }
     fun getTwitch(): String?{
        return twitch
    }
    fun setTwitch(twitch: String){
        this.twitch = twitch
    }
}
