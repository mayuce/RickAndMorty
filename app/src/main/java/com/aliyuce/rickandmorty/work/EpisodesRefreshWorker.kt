package com.aliyuce.rickandmorty.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.aliyuce.rickandmorty.features.episodes.domain.GetEpisodesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * Worker that refreshes the episodes list in the background by invoking the GetEpisodesUseCase.
 * This worker will attempt to call page=1 to refresh the first page and update the cache/db.
 */
@Suppress("ReturnCount")
@HiltWorker
class EpisodesRefreshWorker
    @AssistedInject
    constructor(
        @Assisted private val context: Context,
        @Assisted params: WorkerParameters,
        private val getEpisodesUseCase: GetEpisodesUseCase,
    ) : CoroutineWorker(context, params) {
        override suspend fun doWork(): Result {
            return try {
                val firstResult = getEpisodesUseCase(1)
                if (firstResult.isFailure) return Result.retry()

                val firstPage = firstResult.getOrNull()
                val totalPages = firstPage?.pages ?: 1

                for (page in 2..totalPages) {
                    val res = getEpisodesUseCase(page)
                    if (res.isFailure) {
                        return Result.retry()
                    }
                }

                Result.success()
            } catch (e: Exception) {
                Log.e("EpisodesRefreshWorker", "Error refreshing episodes", e)
                Result.retry()
            }
        }

        companion object {
            const val UNIQUE_WORK_NAME = "episodes_refresh_work"

            fun schedulePeriodicWork(
                context: Context,
                hours: Long = 6,
            ) {
                val constraints =
                    Constraints
                        .Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                val request =
                    PeriodicWorkRequestBuilder<EpisodesRefreshWorker>(hours, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .build()

                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    UNIQUE_WORK_NAME,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    request,
                )
            }

            fun cancelPeriodicWork(context: Context) {
                WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_NAME)
            }
        }
    }
