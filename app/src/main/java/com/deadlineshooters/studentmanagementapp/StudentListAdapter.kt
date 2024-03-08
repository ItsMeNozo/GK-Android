package com.deadlineshooters.studentmanagementapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentListAdapter(private val context: MainActivity, private var studentList: List<Student>) : RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {

    private val drawableCache = LruCache<String, Drawable>(100)
    // Define the listener interface
    interface OnItemClickListener {
        fun onItemClick(student: Student)
    }

    fun updateStudents(newStudents: List<Student>) {
        this.studentList = newStudents
        notifyDataSetChanged()
    }

    // Variable for the listener
    var listener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var fullName: TextView = itemView.findViewById(R.id.tv_fullname)
        var className: TextView = itemView.findViewById(R.id.tv_class)
        var bdGender: TextView = itemView.findViewById(R.id.tv_bd_gender)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener?.onItemClick(studentList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.student_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentStudent = studentList[position]
        "${currentStudent.firstName} ${currentStudent.lastName}".also { holder.fullName.text = it }
        holder.className.text = currentStudent.className
        "${currentStudent.birthday} - ${currentStudent.gender}".also { holder.bdGender.text = it }

        // Get the drawable from the cache
        var drawable = drawableCache.get(currentStudent.id)

        // If the drawable is not in the cache, create it and add it to the cache
        if (drawable == null) {
            val text = "${currentStudent.firstName.first()}${currentStudent.lastName.first()}"
            val bitmap = Bitmap.createBitmap(120, 120, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.textSize = 60f
            paint.textAlign = Paint.Align.CENTER
            val xPos = (canvas.width / 2).toFloat()
            val yPos = (canvas.height / 2 - (paint.descent() + paint.ascent()) / 2).toFloat()
            canvas.drawText(text, xPos, yPos, paint)
            drawable = BitmapDrawable(context.resources, bitmap)
            drawableCache.put(currentStudent.id, drawable)
        }

        // Set the drawable
        holder.imageView.setImageDrawable(drawable)
    }



    override fun getItemCount(): Int {
        return studentList.size
    }
}
