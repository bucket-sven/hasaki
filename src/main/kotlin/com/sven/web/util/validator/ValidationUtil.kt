package com.sven.web.util.validator

import javax.validation.Validation

class ValidationResult {
    var hasErrors: Boolean = false
    var errorMsg: Map<String, String>? = null
}

object ValidationUtil {
//    val validator = Validation.byProvider(HibernateValidator::class.java).configure().failFast(true).buildValidatorFactory().validator
    val validator = Validation.buildDefaultValidatorFactory().validator
    inline fun <reified T>validateEntity(obj: T): ValidationResult {
        val result = ValidationResult()
        val set = validator.validate(obj)
        if (set != null && set.size != 0) {
            result.hasErrors = true
            val errorMsg = hashMapOf<String, String>()
            for (cv in set) {
                errorMsg[cv.propertyPath.toString()] = cv.message
            }
            result.errorMsg = errorMsg
        }
        return result
    }
}