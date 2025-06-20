package com.dipuguide.finslice.data.model

import com.google.firebase.firestore.DocumentId

data class AuthUser(
    @DocumentId
    val id: String? = null,
    val name: String,
    val email: String,
    val password: String,
) {
    constructor() : this(null, "", "", "")
}
