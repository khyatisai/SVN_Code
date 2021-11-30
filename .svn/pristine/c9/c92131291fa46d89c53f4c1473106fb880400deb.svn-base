package com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContentCategory
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.SRHContentListFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.item_srh_category.view.*
import android.os.Bundle
import androidx.core.content.ContextCompat


/**
 * Created by KhyatiShah on 8/16/2019.
 */
class SRHCategoryAdapter(activity: androidx.fragment.app.FragmentActivity) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var categoryList: List<SRHContentCategory>
    private var activity: androidx.fragment.app.FragmentActivity = activity

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {


        return CategoryListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_srh_category,
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val categoryViewHolder = viewHolder as CategoryListViewHolder
        categoryViewHolder.bindView(categoryList.get(position))
    }


    inner class CategoryListViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindView(category: SRHContentCategory) {
            itemView.txtCategoryName.text = category.name
            itemView.txtCategoryDesc.text = category.description_guide
            itemView.imgBackgroundImgContainer.setBackgroundResource(R.drawable.ic_cat_relationship);

            //itemView.imgBackgroundImgContainer.setImageDrawable(R.drawable.ic_cat_relationship)
            when (category.name) {
                //setting background image according to category

                activity.getString(R.string.Relationships) -> {
                    itemView.imgBackgroundImgContainer.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.ic_cat_relationship)
                    )
                    itemView.txtCategoryName.text = "Relaciones"
                    itemView.txtCategoryDesc.text = "Navegar por las relaciones con amigos, familiares y socios"
                }

                activity.getString(R.string.SexualAndReproductiveHealth) -> {
                    itemView.imgBackgroundImgContainer.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.ic_cat_reproductive)
                    )
                    itemView.txtCategoryName.text = "Salud sexual y reproductiva"
                    itemView.txtCategoryDesc.text = "Prevención del embarazo y el embarazo. Tratamiento y apoyo para la atención del VIH. Reconocer y reducir los riesgos de infección."
                }

                activity.getString(R.string.SexualityAndSexualBehaviour) -> {
                    itemView.imgBackgroundImgContainer.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.ic_cat_sexuality)
                    )
                    itemView.txtCategoryName.text = "Sexualidad y comportamiento sexual"
                    itemView.txtCategoryDesc.text = "Cómo el sexo puede hacerte sentir y ayudarte a tomar buenas decisiones."
                }

                activity.getString(R.string.SkillsForHealthAndWellbeing) -> {
                    itemView.imgBackgroundImgContainer.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.ic_cat_skills)
                    )
                    itemView.txtCategoryName.text = "Habilidades para la salud y el bienestar"
                    itemView.txtCategoryDesc.text = "Manejar la presión de los compañeros, tomar buenas decisiones, lidiar con las redes sociales y cómo encontrar apoyo"
                }

                activity.getString(R.string.HumanBodyAndDevelopment) -> {
                    itemView.imgBackgroundImgContainer.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.ic_cat_human_body)
                    )
                    itemView.txtCategoryName.text = "El cuerpo humano y el desarrollo"
                    itemView.txtCategoryDesc.text = "Tu cuerpo, cómo cambia y ayuda a ser positivo con el tuyo."
                }

                activity.getString(R.string.UnderstandingGender) -> {
                    itemView.imgBackgroundImgContainer.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.ic_cat_gender)
                    )
                    itemView.txtCategoryName.text = "Entendiendo el género"
                    itemView.txtCategoryDesc.text = "Qué es el género y cómo las opiniones sobre él afectan tu vida y las vidas de quienes te rodean."
                }

                activity.getString(R.string.ValuesRightsCultureAndSexuality) -> {
                    itemView.imgBackgroundImgContainer.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.ic_cat_value)
                    )
                    itemView.txtCategoryName.text = "Valores, Derechos, Cultura y Sexualidad"
                    itemView.txtCategoryDesc.text = "Tus derechos básicos. Desarrollando tu propio punto de vista."
                }

                activity.getString(R.string.ViolenceAndStayingSafe) -> {
                    itemView.imgBackgroundImgContainer.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.ic_cat_violence)
                    )
                    itemView.txtCategoryName.text = "Violencia y mantenerse a salvo"
                    itemView.txtCategoryDesc.text = "Qué es la violencia y cómo actuar cuando la experimenta o la ve."
                }
            }
            itemView.cardCategory.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("categoryId", category.category_id)
                bundle.putString("categoryName", itemView.txtCategoryName.text.toString())
                var frag = SRHContentListFragment()
                frag.arguments = bundle
                AppUtils.addFragment(activity, frag, true, "")
            }
        }

    }

    fun setCategoryList(categoryList: List<SRHContentCategory>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }
}