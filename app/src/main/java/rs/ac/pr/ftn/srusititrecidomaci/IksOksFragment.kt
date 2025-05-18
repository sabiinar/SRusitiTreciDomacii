package rs.ac.pr.ftn.srusititrecidomaci

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class IksOksFragment : Fragment(R.layout.fragment_iks_oks) {

    class NewGameDialogFragment : DialogFragment() {
        interface NewGameDialogListener {
            fun onNewGameConfirmed()
        }

        private var listener: NewGameDialogListener? = null

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(requireContext())
                .setTitle("Потврда")
                .setMessage("Желите ли започети нову игру?")
                .setPositiveButton("ДА") { _, _ -> listener?.onNewGameConfirmed() }
                .setNegativeButton("НЕ") { dialog, _ -> dialog.dismiss() }
                .create()
        }

        fun setListener(listener: NewGameDialogListener) {
            this.listener = listener
        }
    }

    private lateinit var etInput: EditText
    private lateinit var btnRed: Button
    private lateinit var btnBlue: Button
    private lateinit var btnNewGame: Button
    private lateinit var btnNazad: Button
    private lateinit var fields: List<TextView>
    private val occupiedFields = mutableMapOf<Int, Int>()

    private var lastColor: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view) // Овде се позива initViews

        savedInstanceState?.let {
            val keys = it.getIntArray("occupiedKeys")
            val values = it.getIntArray("occupiedValues")
            if (keys != null && values != null) {
                for (i in keys.indices) {
                    occupiedFields[keys[i]] = values[i]
                    fields[keys[i]].setBackgroundColor(values[i])
                }
            }
        }

        btnRed.setOnClickListener {
            val color = Color.RED
            if (lastColor == color) {
                showToast("На потезу је плави играч!")
            } else {
                colorField(color)
                lastColor = color
            }
        }

        btnBlue.setOnClickListener {
            val color = ContextCompat.getColor(requireContext(), R.color.blue)
            if (lastColor == color) {
                showToast("На потезу је црвени играч!")
            } else {
                colorField(color)
                lastColor = color
            }
        }

        btnNewGame.setOnClickListener {
            val dialog = NewGameDialogFragment().apply {
                setListener(object : NewGameDialogFragment.NewGameDialogListener {
                    override fun onNewGameConfirmed() {
                        resetGame()
                    }
                })
            }
            dialog.show(childFragmentManager, "NewGameDialog")
        }

        btnNazad.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    // Иницијализација елемената
    private fun initViews(view: View) {
        etInput = view.findViewById(R.id.etInput)
        btnRed = view.findViewById(R.id.btnRed)
        btnBlue = view.findViewById(R.id.btnBlue)
        btnNewGame = view.findViewById(R.id.btnNewGame)
        btnNazad = view.findViewById(R.id.btnBack)

        fields = listOf(
            view.findViewById(R.id.p1), view.findViewById(R.id.p2), view.findViewById(R.id.p3),
            view.findViewById(R.id.p4), view.findViewById(R.id.p5), view.findViewById(R.id.p6),
            view.findViewById(R.id.p7), view.findViewById(R.id.p8), view.findViewById(R.id.p9)
        )
    }

    private fun colorField(color: Int) {
        val inputText = etInput.text.toString().trim() // Исправљено: додата заграда
        if (inputText.isEmpty()) {
            showToast("Унеси број поља!")
            return
        }

        val input = inputText.toIntOrNull() ?: run {
            showToast("Неисправан број!")
            return
        }

        if (input !in 1..9) {
            showToast("Број мора бити између 1 и 9!")
            return
        }

        val index = input - 1

        if (occupiedFields.containsKey(index)) {
            showToast("Поље је већ заузето!")
            return
        }

        fields[index].setBackgroundColor(color)
        occupiedFields[index] = color
    }

    private fun resetGame() {
        fields.forEach {
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        etInput.text.clear()
        occupiedFields.clear()
        lastColor = null
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("occupiedKeys", occupiedFields.keys.toIntArray())
        outState.putIntArray("occupiedValues", occupiedFields.values.toIntArray())
    }
}