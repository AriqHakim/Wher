package id.emergence.wher.data.remote

import id.emergence.wher.data.remote.json.FriendLocationResponse
import id.emergence.wher.data.remote.json.FriendRequestBody
import id.emergence.wher.data.remote.json.FriendRequestResponse
import id.emergence.wher.data.remote.json.LocationBody
import id.emergence.wher.data.remote.json.LoginBody
import id.emergence.wher.data.remote.json.MessageResponse
import id.emergence.wher.data.remote.json.PaginationResponse
import id.emergence.wher.data.remote.json.RegisterBody
import id.emergence.wher.data.remote.json.TokenResponse
import id.emergence.wher.data.remote.json.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /*
    Auth
     */
    // #1
    @POST("login")
    suspend fun login(
        @Body body: LoginBody,
    ): Response<TokenResponse>

    // #2
    @POST("register")
    suspend fun register(
        @Body body: RegisterBody,
    ): Response<MessageResponse>

    /*
    Profile
     */
    // #4
    @GET("profile")
    suspend fun fetchProfile(
        @Header("Authorization") token: String,
    ): Response<UserResponse>

    // #5
    @JvmSuppressWildcards
    @Multipart
    @PUT("profile")
    suspend fun editProfile(
        @Header("Authorization") token: String,
        @PartMap map: Map<String, RequestBody>,
        @Part photo: MultipartBody.Part,
    ): Response<MessageResponse>

    // #6
    @GET("profile/{id}")
    suspend fun fetchProfile(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<UserResponse>

    // #14
    @DELETE("profile/remove")
    suspend fun deleteAccount(
        @Header("Authorization") token: String,
    ): Response<MessageResponse>

    /*
    Friends
     */
    // #7
    @GET("users")
    suspend fun searchUsers(
        @Header("Authorization") token: String,
        @Query("q") q: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10,
    ): Response<PaginationResponse<UserResponse>>

    // #10
    @GET("friends/request")
    suspend fun fetchFriendRequest(
        @Header("Authorization") token: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10,
    ): Response<PaginationResponse<FriendRequestResponse>>

    // #9
    @POST("friends/request/{id}")
    suspend fun requestFriend(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body body: FriendRequestBody,
    ): Response<MessageResponse>

    // #11
    @POST("friends/request/{id}/accept")
    suspend fun acceptFriendRequest(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<MessageResponse>

    @POST("friends/request/{id}/reject")
    suspend fun rejectFriendRequest(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<MessageResponse>

    // #12
    @GET("friends")
    suspend fun fetchFriends(
        @Header("Authorization") token: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10,
    ): Response<PaginationResponse<UserResponse>>

    // #13
    @DELETE("friends/{id}/remove")
    suspend fun removeFriend(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<MessageResponse>

    /*
    Locations
     */
    // #15
    @GET("locations")
    suspend fun fetchLocations(
        @Header("Authorization") token: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10,
    ): Response<PaginationResponse<FriendLocationResponse>>

    // #16
    @PUT("locations")
    suspend fun updateLocation(
        @Header("Authorization") token: String,
        @Body body: LocationBody,
    ): Response<MessageResponse>
}
