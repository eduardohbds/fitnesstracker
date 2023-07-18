package co.silvabr.fitnesstracker;

import co.silvabr.fitnesstracker.model.Calc

interface OnListClickListener {
	fun onClick(id: Int, type: String)
	fun onLongClick(position: Int, calc: Calc)
}