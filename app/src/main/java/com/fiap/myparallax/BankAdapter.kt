package com.fiap.myparallax

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BankAdapter(private val items: MutableList<Bank>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_BRADESCO = 1
    }

    // Interface para aplicar as transformações nos cards
    interface CardTransformation {
        fun applyTransformations(scale: Float, translationY: Float, parallaxTranslationY: Float, accumulatedTranslationY: Float, i: Int)
    }

    // ViewHolder para o layout padrão
    inner class BankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), CardTransformation {
        val bankName: TextView = itemView.findViewById(R.id.titleBank)
        val bankDescription: TextView = itemView.findViewById(R.id.descriptionBank)
        val bankImage: ImageView = itemView.findViewById(R.id.imageBank)

        override fun applyTransformations(scale: Float, translationY: Float, parallaxTranslationY: Float, accumulatedTranslationY: Float, i: Int) {
            itemView.translationY = translationY - accumulatedTranslationY
            itemView.scaleX = scale
            itemView.scaleY = scale
            itemView.translationZ = i.toFloat() // Inverter a lógica da translationZ
            itemView.alpha = scale
        }
    }

    // ViewHolder para o layout do Bradesco
    inner class BradescoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), CardTransformation {
        val bankName: TextView = itemView.findViewById(R.id.bankName)
        val bankDescription: TextView = itemView.findViewById(R.id.bankDescription)
        val bankImage: ImageView = itemView.findViewById(R.id.bankImage)

        override fun applyTransformations(scale: Float, translationY: Float, parallaxTranslationY: Float, accumulatedTranslationY: Float, i: Int) {
            itemView.translationY = translationY - accumulatedTranslationY
            itemView.scaleX = scale
            itemView.scaleY = scale
            itemView.translationZ = i.toFloat() // Inverter a lógica da translationZ
            itemView.alpha = scale
        }
    }

    // Retorna o tipo de view para cada item
    override fun getItemViewType(position: Int): Int {
        return if (items[position].title == "Bradesco") {
            VIEW_TYPE_BRADESCO
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    // Cria o ViewHolder apropriado com base no tipo de view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_BRADESCO -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_bank_bradesco, parent, false)
                BradescoViewHolder(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
                BankViewHolder(itemView)
            }
        }
    }

    // Configura os elementos de view de cada ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = items[position]
        when (holder) {
            is BradescoViewHolder -> {
                holder.bankName.text = currentItem.title
                holder.bankDescription.text = currentItem.description
                holder.bankImage.setImageResource(currentItem.logo)
            }
            is BankViewHolder -> {
                holder.bankName.text = currentItem.title
                holder.bankDescription.text = currentItem.description
                holder.bankImage.setImageResource(currentItem.logo)
            }
        }
    }

    // Retorna o número de itens na lista
    override fun getItemCount(): Int {
        return items.size
    }

    // Adiciona um itemà lista
    fun addItem(item: Bank) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    // Remove um item da lista
    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}