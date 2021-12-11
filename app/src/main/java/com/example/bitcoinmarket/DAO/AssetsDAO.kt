package com.example.bitcoinmarket.DAO

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.bitcoinmarket.*
import com.example.bitcoinmarket.Objetos.Asset

class AssetsDAO(context: Context) {
    val banco = DbHelper(context)

    fun insert(asset: Asset): String {
        val db = banco.writableDatabase
        val contextValues = ContentValues()

        contextValues.put(ID_COLUMN, asset.id)
        contextValues.put(NOME_COLUMN, asset.nome)
        contextValues.put(CODIGO_COLUMN, asset.codigo)
        contextValues.put(QTD_COLUMN, asset.qtd)

        val resp_id = db.insert(TABELA_ATIVO, null, contextValues)
        val msg = if (resp_id != -1L) {
            "Inserido"
        } else {
            "Erro ao inserir"
        }
        db.close()
        return msg
    }

    fun delete(asset: Asset): Int {
        val db = banco.writableDatabase
        return db.delete(TABELA_ATIVO, "id_ativos =?", arrayOf(asset.id.toString()))
    }

    fun select(): ArrayList<Asset> {
        Log.v("LOG", "GetAll")
        val db = banco.writableDatabase
        val sql = "SELECT * from " + TABELA_ATIVO
        val cursor = db.rawQuery(sql, null)
        val ativo = ArrayList<Asset>()
        while (cursor.moveToNext()) {
            val moeda = ativoFromCursor(cursor)
            ativo.add(moeda)
        }
        cursor.close()
        db.close()
        Log.v("LOG", "Get ${ativo.size}")
        return ativo
    }

    fun pegaId(id_ativo: Int?): Boolean {
        val db = banco.writableDatabase
        var id = 0

        val sql = "SELECT * from " + TABELA_ATIVO
        val cursor = db.rawQuery(sql, null)
        val ativo = ArrayList<Asset>()
        while (cursor.moveToNext()) {
            val moeda = ativoFromCursor(cursor)
            id = moeda.id!!
            if (id_ativo == id) {
                return false
            }
        }
        cursor.close()
        db.close()
        Log.v("LOG", "Get ${ativo.size}")
        return true
    }


    private fun ativoFromCursor(cursor: Cursor): Asset {
        val id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN))
        val codigo = cursor.getString(cursor.getColumnIndex(CODIGO_COLUMN))
        val nome = cursor.getString(cursor.getColumnIndex(NOME_COLUMN))
        val qtd = cursor.getDouble(cursor.getColumnIndex(QTD_COLUMN))

        return Asset(id, nome, codigo, qtd)

    }
}