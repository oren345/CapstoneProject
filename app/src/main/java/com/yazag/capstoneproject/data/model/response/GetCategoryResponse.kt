package com.yazag.capstoneproject.data.model.response

data class GetCategoryResponse(
    val category: List<Category>?
): BaseResponse()
