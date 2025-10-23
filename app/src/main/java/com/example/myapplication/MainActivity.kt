package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatDelegate
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // Campos de entrada
    private lateinit var etAnos: EditText
    private lateinit var etMeses: EditText
    private lateinit var etDias: EditText
    private lateinit var cbReincidente: CheckBox
    private lateinit var cbViolencia: CheckBox
    private lateinit var etDataInicio: EditText
    private lateinit var etDiasTrabalhados: EditText
    private lateinit var etHorasEstudo: EditText
    private lateinit var btnCalcular: Button
    private lateinit var llResultados: LinearLayout

    // RadioGroups
    private lateinit var rgRegime: RadioGroup
    private lateinit var rgTipoCrime: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupButton()
    }

    private fun initViews() {
        etAnos = findViewById(R.id.etAnos)
        etMeses = findViewById(R.id.etMeses)
        etDias = findViewById(R.id.etDias)
        cbReincidente = findViewById(R.id.cbReincidente)
        cbViolencia = findViewById(R.id.cbViolencia)
        etDataInicio = findViewById(R.id.etDataInicio)
        etDiasTrabalhados = findViewById(R.id.etDiasTrabalhados)
        etHorasEstudo = findViewById(R.id.etHorasEstudo)
        btnCalcular = findViewById(R.id.btnCalcular)
        llResultados = findViewById(R.id.llResultados)

        // Novos RadioGroups
        rgRegime = findViewById(R.id.rgRegime)
        rgTipoCrime = findViewById(R.id.rgTipoCrime)
    }

    private fun setupButton() {
        btnCalcular.setOnClickListener {
            calcularDatas()
        }
    }

    private fun calcularDatas() {
        val anos = etAnos.text.toString().toIntOrNull() ?: 0
        val meses = etMeses.text.toString().toIntOrNull() ?: 0
        val dias = etDias.text.toString().toIntOrNull() ?: 0

        // Obter seleções dos RadioButtons
        val regime = when (rgRegime.checkedRadioButtonId) {
            R.id.rbFechado -> "Fechado"
            R.id.rbSemiaberto -> "Semiaberto"
            R.id.rbAberto -> "Aberto"
            else -> ""
        }

        val tipoCrime = when (rgTipoCrime.checkedRadioButtonId) {
            R.id.rbComum -> "Comum"
            R.id.rbHediondo -> "Hediondo"
            R.id.rbOutros -> "Outros"
            else -> ""
        }

        val reincidente = cbReincidente.isChecked
        val violencia = cbViolencia.isChecked
        val dataInicioStr = etDataInicio.text.toString()
        val diasTrabalhados = etDiasTrabalhados.text.toString().toIntOrNull() ?: 0
        val horasEstudo = etHorasEstudo.text.toString().toIntOrNull() ?: 0

        // Parse da data de início
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataInicio = try {
            sdf.parse(dataInicioStr) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        // Cálculo dos dias totais
        val diasTotaisPena = (anos * 365) + (meses * 30) + dias
        val diasRemicaoTrabalho = diasTrabalhados / 3
        val diasRemicaoEstudo = horasEstudo / 12
        val diasRemicaoTotal = diasRemicaoTrabalho + diasRemicaoEstudo
        val diasPenaAjustada = maxOf(0, diasTotaisPena - diasRemicaoTotal)

        // Percentuais padrão
        var percentualSemiaberto = 16.0
        var percentualAberto = 16.0
        var percentualCondicional = 33.4

        // Ajustes baseados nas condições
        if (reincidente) {
            percentualSemiaberto = 20.0
            percentualAberto = 20.0
            percentualCondicional = 40.0
        }

        if (violencia) {
            percentualSemiaberto = 20.0
            percentualAberto = 20.0
        }

        if (tipoCrime == "Hediondo") {
            percentualSemiaberto = 20.0
            percentualAberto = 20.0
            percentualCondicional = 60.0
        }

        // Cálculo das datas
        val calendar = Calendar.getInstance()
        calendar.time = dataInicio

        calendar.add(Calendar.DAY_OF_YEAR, diasPenaAjustada)
        val dataTermino = calendar.time

        calendar.time = dataInicio
        calendar.add(Calendar.DAY_OF_YEAR, (diasPenaAjustada * percentualSemiaberto / 100).toInt())
        val dataSemiaberto = calendar.time

        calendar.time = dataInicio
        calendar.add(Calendar.DAY_OF_YEAR, (diasPenaAjustada * percentualAberto / 100).toInt())
        val dataAberto = calendar.time

        calendar.time = dataInicio
        calendar.add(Calendar.DAY_OF_YEAR, (diasPenaAjustada * percentualCondicional / 100).toInt())
        val dataCondicional = calendar.time

        // Mostrar resultados
        exibirResultados(
            dataTermino, dataSemiaberto, dataAberto, dataCondicional,
            percentualSemiaberto, percentualAberto, percentualCondicional
        )
    }

    private fun exibirResultados(
        dataTermino: Date,
        dataSemiaberto: Date,
        dataAberto: Date,
        dataCondicional: Date,
        percentualSemiaberto: Double,
        percentualAberto: Double,
        percentualCondicional: Double
    ) {
        llResultados.removeAllViews()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        addResultadoItem("Término da Pena (100%)", sdf.format(dataTermino))
        addResultadoItem("Progressão p/ Semiaberto (${percentualSemiaberto}%)", sdf.format(dataSemiaberto))
        addResultadoItem("Progressão p/ Aberto (${percentualAberto}%)", sdf.format(dataAberto))
        addResultadoItem("Liberdade Condicional (${percentualCondicional}%)", sdf.format(dataCondicional))
    }

    private fun addResultadoItem(titulo: String, data: String) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.white))
        }

        val tvTitulo = TextView(this).apply {
            text = titulo
            textSize = 16f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.black))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }

        val tvData = TextView(this).apply {
            text = data
            textSize = 18f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.holo_red_dark))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }

        layout.addView(tvTitulo)
        layout.addView(tvData)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, 16)
        }

        llResultados.addView(layout, params)
    }
}
