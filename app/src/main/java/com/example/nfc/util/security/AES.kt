package com.example.nfc.util.security

import android.content.Context
import android.util.Base64
import org.bouncycastle.crypto.BufferedBlockCipher
import org.bouncycastle.crypto.CipherParameters
import org.bouncycastle.crypto.engines.AESEngine
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.util.Arrays
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.security.SecureRandom

class AES(var context: Context) {
    /**
     * Demostración de cifrado AES con Bouncy Castle
     */
    // Creamos un "motor" AES con un tamaño de bloque de 16 bytes
    val blockSize = 16

    /**
     * Gestiona la creación de una clave AES y la almacena en un archivo en
     * formato Hexadecimal para que sea legible al abrirlo
     */
    fun doGenerateKey() {
        val key = generateKeyAndIV()
        if (key != null) {
            println(
                "Clave generada:"
                        + java.lang.String(
                    Base64.encodeToString(
                        Arrays.copyOfRange(key, 0, 16),
                        Base64.NO_WRAP
                    )
                ) as String
            )
            println(
                "IV generado:"
                        + java.lang.String(
                    Base64.encodeToString(
                        Arrays.copyOfRange(
                            key, 16,
                            blockSize + 16
                        ), Base64.NO_WRAP
                    )
                ) as String
            )
            val ficheroPublica = File(context.filesDir, "aeskeyiv")
            //Escribo en el fichero que guarda la clave privada
            try {
                val ficheroPublicaEscritura = PrintWriter(FileWriter(ficheroPublica))
                val keyOnly = Arrays.copyOfRange(key, 0, 16)
                ficheroPublicaEscritura.println(Base64.encodeToString(keyOnly, Base64.NO_WRAP))
                ficheroPublicaEscritura.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Gestiona el cifrado de un archivo txt (leyendo tambien el archivo de la
     * clave AES)
     */
    @Throws(IOException::class)
    fun doEncrypt(text: ByteArray): ByteArray {
        val ficheroClave = File(context.filesDir, "aeskeyiv")
        val lectorClave = BufferedReader(FileReader(ficheroClave))
        val key = Base64.decode(lectorClave.readLine(), Base64.NO_WRAP)
        var texto = "ERROR".toByteArray()
        if (key != null) {
            val res = encrypt(
                text,
                Arrays.copyOfRange(key, 0, 16),
            )
            if (res != null) {
                texto = res
            }
        }
        return texto
    }

    /**
     * Gestiona el descifrado de un archivo (leyendo tambien el archivo de la
     * clave AES)
     */
    @Throws(IOException::class)
    fun doDecrypt(text: ByteArray?): ByteArray? {
        if (text == null) {
            return null
        }
        val ficheroClave = File(context.filesDir, "aeskeyiv")
        val lectorClave = BufferedReader(FileReader(ficheroClave))
        val key = Base64.decode(lectorClave.readLine(), Base64.NO_WRAP)
        return if (key != null) {
            decrypt(text, key)
        } else null
    }

    /**
     * Genera una Clave e IV para el cifrado AES a partir de un número aleatorio
     * "seguro"
     *
     * @return 16+blocksize bytes (Clave+IV)
     */
    fun generateKeyAndIV(): ByteArray? {
        // Usamos el generador de números aleatorios para criptografía
        var sr: SecureRandom? = null
        try {
            sr = SecureRandom()
            // Lo inicializamos con una semilla
            sr.setSeed("UCTresM.".toByteArray())
        } catch (e: Exception) {
            System.err
                .println("Ha ocurrido un error generando el número aleatorio")
            return null
        }
        // Lo generamos del tamaño que necesitamos (16 bytes de clave + tamaño de bloque como IV)
        return sr.generateSeed(16 + blockSize + 10)
    }

    companion object {
        /**
         * Cifra/Descifra datos con el algoritmo AES. Al ser un algoritmo de cifrado
         * simétrico se puede usar para ambos procesos
         *
         * @param cipher Cifrador/Descifrador AES
         * @param data   Datos origen
         * @return Datos destino
         * @throws Exception
         */
        @Throws(Exception::class)
        private fun cipherData(
            cipher: BufferedBlockCipher,
            data: ByteArray
        ): ByteArray {
            // Creamos un array de bytes del tamaño estimado de descifrado
            val minSize = cipher.getOutputSize(data.size)
            val outBuf = ByteArray(minSize)
            // Procesamos todos los bytes de los datos
            val length1 = cipher.processBytes(data, 0, data.size, outBuf, 0)
            // Realizamos el procesamiento final (conceptualmente, es como el flush de los streams)
            val length2 = cipher.doFinal(outBuf, length1)
            val actualLength = length1 + length2
            val result = ByteArray(actualLength)
            // Copiamos el resultado y lo devolvemos
            System.arraycopy(outBuf, 0, result, 0, result.size)
            return result
        }

        /**
         * Descifra datos usando el algoritmo AES
         * Tanto el método de cifrado como el de descifrado se pueden realizar como uno sólo,
         * pero se mantienen separados por claridad del código para el alumno
         *
         * @param ciphered Datos cifrados
         * @param key      Clave (24 bytes)
         * @param iv       Vector de Inicialización (Tamaño en bytes del bloque)
         * @return Datos descifrados
         */
        private fun decrypt(ciphered: ByteArray, key: ByteArray): ByteArray? {
            return try {
                // Creamos el cifrador
                val aes = AESEngine()
                val cipher = BufferedBlockCipher(aes)

                // Procesamos la clave
                val keyParam: CipherParameters = KeyParameter(key)
                aes.init(false, keyParam)
                cipherData(cipher, ciphered)
            } catch (e: Exception) {
                println(
                    "Ha ocurrido un error al intentar descifrar el texto:"
                            + e
                )
                null
            }
        }

        /**
         * Cifra datos usando el algoritmo AES
         *
         * @param plain Datos a cifrar
         * @param key   Clave (16 bytes)
         * @param iv    Vector de Inicialización (Tamaño en bytes del bloque)
         * @return Datos cifrados
         */
        private fun encrypt(plain: ByteArray, key: ByteArray): ByteArray? {
            return try {
                // Creamos el cifrador
                val aes = AESEngine()
                val cipher = BufferedBlockCipher(aes)

                // Procesamos la clave
                val keyParam: CipherParameters = KeyParameter(key)
                aes.init(true, keyParam)
                cipherData(cipher, plain)
            } catch (e: Exception) {
                println(
                    "Ha ocurrido un error al intentar cifrar el texto:"
                            + e
                )
                null
            }
        }
    }
}
