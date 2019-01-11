package hu.aut.android.todorecyclerviewdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.RatingBar
import android.widget.Toast


class Rating : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val mRatingBar = findViewById<RatingBar>(R.id.star)
        val mRatingScale = findViewById<View>(R.id.tvRatingScale) as TextView
        val mFeedback = findViewById<EditText>(R.id.etFeedback)
        val mSendFeedback = findViewById<View>(R.id.btnSubmit) as Button

        mRatingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, v, b ->
            mRatingScale.text = v.toString()
            when (ratingBar.rating.toInt()) {
                1 -> mRatingScale.text = getString(R.string.verybad)
                2 -> mRatingScale.text = getString(R.string.improvement)
                3 -> mRatingScale.text = getString(R.string.good)
                4 -> mRatingScale.text = getString(R.string.great)
                5 -> mRatingScale.text = getString(R.string.awesome)
                else -> mRatingScale.text = ""
            }
        }

        mSendFeedback.setOnClickListener {
            if (mFeedback.text.toString().isEmpty()) {
                Toast.makeText(this@Rating, getString(R.string.fill_details), Toast.LENGTH_LONG).show()
            } else {
                mFeedback.setText("")
                mRatingBar.setRating(0F)
                Toast.makeText(this@Rating, getString(R.string.fill_detail), Toast.LENGTH_SHORT).show()
            }
        }


    }
}



