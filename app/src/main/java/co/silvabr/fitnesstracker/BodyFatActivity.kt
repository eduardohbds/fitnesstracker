package co.silvabr.fitnesstracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import co.silvabr.fitnesstracker.model.Calc

class BodyFatActivity : AppCompatActivity() {

    private lateinit var editWaist: EditText
    private lateinit var editNeck: EditText
    private lateinit var editHeight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_fat)

        editWaist = findViewById(R.id.edit_bf_waist)
        editNeck = findViewById(R.id.edit_bf_neck)
        editHeight = findViewById(R.id.edit_bf_height)
        val btnSend: Button = findViewById(R.id.btn_bf_send)

        btnSend.setOnClickListener {
            if (!validate()) {
                Toast.makeText(this, R.string.fields_messages, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val waist = editWaist.text.toString().toInt()
            val neck = editNeck.text.toString().toInt()
            val height = editHeight.text.toString().toInt()

            val result = calculateBodyFatPercentage(
                waist, neck, height
            )
            Log.d("Teste", "resultado: $result")

            AlertDialog.Builder(this)
                .setMessage(getString(R.string.tmb_response, result))
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    // aqui vai rodar depois do click
                }
                .setNegativeButton(R.string.save) { dialog, which ->
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()

                        val updateId = intent.extras?.getInt("updateId")
                        if (updateId != null) {
                            dao.update(Calc(id = updateId, type = "bf", res = result))
                        } else {
                            dao.insert(Calc(type = "bf", res = result))
                        }

                        runOnUiThread {
                            openListActivity()
                        }
                    }.start()

                }
                .create()
                .show()

            val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            finish()
            openListActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openListActivity() {
        val intent = Intent(this, ListCalcActivity::class.java)
        intent.putExtra("type", "bf")
        startActivity(intent)
    }

    private fun calculateBodyFatPercentage(waist: Int, neck: Int, height: Int): Double {
        // peso / (altura * altura)
        return 0.29288 * (waist - neck) - 0.0005 * ((waist - neck) * (waist - neck)) + 0.15845 * height - 5.76377
    }

    private fun validate(): Boolean {
        return (editWaist.text.toString().isNotEmpty()
                && editHeight.text.toString().isNotEmpty()
                && editNeck.text.toString().isNotEmpty()
                && !editWaist.text.toString().startsWith("0")
                && !editHeight.text.toString().startsWith("0")
                && !editNeck.text.toString().startsWith("0")
                )
    }
}