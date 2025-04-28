package br.com.fitnesspro.service.config.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfig {

    @PostConstruct
    fun initializeFirebase() {
        val serviceAccount = getCredentialsInputStream()

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
    }

    private fun getCredentialsInputStream(): InputStream {
        val file = File("serviceAccountKey.json")

        return if (file.exists()) {
            FileInputStream(file)
        } else {
            val json = System.getenv("CLOUD_CREDENTIALS")
            ByteArrayInputStream(json.toByteArray(Charsets.UTF_8))
        }
    }
}