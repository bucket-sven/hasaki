package com.sven.web.dao.mapper

import com.baomidou.mybatisplus.mapper.BaseMapper
import com.sven.web.dao.entity.User
import org.springframework.stereotype.Component

@Component
interface UserMapper : BaseMapper<User>