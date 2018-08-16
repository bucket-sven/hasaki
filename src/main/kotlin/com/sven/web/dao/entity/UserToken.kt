package com.sven.web.dao.entity

import com.baomidou.mybatisplus.annotations.TableField
import com.baomidou.mybatisplus.annotations.TableId
import com.baomidou.mybatisplus.annotations.TableName
import com.baomidou.mybatisplus.enums.FieldFill
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
        @TableField(update = "now()", fill = FieldFill.INSERT_UPDATE)
        var updateTime: Date? = null
)