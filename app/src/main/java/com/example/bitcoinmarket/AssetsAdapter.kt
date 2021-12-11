package com.example.bitcoinmarket

import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.bitcoinmarket.DAO.AssetsDAO
import com.example.bitcoinmarket.DAO.PurchasesDAO
import com.example.bitcoinmarket.Objetos.Asset
import kotlinx.android.synthetic.main.asset_item.view.*
import java.text.DecimalFormat

class AssetsAdapter(private val assets: List<Asset>) :
    RecyclerView.Adapter<AssetsAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        Log.v("LOG", "onCreate")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.asset_item, parent, false)
        val vh = VH(v)


        vh.itemView.setOnClickListener {
            val ativo = assets[vh.adapterPosition]
            val it = Intent(parent.context, PurchasesActivity::class.java)
            it.putExtra("ativos", ativo)
            parent.context.startActivity(it)

        }
        return vh
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Log.v("LOG", "ViewHolder")
        var ativo = assets[position]
        var mostrarItens = false
        holder.btn_delete.setOnClickListener(View.OnClickListener {
            val ativoDao = AssetsDAO(it.context)
            val ativo = assets[position]
            val alerta =
                AlertDialog.Builder(it.context)
            alerta.setTitle("Aviso")
            alerta
                .setIcon(R.drawable.ic_info_foreground)
                .setMessage("Deseja realmente remover o ativo.")
                .setCancelable(true)
                .setPositiveButton(
                    "Sim",
                    DialogInterface.OnClickListener({ dialogInterface, i ->
                        ativoDao.delete(ativo)
                        Toast.makeText(it.context, "Ativo removido com sucesso", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(it.context, MainActivity::class.java)
                        it.context.startActivity(intent)

                    })
                )
                .setNegativeButton(
                    "Não",
                    DialogInterface.OnClickListener({ DialogInterface, i ->
                    })
                )


            val alertDialog = alerta.create()
            alertDialog.show()
        })

        holder.btn_drop.setOnClickListener({
            val df = DecimalFormat("#0.00")
            val compraDao = PurchasesDAO(it.context)
            val qtd = compraDao.selectQtd(ativo.nome.toString())
            val valor = compraDao.selectValorInvestido(ativo.nome.toString())
            if (mostrarItens == false) {

                holder.txt_quantidade.setVisibility(View.VISIBLE)
                holder.txt_quantidade.text = "Quantidade = " + qtd.toString()
                holder.txt_valor.setVisibility(View.VISIBLE)
                holder.txt_valor.text = "Valor investido = " + df.format(valor)

                val layoutParams =
                    holder.barra.getLayoutParams()
                layoutParams.height = 330
                holder.barra.setLayoutParams(layoutParams);
                mostrarItens = true
            } else {
                holder.txt_quantidade.setVisibility(View.GONE)
                holder.txt_valor.setVisibility(View.GONE)
                val layoutParams =
                    holder.barra.getLayoutParams()
                layoutParams.height = 210
                holder.barra.setLayoutParams(layoutParams)
                mostrarItens = false
            }
        })

        holder.txtCodigo.text = ativo.codigo.toString()
        holder.txtMoeda.text = ativo.nome.toString()

    }

    class VH(itenView: View) : RecyclerView.ViewHolder(itenView) {

        var txtCodigo: TextView = itenView.txt_codigo_asset
        var txtMoeda: TextView = itenView.txtmoeda_asset
        var btn_delete: ImageView = itemView.btn_delete_assets
        var btn_drop: ImageView = itemView.drop_assets
        var txt_quantidade: TextView = itemView.txt_quantidade_asset
        var txt_valor: TextView = itemView.txt_valor_asset
        var barra: View = itemView.view3

    }
}