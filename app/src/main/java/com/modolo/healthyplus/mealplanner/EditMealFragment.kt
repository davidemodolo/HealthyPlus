package com.modolo.healthyplus.mealplanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.modolo.healthyplus.R
import com.modolo.healthyplus.mealplanner.food.Food
import com.modolo.healthyplus.mealplanner.food.FoodAdapter

class EditMealFragment : Fragment(), FoodAdapter.FoodListener {

    lateinit var mealPassed: Meal
    private var foodListTmp = ArrayList<Food>()


    lateinit var foodName: TextInputEditText
    lateinit var foodQuantity: TextInputEditText
    lateinit var udmSpinner: Spinner
    lateinit var foodRecycler: RecyclerView
    lateinit var foodKcal: TextInputEditText
    lateinit var nameEdit: EditText
    private lateinit var viewModel: MealsSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.mealplanner_frag_edit, container, false)

        nameEdit = view.findViewById(R.id.title)

        val delete = view.findViewById<TextView>(R.id.btnDelete)
        val save = view.findViewById<TextView>(R.id.btnSave)

        foodRecycler = view.findViewById(R.id.foodRecycler)
        val inputFields = view.findViewById<ConstraintLayout>(R.id.inputLayout)
        foodName = inputFields.findViewById(R.id.foodNameText)
        foodQuantity = inputFields.findViewById(R.id.quantityText)
        udmSpinner = inputFields.findViewById(R.id.spinnerUdm)
        foodKcal = inputFields.findViewById(R.id.kcalText)
        val addFood = inputFields.findViewById<TextView>(R.id.addBtn)
        addFood.setOnClickListener {
            addFood.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
            if (foodName.text.toString() != "") {
                val nameTmp = foodName.text.toString()
                val quantityTmp =
                    if (foodQuantity.text.toString() != "") foodQuantity.text.toString()
                        .toFloat() else 0.0F
                val udmTmp = udmSpinner.selectedItem.toString()
                val kcalTmp =
                    if (foodKcal.text.toString() != "") foodKcal.text.toString().toFloat() else 0.0F
                foodListTmp.add(Food(nameTmp, quantityTmp, udmTmp, kcalTmp))
                foodRecycler.adapter = FoodAdapter(ArrayList(foodListTmp), this, requireContext())

                foodName.setText("")
                foodQuantity.setText("")
                foodKcal.setText("")
            }
        }

        delete.setOnClickListener {
            delete.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
            viewModel.removeMeal(mealPassed.id)
            findNavController().navigateUp()
        }

        save.setOnClickListener {
            mealPassed.name = nameEdit.text.toString()
            mealPassed.foodList = foodListTmp
            Log.i("devdebug", "editMeal: save pressed")
            viewModel.updateMeal(mealPassed)
            save.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
            findNavController().navigateUp()
        }

        //chiudi quando si preme la X senza salvare
        val close = view.findViewById<ImageView>(R.id.close)
        close.setOnClickListener {
            viewModel.resetEdit()
            findNavController().navigateUp()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MealsSharedViewModel::class.java)
        mealPassed = viewModel.getEdit()
        Log.i("devdebug", "editMeal received: $mealPassed")
        nameEdit.setText(mealPassed.name)
        foodListTmp = ArrayList(mealPassed.foodList)
        foodRecycler.adapter = FoodAdapter(ArrayList(foodListTmp), this, requireContext())

    }

    override fun onFoodListener(food: Food, position: Int, longpress: Boolean) {
        foodName.setText(food.name)
        foodQuantity.setText(food.quantity.toString())
        udmSpinner.setSelection(findSpinnerElement(food.udm))
        foodKcal.setText(food.kcal.toString())
        foodListTmp.remove(food)
        foodRecycler.adapter = FoodAdapter(ArrayList(foodListTmp), this, requireContext())
    }

    private fun findSpinnerElement(value: String): Int {
        val udmlist = resources.getStringArray(R.array.udms_short)
        udmlist.forEachIndexed { index, s ->
            if (s == value)
                return index
        }
        return 0
    }
}