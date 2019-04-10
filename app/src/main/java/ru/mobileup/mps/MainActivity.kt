package ru.mobileup.mps

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import ru.mobileup.common.createApplicationScreenMessage
import ru.mobileup.common.startCountdown

class MainActivity : AppCompatActivity() {

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        button.setOnClickListener {
            job?.cancel()
            job = startCountdown {
                label.text = if (it > 0) {
                    it.toString()
                } else {
                    createApplicationScreenMessage()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        button.setOnClickListener(null)
        job?.cancel()
    }
}
