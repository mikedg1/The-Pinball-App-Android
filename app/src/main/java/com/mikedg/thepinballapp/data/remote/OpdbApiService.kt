package com.mikedg.thepinballapp.data.remote

import com.mikedg.thepinballapp.BuildConfig
import com.mikedg.thepinballapp.data.model.ChangeLog
import com.mikedg.thepinballapp.data.model.Machine
import com.mikedg.thepinballapp.data.model.TypeAheadSearchResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class OpdbApiService(
    private val apiToken: String = BuildConfig.OPDB_API_TOKEN
) {
    interface OpdbApi {
        @GET("/api/changelog")
        suspend fun getChangelog(@Query("from") from: String?): List<ChangeLog>

        @GET("/api/machines/{opdb_id}")
        suspend fun getMachineInfo(
            @Path("opdb_id") opdbId: String,
            @Query("api_token") apiToken: String
        ): Machine

        @GET("/api/search/typeahead")
        suspend fun searchTypeahead(
            @Query("q") query: String,
            @Query("include_groups") includeGroups: Int? = 0,
            @Query("include_aliases") includeAliases: Int? = 1
        ): List<TypeAheadSearchResult>

        @GET("/api/search")
        suspend fun searchMachines(
            @Query("api_token") apiToken: String,
            @Query("q") query: String,
            @Query("require_opdb") requireOpdb: Int? = 1,
            @Query("include_groups") includeGroups: Int? = 0,
            @Query("include_aliases") includeAliases: Int? = 1,
            @Query("include_grouping_entries") includeGroupingEntries: Int? = 0
        ): List<Machine>
    }

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://opdb.org/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val api = retrofit.create(OpdbApi::class.java)

    suspend fun fetchChangeLogs(from: String? = null): List<ChangeLog> {
        return api.getChangelog(from)
    }

    suspend fun fetchMachine(opdbId: String, apiToken: String = this.apiToken): Machine {
        return api.getMachineInfo(opdbId, apiToken)
    }

    suspend fun searchTypeAhead(
        query: String,
        includeGroups: Boolean = false,
        includeAliases: Boolean = true
    ): List<TypeAheadSearchResult> {
        return api.searchTypeahead(
            query = query,
            includeGroups = if (includeGroups) 1 else 0,
            includeAliases = if (includeAliases) 1 else 0
        )
    }

    suspend fun search(
        query: String,
        apiToken: String = this.apiToken,
        requireOpdb: Boolean = true,
        includeGroups: Boolean = false,
        includeAliases: Boolean = true,
        includeGroupingEntries: Boolean = false
    ): List<Machine> {
        return api.searchMachines(
            apiToken = apiToken,
            query = query,
            requireOpdb = if (requireOpdb) 1 else 0,
            includeGroups = if (includeGroups) 1 else 0,
            includeAliases = if (includeAliases) 1 else 0,
            includeGroupingEntries = if (includeGroupingEntries) 1 else 0
        )
    }
}