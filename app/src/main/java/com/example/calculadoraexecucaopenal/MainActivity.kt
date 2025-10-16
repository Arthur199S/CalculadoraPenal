package com.example.calculadoraexecucaopenal

import com.example.calculadoraexecucaopenal.R

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etAnos: EditText
    private lateinit var etMeses: EditText
    private lateinit var etDias: EditText
    private lateinit var spinnerRegime: Spinner
    private lateinit var spinnerTipoCrime: Spinner
    private lateinit var cbReincidente: CheckBox
    private lateinit var cbViolencia: CheckBox
    private lateinit var etDataInicio: EditText
    private lateinit var etDiasTrabalhados: EditText
    private lateinit var etHorasEstudo: EditText
    private lateinit var btnCalcular: Button
    private lateinit var llResultados: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupSpinners()
        setupButton()
    }

    private fun initViews() {
        etAnos = findViewById(R.id.etAnos)
        etMeses = findViewById(R.id.etMeses)
        etDias = findViewById(R.id.etDias)
        spinnerRegime = findViewById(R.id.spinnerRegime)
        spinnerTipoCrime = findViewById(R.id.spinnerTipoCrime)
        cbReincidente = findViewById(R.id.cbReincidente)
        cbViolencia = findViewById(R.id.cbViolencia)
        etDataInicio = findViewById(R.id.etDataInicio)
        etDiasTrabalhados = findViewById(R.id.etDiasTrabalhados)
        etHorasEstudo = findViewById(R.id.etHorasEstudo)
        btnCalcular = findViewById(R.id.btnCalcular)
        llResultados = findViewById(R.id.llResultados)
    }

    private fun setupSpinners() {
        // Configurar Spinner do Regime
        val regimes = arrayOf("Fechado", "Semiaberto", "Aberto")
        val regimeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, regimes)
        regimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRegime.adapter = regimeAdapter

        // Configurar Spinner do Tipo de Crime
        val tiposCrime = arrayOf("Comum", "Hediondo", "Outros")
        val crimeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposCrime)
        crimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoCrime.adapter = crimeAdapter
    }

    private fun setupButton() {
        btnCalcular.setOnClickListener {
            calcularDatas()
        }
    }

    private fun calcularDatas() {
        // Obter valores dos inputs
        val anos = etAnos.text.toString().toIntOrNull() ?: 0
        val meses = etMeses.text.toString().toIntOrNull() ?: 0
        val dias = etDias.text.toString().toIntOrNull() ?: 0
        spinnerRegime.selectedItem?.toString() ?: return
        val tipoCrime = spinnerTipoCrime.selectedItem?.toString() ?: return
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
            Date() // Data atual como fallback
        }

        // Calcular dias totais da pena
        val diasTotaisPena = (anos * 365) + (meses * 30) + dias

        // Calcular remição (3 dias por 1 trabalhado, 1 dia por 12h de estudo)
        val diasRemicaoTrabalho = diasTrabalhados / 3
        val diasRemicaoEstudo = horasEstudo / 12
        val diasRemicaoTotal = diasRemicaoTrabalho + diasRemicaoEstudo

        // Ajustar pena com remição
        val diasPenaAjustada = maxOf(0, diasTotaisPena - diasRemicaoTotal)

        // Calcular percentuais para progressão
        var percentualSemiaberto = 16.0
        var percentualAberto = 16.0
        var percentualCondicional = 33.4

        // Ajustar percentuais baseado em condições
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

        // Calcular datas
        val calendar = Calendar.getInstance()
        calendar.time = dataInicio

        // Término da pena
        calendar.time = dataInicio
        calendar.add(Calendar.DAY_OF_YEAR, diasPenaAjustada)
        val dataTermino = calendar.time

        // Progressão para Semiaberto
        calendar.time = dataInicio
        calendar.add(Calendar.DAY_OF_YEAR, (diasPenaAjustada * percentualSemiaberto / 100).toInt())
        val dataSemiaberto = calendar.time

        // Progressão para Aberto
        calendar.time = dataInicio
        calendar.add(Calendar.DAY_OF_YEAR, (diasPenaAjustada * percentualAberto / 100).toInt())
        val dataAberto = calendar.time

        // Liberdade Condicional
        calendar.time = dataInicio
        calendar.add(Calendar.DAY_OF_YEAR, (diasPenaAjustada * percentualCondicional / 100).toInt())
        val dataCondicional = calendar.time

        // Exibir resultados
        exibirResultados(dataTermino, dataSemiaberto, dataAberto, dataCondicional,
            percentualSemiaberto, percentualAberto, percentualCondicional)
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

        // Título
        val titulo = TextView(this).apply {
            text = "Progressões e Benefícios"
            textSize = 18f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.black))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setPadding(0, 0, 0, 16)
        }
        llResultados.addView(titulo)

        // Término da Pena
        addResultadoItem(
            "Término da Pena sem Progressões e Remições",
            sdf.format(dataTermino),
            "(100.0% da pena)"
        )

        // Progressão Semiaberto
        addResultadoItem(
            "Progressão para Regime Semiaberto",
            sdf.format(dataSemiaberto),
            "(${percentualSemiaberto}% da pena)"
        )

        // Progressão Aberto
        addResultadoItem(
            "Progressão para Regime Aberto",
            sdf.format(dataAberto),
            "(${percentualAberto}% da pena)"
        )

        // Liberdade Condicional
        addResultadoItem(
            "Liberdade Condicional",
            sdf.format(dataCondicional),
            "(${percentualCondicional}% da pena)"
        )
    }

    private fun addResultadoItem(titulo: String, data: String, percentual: String) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 16, 32, 16)
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.white))
            setPadding(16, 16, 16, 16)
            // Para a borda colorida, você pode usar um Drawable personalizado
            // Aqui estou usando apenas margem para simular
            setPadding(16, 16, 16, 16)
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
            setPadding(0, 8, 0, 4)
        }

        val tvPercentual = TextView(this).apply {
            text = percentual
            textSize = 14f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.darker_gray))
        }

        layout.addView(tvTitulo)
        layout.addView(tvData)
        layout.addView(tvPercentual)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, 16)
        }

        llResultados.addView(layout, params)
    }
}