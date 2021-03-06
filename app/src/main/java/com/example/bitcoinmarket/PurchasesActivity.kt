package com.example.bitcoinmarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitcoinmarket.DAO.PurchasesDAO
import com.example.bitcoinmarket.Objetos.Asset
import com.example.bitcoinmarket.Objetos.Purchase
import com.example.bitcoinmarket.Objetos.Name
import kotlinx.android.synthetic.main.activity_purchases.*
import kotlinx.android.synthetic.main.activity_main.fab_add
import kotlinx.android.synthetic.main.activity_main.rv_dados
import kotlinx.android.synthetic.main.activity_main.txtMsg

class PurchasesActivity : AppCompatActivity() {
    private var AtivoList = mutableListOf<Purchase>()
    var nome = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchases)
        val ativo = intent.getParcelableExtra<Asset>("ativos")
        nome = ativo?.nome.toString()


        fab_add.setOnClickListener(View.OnClickListener {


            var enviarNome = Name(ativo?.nome)
            val it = Intent(this, SavePurchasesActivity::class.java)
            it.putExtra("nomeAtivo", enviarNome)
            startActivity(it)
        })
        initRecyclerView()

    }

    private fun initRecyclerView() {
        Log.v("LOG", "Inicia RecyclerView")
        val adapter2 = PurchaseAdapter(AtivoList)
        rv_dados.adapter = adapter2
        val layout = LinearLayoutManager(this)
        rv_dados.layoutManager = layout
    }

    private fun update() {
        val compraDao = PurchasesDAO(this)
        AtivoList.clear()
        AtivoList = compraDao.selectNome(nome)
        txt_codigo_purchaseactivity.text =
            resources.getString(R.string.qtd_ativos) + " = " + compraDao.selectSoma(nome).toString()

        if (AtivoList.isEmpty()) {
            rv_dados.setVisibility(View.GONE)
            txtMsg.setVisibility(View.VISIBLE)
            txtMsg.text = "Nenhum ativo adicionado"
        } else {
            rv_dados.setVisibility(View.VISIBLE)
            txtMsg.setVisibility(View.GONE)
        }
        rv_dados.adapter?.notifyDataSetChanged()
    }

    override fun onRestart() {
        super.onRestart()
        update()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        update()
        initRecyclerView()
    }
}
