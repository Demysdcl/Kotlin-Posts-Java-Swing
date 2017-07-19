package repository

import entity.FullParameters
import entity.HttpResponse
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

abstract class BaseRepository {

    /**
     * Faz a execução a chamada para a API
     * Todos os parâmetros são configurados dentro da classe FullParameters
     */
    fun execute(fullParameters: FullParameters): HttpResponse {

        val conn: HttpURLConnection
        val response: HttpResponse

        val url: URL = URL(fullParameters.url + getQuery(fullParameters.parameters))

        conn = url.openConnection() as HttpURLConnection
        conn.readTimeout = 100000
        conn.connectTimeout = 120000
        conn.requestMethod = fullParameters.method.toString()

        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.setRequestProperty("charset", "utf-8")

        conn.useCaches = false

        // Faz a requisição
        conn.connect()

        // Caso seja erro 404, não existe input strem fazendo com ocorra exception - 200 é sucesso
        if (conn.responseCode == 404) {
            response = HttpResponse(conn.responseCode, "")
        } else {

            // Lê conteúdo e retorna json como string
            val inputStream: InputStream = conn.inputStream
            response = HttpResponse(conn.responseCode, getStringFromInputStream(inputStream))

        }

        // Faz o retorno tratado
        return response

    }

    /**
     * Faz a conversão do retorno do webservice para string
     */
    fun getStringFromInputStream(inputStream: InputStream): String {

        try {

            val strBuilder: StringBuilder = StringBuilder()
            val br: BufferedReader = BufferedReader(InputStreamReader(inputStream))

            for (line in br.readLines()) {
                strBuilder.append(line)
            }

            return strBuilder.toString()

        } catch (e: Exception) {
            return ""
        }

    }

    /**
     * Monta query
     */
    fun getQuery(parameters: Map<String, String>): String {

        // www.api.com/post?id=10&size=10&page=3

        if (parameters.isEmpty())
            return ""

        val result: StringBuilder = StringBuilder()
        var first: Boolean = true

        for (param in parameters) {
            if (first) {
                result.append("?")
                first = false
            } else {
                result.append("&")
            }

            result.append(URLEncoder.encode(param.key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(param.value, "UTF-8"))
        }

        return result.toString()

    }

}