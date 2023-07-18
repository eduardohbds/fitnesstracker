package co.silvabr.fitnesstracker

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.silvabr.fitnesstracker.R

class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(
            MainItem(
                id = 1,
                drawableId = R.drawable.ic_baseline_wb_sunny_24,
                textStringId = R.string.label_imc,
                color = Color.GRAY
            )
        )
        mainItems.add(
            MainItem(
                id = 2,
                drawableId = R.drawable.tmb,
                textStringId = R.string.label_tmb,
                color = Color.GRAY
            )
        )
        mainItems.add(
            MainItem(
                id = 3,
                drawableId = R.drawable.body_fat,
                textStringId = R.string.label_bf,
                color = Color.GRAY
            )
        )
        mainItems.add(
            MainItem(
                id = 4,
                drawableId = R.drawable.baseline_location_on_24,
                textStringId = R.string.label_location,
                color = Color.GRAY
            )
        )
        val adapter = MainAdapter(mainItems) { id ->
            when (id) {
                1 -> {
                    val intent = Intent(this@MainActivity, ImcActivity::class.java)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(this@MainActivity, TmbActivity::class.java)
                    startActivity(intent)
                }
                3 -> {
                    val intent = Intent(this@MainActivity, BodyFatActivity::class.java)
                    startActivity(intent)
                }
                4 -> {
                    val intent = Intent(this@MainActivity, GymFinderActivity::class.java)
                    startActivity(intent)
                }
            }
            Log.i("Teste", "clicou $id!!")
        }

        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)

    }


    private inner class MainAdapter(
        private val mainItems: List<MainItem>,
        private val onItemClickListener: (Int) -> Unit,
        ) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

        // 1 - Qual é o layout XML da celula especifica (item)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        // 2 - disparado toda vez houver uma rolagem na tela e for necessario trocar o conteudo
        // da celula
        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = mainItems[position]
            holder.bind(itemCurrent)
        }

        // 3 - informar quantas celulas essa listagem terá
        override fun getItemCount(): Int {
            return mainItems.size
        }

        // é a classe da celula em si!!!
        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: MainItem) {
                val img: ImageView = itemView.findViewById(R.id.item_img_icon)
                val name: TextView = itemView.findViewById(R.id.item_txt_name)
                val container: LinearLayout = itemView.findViewById(R.id.item_container_imc)

                img.setImageResource(item.drawableId)
                name.setText(item.textStringId)
                container.setBackgroundColor(item.color)

                container.setOnClickListener {
                    // aqui ele é uma ref. function
                    onItemClickListener.invoke(item.id)
                }
            }
        }

    }
}