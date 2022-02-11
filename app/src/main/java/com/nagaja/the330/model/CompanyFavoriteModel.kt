package com.nagaja.the330.model

class CompanyFavoriteModel(
    var userFollowId: UserFollowIdModel? = null,
    var actor: UserDetail? = null,
    var target: CompanyModel? = null,
    var bookmark: Int? = null,
    var useCount: Int? = null
)