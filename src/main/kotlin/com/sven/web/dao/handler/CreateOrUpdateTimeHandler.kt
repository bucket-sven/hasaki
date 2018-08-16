package com.sven.web.dao.handler

import com.baomidou.mybatisplus.mapper.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import java.sql.Timestamp

class CreateOrUpdateTimeHandler : MetaObjectHandler() {
    override fun insertFill(metaObject: MetaObject?) {
        val createTime = getFieldValByName("createTime", metaObject)
        val updateTime = getFieldValByName("updateTime", metaObject)
        val now = Timestamp(System.currentTimeMillis())
        if (createTime == null) {
            setFieldValByName("createTime", now, metaObject)
        }
        if (updateTime == null) {
            setFieldValByName("updateTime", now, metaObject)
        }
    }

    override fun updateFill(metaObject: MetaObject?) {
        val now = Timestamp(System.currentTimeMillis())
        setFieldValByName("updateTime", now, metaObject)
    }
}