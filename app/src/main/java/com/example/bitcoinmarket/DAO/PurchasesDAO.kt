package com.example.bitcoinmarket.DAO

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.bitcoinmarket.*
import com.example.bitcoinmarket.Objetos.Purchase

class PurchasesDAO(context: Context) {
    val banco = DbHelper(context)


    fun insert(purchase: Purchase): String {
        val db = banco.writableDatabase
        val contextValues = ContentValues()

        contextValues.put(ID_COMPRA, purchase.id)
        contextValues.put(NOME_COMPRA, purchase.nome)
        contextValues.put(DATA_COMPRA, purchase.data)
        contextValues.put(QTD_COMPRA, purchase.qtd)
        contextValues.put(VALOR_COMPRA, purchase.valor)

        val resp_id = db.insert(TABELA_COMPRA, null, contextValues)
        val msg = if (resp_id != -1L) {
            "Inserido"
        } else {
            "Nao inserido"
        }
        db.close()
        return msg
    }

    fun selectNome(nome: String): ArrayList<Purchase> {
        Log.v("LOG", "GetAll")
        val db = banco.writableDatabase
        val sql = "SELECT * from " + TABELA_COMPRA + " where $NOME_COMPRA like '%$nome%' "
        Log.v("LOG", "" + sql)
        val cursor = db.rawQuery(sql, null)
        val compra = ArrayList<Purchase>()
        while (cursor.moveToNext()) {
            val compras = compraFromCursor(cursor)
            compra.add(compras)
        }
        cursor.close()
        db.close()
        Log.v("LOG", "Get ${compra.size}")
        return compra
    }

    fun selectQtd(nome: String): Double {
        Log.v("LOG", "GetAll")
        val db = banco.writableDatabase
        val sql = "SELECT * from " + TABELA_COMPRA + " where $NOME_COMPRA like '%$nome%' "
        Log.v("LOG", "" + sql)
        val cursor = db.rawQuery(sql, null)
        var qtd = 0.0
        while (cursor.moveToNext()) {
            val compras = compraFromCursor(cursor)
            qtd += compras.qtd!!
        }
        cursor.close()
        db.close()
        return qtd
    }

    fun selectTotal(): Double {
        Log.v("LOG", "GetAll")
        val db = banco.writableDatabase
        val sql = "SELECT * from " + TABELA_COMPRA
        Log.v("LOG", "" + sql)
        val cursor = db.rawQuery(sql, null)
        val compra = ArrayList<Purchase>()
        var total = 0.0
        while (cursor.moveToNext()) {
            val compras = compraFromCursor(cursor)
            var qtd = compras.qtd!!
            total += qtd * compras.valor!!
        }
        cursor.close()
        db.close()
        Log.v("LOG", "Get ${compra.size}")
        return total
    }

    fun selectValorInvestido(nome: String): Double {
        Log.v("LOG", "GetAll")
        val db = banco.writableDatabase
        val sql = "SELECT * from " + TABELA_COMPRA + " where $NOME_COMPRA like '%$nome%' "
        Log.v("LOG", "" + sql)
        val cursor = db.rawQuery(sql, null)
        val compra = ArrayList<Purchase>()
        var total = 0.0
        while (cursor.moveToNext()) {
            val compras = compraFromCursor(cursor)
            var qtd = compras.qtd!!
            total += qtd * compras.valor!!
        }
        cursor.close()
        db.close()
        Log.v("LOG", "Get ${compra.size}")
        return total
    }

    fun delete(purchase: Purchase): Int {
        val db = banco.writableDatabase
        return db.delete(TABELA_COMPRA, "id_compra =?", arrayOf(purchase.id.toString()))
    }

    fun selectSoma(nome: String): Double {
        Log.v("LOG", "GetAll")
        val db = banco.writableDatabase
        val sql = "SELECT * from " + TABELA_COMPRA + " where $NOME_COMPRA like '%$nome%' "
        Log.v("LOG", "" + sql)
        val cursor = db.rawQuery(sql, null)
        val compra = ArrayList<Purchase>()
        var sum = 0.0
        while (cursor.moveToNext()) {
            val compras = compraFromCursor(cursor)
            sum += compras.qtd!!
        }
        cursor.close()
        db.close()
        Log.v("LOG", "Get ${compra.size}")
        return sum
    }


    @SuppressLint("Range")
    private fun compraFromCursor(cursor: Cursor): Purchase {
        val id = cursor.getInt(cursor.getColumnIndex(ID_COMPRA))
        val nome = cursor.getString(cursor.getColumnIndex(NOME_COMPRA))
        val data = cursor.getString(cursor.getColumnIndex(DATA_COMPRA))
        val qtd = cursor.getDouble(cursor.getColumnIndex(QTD_COMPRA))
        val valor = cursor.getDouble(cursor.getColumnIndex(VALOR_COMPRA))

        return Purchase(id, nome, data, qtd, valor)

    }
}
