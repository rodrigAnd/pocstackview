package com.fiap.myparallax

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BankAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.my_recycler_view)
        adapter = BankAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        var isSnapHelperEnabled = true
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        val scrollListener = object : RecyclerView.OnScrollListener() {
            var accumulatedTranslationY = 0f

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (firstVisibleItemPosition == 0 && adapter.getItemViewType(0) == BankAdapter.VIEW_TYPE_BRADESCO) { // Verifica se o card menor está no topo
                    if (isSnapHelperEnabled) {
                        snapHelper.attachToRecyclerView(null) // Desabilita o snapHelper
                        isSnapHelperEnabled = false
                    }
                } else {
                    if (!isSnapHelperEnabled) {
                        snapHelper.attachToRecyclerView(recyclerView) // Habilita o snapHelper
                        isSnapHelperEnabled = true
                    }
                }

                for (i in 0 until recyclerView.childCount) {
                    val child = recyclerView.getChildAt(i)
                    val top = child.top

                    if (top < 0) {
                        // Calcula o fator de escala para o zoom out (limitado a 20%)
                        val scale = (1f + (top / child.height.toFloat()).coerceAtMost(0.1f)).coerceAtLeast(0.9f)

                        // Calcula a translação vertical para empilhar os cards
                        val translationY = -top * (1 - scale)

                        // Calcula a translação para o efeito parallax
                        val parallaxTranslationY = (top / 1.5f).coerceAtLeast(0f)
                        accumulatedTranslationY += parallaxTranslationY * (1 - scale)

                        // Calcula o alpha com base na posição do topo do card
                        val screenHeight = recyclerView.height
                        val alpha = 1 + (top / screenHeight.toFloat())

                        // Aplica as transformações
                        child.translationY = translationY - accumulatedTranslationY // Adiciona parallax
                        child.scaleX = scale
                        child.scaleY = scale
                        child.translationZ = -i.toFloat() // Ajusta a profundidade
                        child.alpha = alpha.coerceAtLeast(1f) // Aplica fade out
                    } else {
                        // Reseta as transformações quando o card sai da tela
                        child.translationY = 0f
                        child.scaleX = 1f
                        child.scaleY = 1f
                        child.alpha = 1f
                        accumulatedTranslationY = 0f
                        child.translationZ = 0f // Reseta a profundidade
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(scrollListener)

        // Adiciona itens ao adapter (substitua pelos seus dados)
        adapter.addItem(item = Bank("Bradesco", getString(R.string.bank_bradesco), R.drawable.bradesco))
        adapter.addItem(item =Bank("BB", getString(R.string.bank_bb), R.drawable.brasil))
        adapter.addItem(item = Bank("BV", getString(R.string.bank_bv), R.drawable.bv))
        adapter.addItem(item = Bank("Inter", getString(R.string.bank_inter), R.drawable.inter))
        adapter.addItem(item = Bank("Itau", getString(R.string.bank_itau), R.drawable.itau))
        adapter.addItem(item = Bank("Neon", getString(R.string.bank_neon), R.drawable.neon))
        adapter.addItem(item = Bank("Original", getString(R.string.bank_original), R.drawable.original))
        adapter.addItem(item = Bank("Santander", getString(R.string.bank_santander), R.drawable.santander))
    }
}