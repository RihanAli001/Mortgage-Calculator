package com.rihanali001.mortgagecalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.slider.Slider
import com.rihanali001.mortgagecalculator.databinding.FragmentEmiCalculatorBinding
import java.text.NumberFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.pow

class EmiCalculatorFragment : Fragment() {
    private lateinit var binding: FragmentEmiCalculatorBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        inflater.inflate(R.layout.fragment_emi_calculator, container, false)
        binding = FragmentEmiCalculatorBinding.inflate(layoutInflater)
        binding.loanAmountEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!isNullOrEmptyOrSinglePoint(p0)){
                    if (binding.downPaymentSlider.value > p0.toString().toFloat())
                        binding.downPaymentSlider.value = floor(p0.toString().toDouble()).toFloat()
                    if (p0.toString().toFloat() >= 1f)
                        binding.downPaymentSlider.valueTo = floor(p0.toString().toDouble()).toFloat()
                }
                calculateEmi()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.loanLengthSlider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            val year = (value).toInt()/12
            val month = (value).toInt()-year*12
            binding.loanLengthCountTv.text = getString(R.string.loan_length_count, year.toString(), month.toString())
            calculateEmi()
        })
        binding.downPaymentSlider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            binding.downPaymentCountTv.text = getString(R.string.down_payment_amount, NumberFormat.getCurrencyInstance(Locale("en","in")).format(value))
            calculateEmi()
        })
        binding.loanPercentageRateEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!isNullOrEmptyOrSinglePoint(p0)){
                    if (p0.toString().toFloat() in 0f..100f) {
                        binding.percentageRateCountTv.error = null
                        calculateEmi()
                    }
                    else {
                        binding.percentageRateCountTv.error = "Must be between 0 to 100."
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) { }
        })
        updateUi()
        return binding.root
    }

    private fun updateUi() {
        binding.loanAmountEt.setText("1000000")
        binding.loanLengthSlider.value = 120f
        binding.downPaymentSlider.value = 0f
        binding.loanPercentageRateEt.setText("0.6")
    }

    private fun isNullOrEmptyOrSinglePoint(p0: CharSequence?): Boolean {
        return (p0.isNullOrEmpty() || (p0.toString() == "."))
    }

    private fun calculateEmi() {
        val amount = if (isNullOrEmptyOrSinglePoint(binding.loanAmountEt.text)) 0f else binding.loanAmountEt.text.toString().toFloat()
        val months = (binding.loanLengthSlider.value).toInt()
        val downPayment = binding.downPaymentSlider.value
        val interest = if (isNullOrEmptyOrSinglePoint(binding.loanPercentageRateEt.text)) 0f else binding.loanPercentageRateEt.text.toString().toFloat()/100
        val loan = amount-downPayment
        val emiPerMonth = (loan*interest* (1 + interest).toDouble().pow(months.toDouble()))/((1+interest).toDouble().pow(months.toDouble())-1)
        binding.resultAmount.text = getString(R.string.result_emi_amount, NumberFormat.getCurrencyInstance(Locale("en","in")).format(emiPerMonth))
    }
}