package com.sven.web.dao.entity

import com.baomidou.mybatisplus.annotations.TableField
import com.baomidou.mybatisplus.annotations.TableId
import com.baomidou.mybatisplus.annotations.TableName
import com.baomidou.mybatisplus.enums.FieldFill
import java.util.*

@TableName("user")
data class User(
        @TableId
        var id: Long? = null,
        @TableField
        var account: String? = null,
        @TableField
        var regType: String? = null,
        @TableField(fill = FieldFill.INSERT)
        var createTime: Date? = null,
        @TableField(update = "now()", fill = FieldFill.INSERT_UPDATE)
        var updateTime: Date? = null
)
