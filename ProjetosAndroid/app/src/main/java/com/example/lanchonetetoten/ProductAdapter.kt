package com.example.lanchonetetoten

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.lanchonetetoten.models.Product
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var items: List<Product> = ArrayList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

                return LiveViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
                )

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

                when (holder){
                        is LiveViewHolder ->{
                           holder.bind(items[position])
                        }
                }
        }

        override fun getItemCount(): Int {
                return items.size
        }

        fun setDataSet (product: List<Product>){
                this.items = product
        }

        class LiveViewHolder constructor(
                itemView: View
        ) : RecyclerView.ViewHolder(itemView) {

                private val namproduct = itemView.nameprodutc
                private val priceproduct = itemView.priceproduct

                fun bind(product: Product) {

                namproduct.text = product.nameproduct
                priceproduct.text = product.price.toString()
        }
     }
}