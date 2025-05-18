package rs.ac.pr.ftn.srusititrecidomaci

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class PrikazTekstaFragment : Fragment(R.layout.fragment_prikaz_teksta) {

    private var izabraniBroj: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val poljeIzbor = view.findViewById<TextView>(R.id.izabraniBroj)
        val dugmePrikazi = view.findViewById<Button>(R.id.dugmePrikazi)
        val dugmeNazad = view.findViewById<Button>(R.id.dugmeNazad)
        val rezultatText = view.findViewById<TextView>(R.id.rezultatText)
        val rezultatScrollView = view.findViewById<ScrollView>(R.id.rezultatScrollView)

        rezultatText.movementMethod = ScrollingMovementMethod.getInstance()

        // Klik na polje za izbor broja otvara dijalog
        poljeIzbor.setOnClickListener {
            val dialog = IzborBrojaDialogFragment { izabrano ->
                izabraniBroj = izabrano
                poljeIzbor.text = "Изабрани број: $izabrano"
            }
            dialog.show(parentFragmentManager, "izborBroja")
        }

        // Dugme "Прикажи"
        dugmePrikazi.setOnClickListener {
            if (izabraniBroj == null) {
                poljeIzbor.text = "Одаберите број!"
                return@setOnClickListener
            }

            val tekst = when (izabraniBroj) {
                "1" -> getString(R.string.tekst_1)
                "2" -> getString(R.string.tekst_2)
                "3" -> getString(R.string.tekst_3)
                else -> getString(R.string.nepoznat_broj)
            }

            rezultatText.text = tekst
            rezultatScrollView.visibility = View.VISIBLE
        }

        // Dugme "Назад"
        dugmeNazad.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}

