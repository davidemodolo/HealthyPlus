package com.modolo.healthyplus.mealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.modolo.healthyplus.R
import com.modolo.healthyplus.mealplanner.food.Food
import com.modolo.healthyplus.mealplanner.food.FoodAdapter
import java.time.LocalDate
import java.time.LocalDateTime

class EditMealFragment(private val meal: Meal?): Fragment(), FoodAdapter.FoodListener {

    constructor(): this(null)
    private var foodListTmp = ArrayList<Food>()
    lateinit var foodName: TextInputEditText
    lateinit var foodQuantity: TextInputEditText
    lateinit var udmSpinner: Spinner
    lateinit var foodKcal: TextInputEditText
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.mealplanner_frag_edit, container, false)
        val name = view.findViewById<EditText>(R.id.title)
        val close = view.findViewById<ImageView>(R.id.close)
        val delete = view.findViewById<TextView>(R.id.btnDelete)
        val save = view.findViewById<TextView>(R.id.btnSave)
        val foodRecycler = view.findViewById<RecyclerView>( R.id.foodRecycler)
        val inputFields = view.findViewById<ConstraintLayout>(R.id.inputLayout)
        foodName = inputFields.findViewById(R.id.foodNameText)
        foodQuantity = inputFields.findViewById(R.id.quantityText)
        udmSpinner = inputFields.findViewById(R.id.spinnerUdm)
        foodKcal = inputFields.findViewById(R.id.kcalText)
        val addFood = inputFields.findViewById<TextView>(R.id.addBtn)
        if(meal!=null){
            name.setText(meal.name)
            foodListTmp = meal.foodList
        }
        foodRecycler.adapter = FoodAdapter(foodListTmp, this, requireContext())
        addFood.setOnClickListener {
            if(foodName.text.toString() != ""){
                val nameTmp = foodName.text.toString()
                val quantityTmp = if(foodQuantity.text.toString()!="") foodQuantity.text.toString().toFloat() else 0.0F
                val udmTmp = udmSpinner.selectedItem.toString()
                val kcalTmp = if(foodKcal.text.toString()!="") foodKcal.text.toString().toFloat() else 0.0F
                foodListTmp.add(Food(nameTmp, quantityTmp, udmTmp, kcalTmp))
            }
        }
        return view
    }

    override fun onFoodListener(food: Food, position: Int, longpress: Boolean) {
        foodName.setText(food.name)
        foodQuantity.setText(food.quantity.toString())
        udmSpinner.setSelection(findSpinnerElement(food.udm))
        foodKcal.setText(food.kcal.toString())
        foodListTmp.remove(food)
    }
    fun findSpinnerElement(value : String): Int{
        val udmlist = resources.getStringArray(R.array.udms_short)
        udmlist.forEachIndexed { index, s ->
            if(s == value)
                return index
        }
        return 1

    }
}