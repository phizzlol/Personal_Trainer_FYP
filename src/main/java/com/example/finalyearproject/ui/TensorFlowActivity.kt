package com.example.finalyearproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalyearproject.R
import kotlinx.android.synthetic.main.activity_tensor_flow.*
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

@Suppress("DEPRECATION")
class TensorFlowActivity : AppCompatActivity() {

    private lateinit var tflite : Interpreter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tensor_flow)
    try{
        tflite = Interpreter(loadModelFile())
    }catch(e: Exception){

    }
        inferButton.setOnClickListener{
            if(edit_text_value1.text!!.isEmpty() || edit_text_value2.text!!.isEmpty() || edit_text_value3.text!!.isEmpty()||
                edit_text_value4.text!!.isEmpty() || edit_text_value5.text!!.isEmpty()){
                input_value1.error = getString(R.string.error_field_required)
                input_value2.error = getString(R.string.error_field_required)
                input_value3.error = getString(R.string.error_field_required)
                input_value4.error = getString(R.string.error_field_required)
                input_value5.error = getString(R.string.error_field_required)
                Toast.makeText(this, "All Fields Required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
val prediction : Float = doInference(edit_text_value1.text.toString(), edit_text_value2.text.toString(),
    edit_text_value3.text.toString(),edit_text_value4.text.toString(),edit_text_value5.text.toString())
            val inferenceValue = prediction.toDouble()
            val inferenceTransformed = inferenceValue / 100
            inferenceText.text = inferenceTransformed.toString()
        }

    }
    private fun doInference(inputString:String, inputString2 :String, inputString3 :String, inputString4 :String, inputString5 :String):Float {
        //input shape is [1]
        val inputVal = FloatArray(6)
        inputVal[0] = java.lang.Float.valueOf(inputString)
        inputVal[1] = java.lang.Float.valueOf(inputString2)
        inputVal[3] = java.lang.Float.valueOf(inputString3)
        inputVal[4] = java.lang.Float.valueOf(inputString4)
        inputVal[5] = java.lang.Float.valueOf(inputString5)
        //output shape is [1][1]
        val outputval = Array(1) {FloatArray(1)}
        //run inference passing input shape
        tflite.run(inputVal, outputval)
        //inferred value at [0][0]
        //return
        return outputval[0][0]
    }
    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = this.assets.openFd("converted_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
