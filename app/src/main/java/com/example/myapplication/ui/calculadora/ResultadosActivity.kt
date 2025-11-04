package com.example.myapplication.ui.calculadora

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class ResultadosActivity : AppCompatActivity() {

    private lateinit var etNomeCompleto: EditText
    private lateinit var etWhatsapp: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNumeroProcesso: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        val dataTermino = intent.getStringExtra("dataTermino")
        val dataProgressao = intent.getStringExtra("dataProgressao")
        val dataLivramento = intent.getStringExtra("dataLivramento")

        findViewById<TextView>(R.id.tvTerminoPena).text = dataTermino
        findViewById<TextView>(R.id.tvProgressaoRegime).text = dataProgressao
        findViewById<TextView>(R.id.tvLivramentoCondicional).text = dataLivramento

        etNomeCompleto = findViewById(R.id.etNomeCompleto)
        etWhatsapp = findViewById(R.id.etWhatsapp)
        etEmail = findViewById(R.id.etEmail)
        etNumeroProcesso = findViewById(R.id.etNumeroProcesso)

        etWhatsapp.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private var old = ""

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                old = s.toString()
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                val str = s.toString().replace(Regex("[^\\d]"), "")
                var formatted = ""
                if (str.length > 2) {
                    formatted = "(${str.substring(0, 2)}) "
                    if (str.length > 7) {
                        formatted += "${str.substring(2, 7)}-${str.substring(7)}"
                    } else {
                        formatted += str.substring(2)
                    }
                } else {
                    formatted = str
                }

                isUpdating = true
                etWhatsapp.setText(formatted)
                etWhatsapp.setSelection(formatted.length)
            }
        })

        etNumeroProcesso.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val mask = "NNNNNNN-DD.AAAA.J.TR.OOOO"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                var str = s.toString().replace(Regex("[^\\d]"), "")
                var formatted = ""
                var i = 0
                mask.forEach { m ->
                    if (i >= str.length) return@forEach
                    if (m == 'N' || m == 'D' || m == 'A' || m == 'J' || m == 'T' || m == 'R' || m == 'O') {
                        formatted += str[i]
                        i++
                    } else if (str.length > i) {
                        formatted += m
                    } else {
                        return@forEach
                    }
                }

                isUpdating = true
                etNumeroProcesso.setText(formatted)
                etNumeroProcesso.setSelection(formatted.length)
            }
        })

        findViewById<Button>(R.id.btnVoltar).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnSalvarEnviar).setOnClickListener {
            if (validarCampos()) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"

                val headerCalculo = getString(R.string.share_header_calculo)
                val dataTerminoText = intent.getStringExtra("dataTermino")
                val dataProgressaoText = intent.getStringExtra("dataProgressao")
                val dataLivramentoText = intent.getStringExtra("dataLivramento")

                val headerDados = getString(R.string.share_header_dados)
                val nome = "${getString(R.string.share_label_nome)}: ${etNomeCompleto.text}"
                val whatsapp = "${getString(R.string.share_label_whatsapp)}: ${etWhatsapp.text}"
                val email = "${getString(R.string.share_label_email)}: ${etEmail.text}"
                val processo = "${getString(R.string.share_label_processo)}: ${etNumeroProcesso.text}"

                val shareBody = """
                $headerCalculo
                - $dataTerminoText
                - $dataProgressaoText
                - $dataLivramentoText

                $headerDados
                - $nome
                - $whatsapp
                - $email
                - $processo
                """.trimIndent()

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
            }
        }

        findViewById<Button>(R.id.btnFalarAdvogado).setOnClickListener {
            val phoneNumber = "5511989498044"
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.whatsapp_error), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validarCampos(): Boolean {
        if (TextUtils.isEmpty(etNomeCompleto.text)) {
            etNomeCompleto.error = getString(R.string.campo_obrigatorio)
            return false
        }

        val whatsapp = etWhatsapp.text.toString().replace(Regex("[^\\d]"), "")
        if (TextUtils.isEmpty(whatsapp)) {
            etWhatsapp.error = getString(R.string.campo_obrigatorio)
            return false
        } else if (whatsapp.length != 11) {
            etWhatsapp.error = getString(R.string.whatsapp_invalido)
            return false
        }

        val email = etEmail.text.toString()
        if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = getString(R.string.email_invalido)
            return false
        }

        val numeroProcesso = etNumeroProcesso.text.toString()
        if (!TextUtils.isEmpty(numeroProcesso) && numeroProcesso.length < 25) {
            etNumeroProcesso.error = getString(R.string.processo_invalido)
            return false
        }

        return true
    }
}
