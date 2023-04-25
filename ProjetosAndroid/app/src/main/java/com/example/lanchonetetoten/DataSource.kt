package com.example.lanchonetetoten

import com.example.lanchonetetoten.models.Product

class DataSource {

    companion object {

        fun  createDataSet() : ArrayList<Product> {

            val list = ArrayList<Product>()

            list.add(
                Product(
                    "X-Burger", price = 24.00
                )
            )

            list.add(
                Product(
                    "X-Salada", price= 28.00
                )
            )
            list.add(
                Product(
                    "Batata Frita", price = 15.00
                )
            )
            list.add(
                Product(
                    "Coca-Cola", price = 6.00
                )
            )
            list.add(
                Product(
                    "Suco Laranja", price = 9.00
                )
            )
            list.add(
                Product(
                    "Suco Uva", price = 9.00
                )
            )

            list.add(
                Product(
                    "Hot-Dog", price = 7.00
                )
            )
            list.add(
                Product(
                    "Chocolate", price = 7.00
                )
            )
            list.add(
                Product(
                    "Sorvete", price = 15.00
                )
            )

            return list
        }
    }
}