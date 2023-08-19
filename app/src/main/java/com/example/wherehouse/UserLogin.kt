import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.wherehouse.NextActivity
import com.example.wherehouse.R
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.CaptureActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val BARCODE_SCAN_REQUEST = 1
    }

    private lateinit var barcodeEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cameraButton: Button = findViewById(R.id.cameraButton)
        barcodeEditText = findViewById(R.id.barcodeEditText)

        cameraButton.setOnClickListener {
            // Initialize the barcode scanner
            val integrator = IntentIntegrator(this@MainActivity)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
            integrator.setPrompt("Scan a barcode")
            integrator.setCameraId(0)  // Use the rear camera
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(true)
            integrator.initiateScan()
        }
    }

    // Call this method when you want to navigate to the next page
    private fun openNextPage() {
        val intent = Intent(this, NextActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null && result.contents != null) {
            val scannedBarcode = result.contents
            if ("12345678" == scannedBarcode || "12345678" == barcodeEditText.text.toString()) {
                openNextPage()
            }
        }
    }
}
