package com.example.bitcoinmarket

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.bitcoinmarket.DAO.AssetsDAO
import com.example.bitcoinmarket.Objetos.Asset
import kotlinx.android.synthetic.main.activity_save_assets.*

class SaveAssetsActivity : AppCompatActivity() {
    var nome = ""
    var codigo = ""
    var qtd = 0.0
    var id_ativo: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_assets)

        btn_add_btc.setOnClickListener({

            nome = "Bitcoin"
            codigo = "BTC"
            id_ativo = 1
            validaId()
        })

        btn_add_ltc.setOnClickListener({
            nome = "Litecoin"
            codigo = "LTC"
            id_ativo = 2
            validaId()
        })

        btn_add_bch.setOnClickListener({
            nome = "BCash"
            codigo = "BHC"
            id_ativo = 3
            validaId()
        })

        btn_add_xrp.setOnClickListener({
            nome = "XRP"
            codigo = "XRP"
            id_ativo = 4
            validaId()
        })

        btn_add_eth.setOnClickListener({
            nome = "Ethereum"
            codigo = "ETH"
            id_ativo = 5
            validaId()
        })
    }

    fun validaId() {
        var ativo = Asset(id_ativo, nome, codigo, qtd)
        var ativoDao = AssetsDAO(this)
        var validacaoId = ativoDao.pegaId(id_ativo)
        if (validacaoId == true) {
            ativoDao.insert(ativo)
            onBackPressed()
        } else {
            val alerta =
                AlertDialog.Builder(this@SaveAssetsActivity)
            alerta.setTitle("Aviso")
            alerta
                .setIcon(R.drawable.ic_info_foreground)
                .setMessage("Ativo já adicionado.")
                .setCancelable(true)
                .setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener({ dialogInterface, i ->
                    })
                )


            val alertDialog = alerta.create()
            alertDialog.show()
        }
    }
}