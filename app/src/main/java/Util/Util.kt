package Util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import cr.ac.utn.beekeepersnotebook.R

const val EXTRA_MESSAGE_PERSONID = "cr.ac.utn.beekeepersnotebook.PersonId"

class Util {
    companion object {

        fun openActivity(
            context: Context,
            objClass: Class<*>,
            extraName: String = "",
            value: String? = null
        ) {
            val intent = Intent(context, objClass).apply {
                if (extraName.isNotEmpty() && value != null) {
                    putExtra(extraName, value)
                }
            }
            context.startActivity(intent)
        }

        fun parseStringToDateModern(dateString: String, pattern: String): LocalDate? {
            return try {
                val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                LocalDate.parse(dateString, formatter)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun parseStringToDateTimeModern(dateTimeString: String, pattern: String): LocalDateTime? {
            return try {
                val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                LocalDateTime.parse(dateTimeString, formatter)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun parseStringToDateLegacy(dateString: String, pattern: String): Date? {
            return try {
                val formatter = SimpleDateFormat(pattern, Locale.getDefault())
                formatter.parse(dateString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun showDialogCondition(
            context: Context,
            message: String,
            onConfirm: () -> Unit
        ) {
            val dialogBuilder = AlertDialog.Builder(context)

            dialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.TextYes)) { _, _ ->
                    onConfirm()
                }
                .setNegativeButton(context.getString(R.string.TextNo)) { dialog, _ ->
                    dialog.dismiss()
                }

            val alert = dialogBuilder.create()
            alert.setTitle(context.getString(R.string.TextTitleDialogQuestion))
            alert.show()
        }
    }
}
