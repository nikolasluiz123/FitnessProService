package br.com.fitnesspro.common.service.storage

import br.com.fitnesspro.common.cloud.credentials.CloudCredentials
import br.com.fitnesspro.shared.communication.enums.storage.EnumGCBucketContentTypes
import br.com.fitnesspro.common.cloud.enums.EnumGCBucketNames
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import java.io.InputStream

abstract class AbstractGCBucketService {

    protected fun getStorageService(): Storage {
        return StorageOptions.newBuilder()
            .setCredentials(GoogleCredentials.fromStream(CloudCredentials.getCredentialsInputStream()))
            .build()
            .service
    }

    fun uploadFile(bucketName: EnumGCBucketNames, fileName: String, contentType: EnumGCBucketContentTypes, fileStream: InputStream) {
        val blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName.value, fileName))
            .setContentType(contentType.value)
            .build()

        getStorageService().create(blobInfo, fileStream.readBytes())
    }

    fun deleteFile(bucketName: EnumGCBucketNames, fileName: String): Boolean {
        return getStorageService().delete(bucketName.value, fileName)
    }
}