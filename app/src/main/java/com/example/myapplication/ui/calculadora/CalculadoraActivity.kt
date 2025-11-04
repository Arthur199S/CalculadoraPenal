package com.example.myapplication.ui.calculadora

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalculadoraActivity : AppCompatActivity() {

    private val supportedLanguages = listOf("pt", "en", "es", "fr", "de", "ar", "hi", "zh", "ja")

    // Campos de entrada
    private lateinit var etAnos: EditText
    private lateinit var etMeses: EditText
    private lateinit var etDias: EditText
    private lateinit var cbReincidente: CheckBox
    private lateinit var cbViolencia: CheckBox
    private lateinit var cbMorte: CheckBox
    private lateinit var etDataInicio: EditText
    private lateinit var etDiasTrabalhados: EditText
    private lateinit var etHorasEstudo: EditText
    private lateinit var etLeitura: EditText
    private lateinit var etDetracao: EditText
    private lateinit var btnCalcular: Button

    private lateinit var spinnerIdiomas: Spinner

    private lateinit var btnWhatsapp: ImageButton
    private lateinit var btnGmail: ImageButton

    // RadioGroups
    private lateinit var rgRegime: RadioGroup
    private lateinit var rgTipoCrime: RadioGroup

    // Error TextViews
    private lateinit var tvPenaError: TextView
    private lateinit var tvDataError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        loadLanguage()
        setContentView(R.layout.activity_calculadora)
        initViews()
        setupButton()
        setupLanguageSpinner()
        setupContactButtons()
        setupDateInputMask()
    }

    private fun initViews() {
        etAnos = findViewById(R.id.etAnos)
        etMeses = findViewById(R.id.etMeses)
        etDias = findViewById(R.id.etDias)
        cbReincidente = findViewById(R.id.cbReincidente)
        cbViolencia = findViewById(R.id.cbViolencia)
        cbMorte = findViewById(R.id.cbMorte)
        etDataInicio = findViewById(R.id.etDataInicio)
        etDiasTrabalhados = findViewById(R.id.etDiasTrabalhados)
        etHorasEstudo = findViewById(R.id.etHorasEstudo)
        etLeitura = findViewById(R.id.etLeitura)
        etDetracao = findViewById(R.id.etDetracao)
        btnCalcular = findViewById(R.id.btnCalcular)
        spinnerIdiomas = findViewById(R.id.spinnerIdiomas)
        btnWhatsapp = findViewById(R.id.btnWhatsapp)
        btnGmail = findViewById(R.id.btnGmail)

        rgRegime = findViewById(R.id.rgRegime)
        rgTipoCrime = findViewById(R.id.rgTipoCrime)

        tvPenaError = findViewById(R.id.tvPenaError)
        tvDataError = findViewById(R.id.tvDataError)
    }

    private fun setupButton() {
        btnCalcular.setOnClickListener {
            if (validarCampos()) {
                calcularEAbrirResultados()
            }
        }
    }

    private fun validarCampos(): Boolean {
        val anos = etAnos.text.toString().toIntOrNull() ?: 0
        val meses = etMeses.text.toString().toIntOrNull() ?: 0
        val dias = etDias.text.toString().toIntOrNull() ?: 0

        var isValid = true

        if (anos == 0 && meses == 0 && dias == 0) {
            tvPenaError.visibility = View.VISIBLE
            isValid = false
        } else {
            tvPenaError.visibility = View.GONE
        }

        if (rgRegime.checkedRadioButtonId == -1) {
            Toast.makeText(this, getString(R.string.selecione_regime_inicial), Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (rgTipoCrime.checkedRadioButtonId == -1) {
            Toast.makeText(this, getString(R.string.selecione_tipo_crime), Toast.LENGTH_SHORT).show()
            isValid = false
        }

        val dataInicioStr = etDataInicio.text.toString()
        if (dataInicioStr.length < 10) {
            tvDataError.visibility = View.VISIBLE
            isValid = false
        } else {
             try {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                sdf.isLenient = false
                val parsedDate = sdf.parse(dataInicioStr)
                val cal = Calendar.getInstance()
                cal.time = parsedDate
                if (cal.get(Calendar.YEAR) < 1900) {
                    tvDataError.visibility = View.VISIBLE
                    isValid = false
                } else {
                    tvDataError.visibility = View.GONE
                }
            } catch (e: Exception) {
                tvDataError.visibility = View.VISIBLE
                isValid = false
            }
        }

        return isValid
    }

    private fun setupLanguageSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinnerIdiomas)
        val sharedPref = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val currentLanguage = sharedPref.getString("language", "pt-BR") ?: "pt-BR"

        // Códigos ISO para troca interna
        val supportedLanguages = listOf("pt-BR", "ar", "de", "en", "es", "fr", "hi", "ja", "zh")
        // Nomes em inglês para exibir
        val displayLanguages = listOf(
            "Portuguese (Brazil)",
            "Arabic",
            "German",
            "English",
            "Spanish",
            "French",
            "Hindi",
            "Japanese",
            "Chinese"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, displayLanguages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(supportedLanguages.indexOf(currentLanguage))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val newLanguage = supportedLanguages[position]
                if (newLanguage != currentLanguage) {
                    with(sharedPref.edit()) {
                        putString("language", newLanguage)
                        apply()
                    }
                    recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }


    private fun setupContactButtons() {
        btnWhatsapp.setOnClickListener {
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

        btnGmail.setOnClickListener {
            val email = arrayOf("contato@cespedeslourencoedvogados.com.br")
            val subject = getString(R.string.gmail_subject)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, email)
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }

            try {
                startActivity(Intent.createChooser(intent, getString(R.string.email_chooser_title)))
            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.gmail_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDateInputMask() {
        etDataInicio.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString() == current) {
                    return
                }

                var clean = s.toString().replace(Regex("[^\\d]"), "")
                if (clean.length > 8) {
                    clean = clean.substring(0, 8)
                }

                val formatted = buildString {
                    var i = 0
                    while (i < clean.length) {
                        append(clean[i])
                        if ((i == 1 && clean.length > 2) || (i == 3 && clean.length > 4)) {
                            append('/')
                        }
                        i++
                    }
                }

                current = formatted
                etDataInicio.setText(formatted)
                etDataInicio.setSelection(formatted.length)
            }
        })
    }

    private fun calcularEAbrirResultados() {
        val anos = etAnos.text.toString().toIntOrNull() ?: 0
        val meses = etMeses.text.toString().toIntOrNull() ?: 0
        val dias = etDias.text.toString().toIntOrNull() ?: 0

        val regime = when (rgRegime.checkedRadioButtonId) {
            R.id.rbFechado -> "Fechado"
            R.id.rbSemiaberto -> "Semiaberto"
            R.id.rbAberto -> "Aberto"
            else -> ""
        }

        val tipoCrime = when (rgTipoCrime.checkedRadioButtonId) {
            R.id.rbHediondo -> "Hediondo"
            else -> "Comum"
        }

        val reincidente = cbReincidente.isChecked
        val violencia = cbViolencia.isChecked
        val morte = cbMorte.isChecked
        val dataInicioStr = etDataInicio.text.toString()
        val diasTrabalhados = etDiasTrabalhados.text.toString().toIntOrNull() ?: 0
        val horasEstudo = etHorasEstudo.text.toString().toIntOrNull() ?: 0
        val livrosLidos = etLeitura.text.toString().toIntOrNull() ?: 0
        val diasDetracao = etDetracao.text.toString().toIntOrNull() ?: 0

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataInicio = sdf.parse(dataInicioStr) ?: Date()

        val diasTotaisPena = (anos * 365) + (meses * 30) + dias

        val diasRemicaoTrabalho = if (regime != "Aberto") diasTrabalhados / 3 else 0
        val diasRemicaoEstudo = horasEstudo / 12
        val diasRemicaoLeitura = livrosLidos * 4
        val diasRemicaoTotal = diasRemicaoTrabalho + diasRemicaoEstudo + diasRemicaoLeitura

        val diasPenaAjustada = maxOf(0, diasTotaisPena - diasRemicaoTotal - diasDetracao)

        val percentualProgressao = when {
            tipoCrime == "Hediondo" && morte && reincidente -> 70.0
            tipoCrime == "Hediondo" && morte && !reincidente -> 50.0
            tipoCrime == "Hediondo" && reincidente -> 60.0
            tipoCrime == "Hediondo" && !reincidente -> 40.0
            violencia && reincidente -> 30.0
            violencia && !reincidente -> 25.0
            tipoCrime == "Comum" && reincidente -> 20.0
            else -> 16.0 // Crime comum sem reincidência
        }

        val percentualLivramento = when {
            tipoCrime == "Hediondo" && morte -> -1.0 // Not applicable
            tipoCrime == "Hediondo" -> 2.0 / 3.0 * 100
            reincidente -> 1.0 / 2.0 * 100
            else -> 1.0 / 3.0 * 100
        }

        val calendar = Calendar.getInstance()
        calendar.time = dataInicio

        calendar.add(Calendar.DAY_OF_YEAR, diasPenaAjustada)
        val dataTermino = calendar.time

        calendar.time = dataInicio
        calendar.add(Calendar.DAY_OF_YEAR, (diasPenaAjustada * percentualProgressao / 100).toInt())
        val dataProgressao = calendar.time

        val dataLivramento: Date? = if (percentualLivramento > 0) {
            calendar.time = dataInicio
            calendar.add(Calendar.DAY_OF_YEAR, (diasPenaAjustada * percentualLivramento / 100).toInt())
            calendar.time
        } else {
            null
        }

        val intent = Intent(this, ResultadosActivity::class.java).apply {
            putExtra("dataTermino", "${getString(R.string.termino_pena_label)} (100%): ${sdf.format(dataTermino)}")
            putExtra("dataProgressao", "${getString(R.string.progressao_regime)} (${percentualProgressao}%): ${sdf.format(dataProgressao)}")
            val livramentoLabel = getString(R.string.livramento_condicional)
            if (dataLivramento != null) {
                putExtra("dataLivramento", "$livramentoLabel (${String.format("%.1f", percentualLivramento)}%): ${sdf.format(dataLivramento)}")
            } else {
                putExtra("dataLivramento", "$livramentoLabel: ${getString(R.string.nao_aplicavel)}")
            }
        }
        startActivity(intent)
    }

    private fun loadLanguage() {
        val sharedPref = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val language = sharedPref.getString("language", null)
        if (language != null) {
            val appLocale = LocaleListCompat.forLanguageTags(language)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }
}
