package com.mikedg.thepinballapp.data.remote

import com.mikedg.thepinballapp.data.model.ChangeLog
import com.mikedg.thepinballapp.data.model.Machine
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class OpdbApiService() {
    interface OpdbApi {
        @GET("/api/changelog")
        suspend fun getChangelog(@Query("from") from: String?): List<ChangeLog>

        @GET("/api/machines/{opdb_id}")
        suspend fun getMachineInfo(
            @Path("opdb_id") opdbId: String,
            @Query("api_token") apiToken: String
        ): Machine
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://opdb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OpdbApi::class.java)

    suspend fun fetchChangeLogs(from: String? = null): List<ChangeLog> {
        return api.getChangelog(from)
    }

    suspend fun fetchMachine(opdbId: String, apiToken: String): Machine {
        return api.getMachineInfo(opdbId, apiToken)
    }
}