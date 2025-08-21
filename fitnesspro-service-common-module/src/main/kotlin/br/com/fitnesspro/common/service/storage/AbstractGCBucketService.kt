package br.com.fitnesspro.common.service.storage

import br.com.fitnesspro.common.cloud.credentials.CloudCredentials
import br.com.fitnesspro.common.cloud.enums.EnumGCBucketNames
import br.com.fitnesspro.common.service.storage.result.StorageUploadResult
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.StorageModel
import br.com.fitnesspro.shared.communication.enums.storage.EnumGCBucketContentTypes
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import java.io.InputStream
import java.util.concurrent.TimeUnit

abstract class AbstractGCBucketService {

    open fun getUrlExpirationTime(): Long = 1

    open fun getUrlExpirationTimeUnit(): TimeUnit = TimeUnit.DAYS

    protected fun getStorageService(): Storage {
        return StorageOptions.newBuilder()
            .setCredentials(GoogleCredentials.fromStream(CloudCredentials.getCredentialsInputStream()))
            .build()
            .service
    }

    fun uploadFile(
        bucketName: EnumGCBucketNames,
        fileName: String,
        contentType: EnumGCBucketContentTypes,
        fileStream: InputStream,
    ): StorageUploadResult {
        val storage = getStorageService()

        val blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName.value, fileName))
            .setContentType(contentType.value)
            .build()

        storage.create(blobInfo, fileStream.readBytes())

        val uri = storage.signUrl(
            blobInfo,
            getUrlExpirationTime(),
            getUrlExpirationTimeUnit(),
            Storage.SignUrlOption.withV4Signature()
        ).toURI()

        return StorageUploadResult(
            uri = uri,
            expiration = getUrlExpirationTime(),
            expirationUnit = getUrlExpirationTimeUnit()
        )
    }

    protected fun writeDefaultFieldsStorageModelAfterUpload(model: StorageModel, uploadResult: StorageUploadResult) {
        model.storageTransmissionDate = dateTimeNow()
        model.storageUrl = uploadResult.uri.toString()
        model.storageUrlExpiration = uploadResult.expiration
        model.expirationUnit = uploadResult.expirationUnit
    }

    fun deleteFile(bucketName: EnumGCBucketNames, fileName: String): Boolean {
        return getStorageService().delete(bucketName.value, fileName)
    }
}