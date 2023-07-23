package com.betterhackernews.domain

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    //The type of item. One of "job", "story", "comment", "poll", or "pollopt".
    val type: String? = null,
    val dead: Boolean? = null,
    val deleted: Boolean? = null,
    val by: String? = null,
    //In the case of stories or polls, the total comment count.
    val descendants: Int? = null,
    //The item's unique id.
    val id: Int? = null,
    //The ids of the item's comments, in ranked display order.
    val kids: List<Int> = listOf<Int>(),
    //The comment's parent: either another comment or the relevant story.
    val parent: Int? = null,
    //The comment, story or poll text. HTML.
    val text: String? = null,
    val score: Int? = null,
    val time: Long? = null,
    val title: String? = null,
    val url: String? = null,
    //Custom field for each story, loads the comments
    var topComments: List<Item>? = listOf()
)