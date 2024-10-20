package com.antares.esp8266_sensor.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.antares.esp8266_sensor.App
import com.antares.esp8266_sensor.R
import com.antares.esp8266_sensor.databinding.ActivityMainBinding
import javax.inject.Inject
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        val loadingDialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_loading)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchData()
            binding.swipeRefresh.isRefreshing = false
            loadingDialog.show()
        }

        viewModel.fetchData()
        viewModel.antares.observe(this) { content ->
            loadingDialog.dismiss()
            content?.let {
                binding.progressHumidity.visibility = View.GONE
                binding.progressPeople.visibility = View.GONE
                binding.progressOutputFuzzy.visibility = View.GONE
                binding.progressTemperature.visibility = View.GONE
                binding.progressTemperatureAc.visibility = View.GONE

                binding.txtHumidity.visibility = View.VISIBLE
                binding.txtHumidityHeader.visibility = View.VISIBLE
                binding.txtPeople.visibility = View.VISIBLE
                binding.txtPeopleHeader.visibility = View.VISIBLE
                binding.txtTemperatureAc.visibility = View.VISIBLE
                binding.txtTemperatureAcHeader.visibility = View.VISIBLE
                binding.txtOutputFuzzy.visibility = View.VISIBLE
                binding.txtOutputFuzzyHeader.visibility = View.VISIBLE
                binding.txtTemperature.visibility = View.VISIBLE
                binding.txtTemperatureHeader.visibility = View.VISIBLE

                //for-test-change

                content.countPeople?.let { count ->
                    binding.txtPeople.text = count.count.toString()
                }

                content.temperature?.let { temperature ->
                    binding.edtTemperature.visibility = View.VISIBLE
                    binding.btnTemperature.visibility = View.VISIBLE

                    binding.txtTemperature.text = temperature.temperature.toString()
                    binding.txtTemperatureAc.text = temperature.temperatureAc.toString()
                    binding.txtHumidity.text = temperature.humidity.toString()
                    binding.txtOutputFuzzy.text = temperature.outputFuzzy.toString()

                    binding.edtTemperature.setText(temperature.temperature.roundToInt().toString())
                    binding.btnTemperature.setOnClickListener {
                        val edtTemperature = binding.edtTemperature.text.toString()
                        when {
                            edtTemperature.isEmpty() -> binding.edtTemperature.error = "Must be filled"
                            else -> {
                                viewModel.updateTemperature(edtTemperature.toInt())
                            }
                        }
                    }
                }

                content.switch?.let { switchContent ->
                    binding.btnSwitch.visibility = View.VISIBLE
                    if (switchContent.switch) {
                        binding.btnSwitch.text = "Turn OFF"
                        binding.btnSwitch.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
                    } else {
                        binding.btnSwitch.text = "Turn ON"
                        binding.btnSwitch.backgroundTintList = ContextCompat.getColorStateList(this, R.color.green)
                    }

                    binding.btnSwitch.setOnClickListener {
                        if (switchContent.switch) {
                            viewModel.updateSwitch(false)
                        } else {
                            viewModel.updateSwitch(true)
                        }
                    }
                }
            }
        }
    }
}