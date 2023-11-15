package com.yazag.capstoneproject.data.source.remote

import com.yazag.capstoneproject.data.model.request.AddToCartRequest
import com.yazag.capstoneproject.data.model.request.ClearCartRequest
import com.yazag.capstoneproject.data.model.request.DeleteFromCartRequest
import com.yazag.capstoneproject.data.model.response.BaseResponse
import com.yazag.capstoneproject.data.model.response.GetProductDetailResponse
import com.yazag.capstoneproject.data.model.response.GetProductsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {

    @GET("get_products.php")
    suspend fun getProducts(): Response<GetProductsResponse>

    @GET("get_sale_products.php")
    suspend fun getSaleProducts(): Response<GetProductsResponse>

    @GET("get_product_detail.php")
    suspend fun getProductDetail(
        @Query("id") id: Int
    ): Response<GetProductDetailResponse>

    @POST("add_to_cart.php")
    suspend fun addToCart(
        @Body addToCartRequest: AddToCartRequest
    ): Response<BaseResponse>

    @POST("delete_from_cart.php")
    suspend fun deleteFromCart(
        @Body deleteFromRequest: DeleteFromCartRequest
    ): Response<BaseResponse>

    @GET("get_cart_products.php")
    suspend fun getCartProducts(
        @Query("userId") userId: String
    ): Response<GetProductsResponse>

    @POST("clear_cart.php")
    suspend fun clearCart(
        @Body clearCartRequest: ClearCartRequest
    ): Response<BaseResponse>

    @GET("get_products_by_category.php")
    suspend fun getProductsByCategory(
        @Query("category") category: String
    ): Response<GetProductsResponse>

    @GET("search_product.php")
    suspend fun searchProduct(
        @Query("query") query: String
    ): Response<GetProductsResponse>

}