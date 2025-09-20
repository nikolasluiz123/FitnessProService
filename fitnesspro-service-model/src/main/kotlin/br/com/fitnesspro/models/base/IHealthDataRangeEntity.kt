package br.com.fitnesspro.models.base

import java.time.Instant

interface IHealthDataRangeEntity : IHealthDataCollected {
    val rangeStartTime: Instant
    val rangeEndTime: Instant?
}