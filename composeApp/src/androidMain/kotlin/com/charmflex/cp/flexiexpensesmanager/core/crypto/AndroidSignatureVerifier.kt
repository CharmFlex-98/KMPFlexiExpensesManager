package com.charmflex.cp.flexiexpensesmanager.core.crypto

import com.charmflex.cp.flexiexpensesmanager.core.app.AppConfigProvider
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AssetReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.annotation.Singleton
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

@Singleton
internal class AndroidSignatureVerifier(
    private val base64Manager: Base64Manager,
    assetReader: AssetReader,
    appConfigProvider: AppConfigProvider
) : SignatureVerifier {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var publicCert: String

    init {
        scope.launch {
            publicCert = assetReader.read(appConfigProvider.signaturePemFilePath())
        }
    }


    override fun verify(payload: String, signature: String): Boolean {
        val publicKey = loadPublicKey()
        return verifySignature(payload, signature, publicKey)
    }

    private fun loadPublicKey(): PublicKey {
        val clean = publicCert
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "")
        val decoded = base64Manager.decode(clean.toByteArray())
        val spec = X509EncodedKeySpec(decoded)
        val publicKey = KeyFactory.getInstance("RSA").generatePublic(spec)
        return publicKey
    }


    private fun verifySignature(payload: String, signature: String, publicKey: PublicKey): Boolean {
        val sig = Signature.getInstance("SHA256withRSA")
        sig.initVerify(publicKey)
        sig.update(payload.toByteArray(Charsets.UTF_8))
        val signatureBytes = base64Manager.decode(signature.toByteArray())
        return sig.verify(signatureBytes)
    }
}