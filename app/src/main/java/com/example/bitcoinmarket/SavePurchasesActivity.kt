package com.example.bitcoinmarket

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bitcoinmarket.API.CoinsPurchaseHTTP
import com.example.bitcoinmarket.DAO.PurchasesDAO
import com.example.bitcoinmarket.Objetos.Purchase
import com.example.bitcoinmarket.Objetos.AssetCoin
import com.example.bitcoinmarket.Objetos.Name
import kotlinx.android.synthetic.main.activity_save_purchases.*
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class SavePurchasesActivity : AppCompatActivity() {
    private var asyncTask: StatesTask? = null
    var codigo: String = ""
    var valor = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_purchases)

        val compra = intent.getParcelableExtra<Name>("nomeAtivo")
        if (compra?.name.equals("Bitcoin")) {
            codigo = "BTC"
        }
        if (compra?.name.equals("Litecoin")) {
            codigo = "LTC"
        }
        if (compra?.name.equals("XRP")) {
            codigo = "XRP"
        }
        if (compra?.name.equals("BCash")) {
            codigo = "BCH"
        }
        if (compra?.name.equals("Ethereum")) {
            codigo = "ETH"
        }
        CarregaDados()

        FABBack.setOnClickListener({
            onBackPressed()
        })


        FABSave.setOnClickListener {
            if (edtQtd.text.toString().toFloat() > 0) {
                try {
                    val verificacao = edtQtd.text.toString().toFloat()
                    val dStr = getDate()


                    var compra = Purchase(
                        null,
                        compra?.name.toString(),
                        dStr.toString(),
                        edtQtd.text.toString().toDouble(),
                        valor
                    )
                    var compraDao = PurchasesDAO(this)
                    compraDao.insert(compra)
                    onBackPressed()
                } catch (e: Exception) {
                    alerta()
                }
            } else {
                alerta()
            }
        }
    }

    fun alerta() {
        val alerta =
            AlertDialog.Builder(this@SavePurchasesActivity)
        alerta.setTitle("Aviso")
        alerta
            .setIcon(R.drawable.ic_info_foreground)
            .setMessage("Você deve digitar apenas numeros maiores que 0.")
            .setCancelable(true)
            .setPositiveButton(
                "OK",
                DialogInterface.OnClickListener({ dialogInterface, i ->
                })
            )


        val alertDialog = alerta.create()
        alertDialog.show()
    }

    fun CarregaDados() {
        if (asyncTask == null) {
            if (CoinsPurchaseHTTP.hasConnection(this)) {
                if (asyncTask?.status != AsyncTask.Status.RUNNING) {
                    asyncTask = StatesTask()
                    asyncTask?.execute()
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class StatesTask : AsyncTask<Void, Void, AssetCoin?>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }


        @SuppressLint("WrongThread")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg params: Void?): AssetCoin? {
            return CoinsPurchaseHTTP.loadMoedas(codigo)
        }


        private fun update(result: AssetCoin?) {
            var df = DecimalFormat("#0.00")
            if (result != null) {
                valor = result.buy.toDouble()
            }
            txt_valor_purchasesave.text = resources.getString(R.string.valor_ativo) + " R$ " + df.format(valor)

            asyncTask = null
        }

        override fun onPostExecute(result: AssetCoin?) {
            super.onPostExecute(result)
            update(result as AssetCoin?)
        }
    }

    private fun getDate(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        return dateFormat.format(date)
    }
}
