package br.com.fitnesspro.config.gson // Ou onde preferir

import com.google.gson.Gson
import org.springframework.http.converter.json.GsonHttpMessageConverter

class SelectiveGsonHttpMessageConverter(
    gson: Gson,
) : GsonHttpMessageConverter(gson) {

    private val supportedBasePackages: List<String> = listOf("br.com.fitnesspro")

    override fun supports(clazz: Class<*>): Boolean {
        val className = clazz.name

        if (supportedBasePackages.none { className.startsWith(it) }) {
            return false
        }

        return super.supports(clazz)
    }
}