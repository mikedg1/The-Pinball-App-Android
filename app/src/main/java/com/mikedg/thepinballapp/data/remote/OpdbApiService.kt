package com.mikedg.thepinballapp.data.remote

import com.mikedg.thepinballapp.BuildConfig
import com.mikedg.thepinballapp.data.model.opdb.ChangeLog
import com.mikedg.thepinballapp.data.model.opdb.Machine
import com.mikedg.thepinballapp.data.model.opdb.TypeAheadSearchResult
import com.mikedg.thepinballapp.util.ApiResult
import com.mikedg.thepinballapp.util.safeApiCall
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

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

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://opdb.org/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val api = retrofit.create(OpdbApi::class.java)

    /**
     * Fetches change logs from the OPDB API.
     * @param from Optional parameter to specify the starting point for change logs.
     * @return ApiResult containing the list of change logs or an error.
     */
    suspend fun fetchChangeLogs(from: String? = null): ApiResult<List<ChangeLog>> {
        return safeApiCall { api.getChangelog(from) }
    }

    /**
     * Fetches machine details from the OPDB API.
     * @param opdbId The ID of the machine to fetch.
     * @param apiToken The API token to use for authentication.
     * @return ApiResult containing the machine details or an error.
     */
    suspend fun fetchMachine(opdbId: String, apiToken: String = this.apiToken): ApiResult<Machine> {
        return safeApiCall { api.getMachineInfo(opdbId, apiToken) }
    }

    /**
     * Searches for machines using the typeahead API.
     * @param query The search query.
     * @param includeGroups Whether to include groups in the search results.
     * @param includeAliases Whether to include aliases in the search results.
     * @return ApiResult containing the list of typeahead search results or an error.
     */
    suspend fun searchTypeAhead(
        query: String,
        includeGroups: Boolean = false,
        includeAliases: Boolean = true
    ): ApiResult<List<TypeAheadSearchResult>> {
        return safeApiCall {
            api.searchTypeahead(
                query = query,
                includeGroups = if (includeGroups) 1 else 0,
                includeAliases = if (includeAliases) 1 else 0
            )
        }
    }

    /**
     * Searches for machines using the full search API.
     * @param query The search query.
     * @param apiToken The API token to use for authentication.
     * @param requireOpdb Whether to require OPDB IDs in the search results.
     * @param includeGroups Whether to include groups in the search results.
     * @param includeAliases Whether to include aliases in the search results.
     * @param includeGroupingEntries Whether to include grouping entries in the search results.
     * @return ApiResult containing the list of machines or an error.
     */
    suspend fun search(
        query: String,
        apiToken: String = this.apiToken,
        requireOpdb: Boolean = true,
        includeGroups: Boolean = false,
        includeAliases: Boolean = true,
        includeGroupingEntries: Boolean = false
    ): ApiResult<List<Machine>> {
        return safeApiCall {
            api.searchMachines(
                apiToken = apiToken,
                query = query,
                requireOpdb = if (requireOpdb) 1 else 0,
                includeGroups = if (includeGroups) 1 else 0,
                includeAliases = if (includeAliases) 1 else 0,
                includeGroupingEntries = if (includeGroupingEntries) 1 else 0
            )
        }
    }
}