package com.sven.web.dao.entity

import com.baomidou.mybatisplus.annotations.TableField
import com.baomidou.mybatisplus.annotations.TableId
import com.baomidou.mybatisplus.annotations.TableName
import java.util.*

@TableName("user_token")
data class UserToken (
        @TableId
        var id: Long? = null,
        @TableField
        var userId: Long? = null,
        @TableField
        var token: String? = null,
        @TableField
        var createTime: Date? = null,
        @TableField
        var updateTime: Date? = null
)