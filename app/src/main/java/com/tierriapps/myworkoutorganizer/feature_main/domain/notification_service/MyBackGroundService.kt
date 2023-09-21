package com.tierriapps.myworkoutorganizer.feature_main.domain.notification_service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.getNumberList
import com.tierriapps.myworkoutorganizer.common.values.Constants
import com.tierriapps.myworkoutorganizer.feature_main.presenter.MainActivity
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.DivisionForUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyBackGroundService: Service() {
    // layout components
    private lateinit var remoteView: RemoteViews
    private val layout = R.id.linearLayoutNotification
    private val tvInput = R.id.textViewInput
    private val tvInputWeight = R.id.textViewInputWeight
    private val tvExerciseAndSeries = R.id.textViewExerciseAndSerie
    private val tvRepsDone = R.id.textViewRepsDone
    private val numericKeyBoard = listOf(
        R.id.button0, R.id.button1, R.id.button2,
        R.id.button3, R.id.button4, R.id.button5,
        R.id.button6, R.id.button7, R.id.button8, R.id.button9)
    private val buttonSpace = R.id.buttonSpace
    private val buttonDel = R.id.buttonDel
    private val buttonOk = R.id.buttonOk
    private val buttonOpenInApp = R.id.buttonopenInApp
    private val buttonNextExercise = R.id.imageViewNextExercise
    private val buttonLastExercise = R.id.imageViewLastExercise

    // true if is reps, false if is weight
    private var inputSelected = true
    private var hasValue = false
    private lateinit var divisionForUi: DivisionForUi
    private var actualExerciseId = 0
    private var actualRepId = 0
    private var input = ""
    private var inputWeight = ""
    private var bigTextInput = ""

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> start(intent)
            Actions.STOP.toString() -> stop()
            Actions.DIGIT_PRESSED.toString() -> updateData(intent.extras?.getString("digit", ""))
            Actions.OK_PRESSED.toString() -> updateDataWithOk()
            Actions.OPEN_IN_APP.toString() -> openTheApp()
            Actions.DEL_PRESSED.toString() -> updateData("DELETE")
            Actions.SELECT_INPUT.toString() -> selectInput()
            Actions.SELECT_WEIGHT.toString()  -> selectWeight()
            Actions.NEXT_EXERCISE.toString() -> goToNextExercise()
            Actions.LAST_EXERCISE.toString() -> goToLastExercise()
            Actions.FRAGMENT_ASKS_FOR_DATA.toString() -> giveDataToTheFragment()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    enum class Actions{
        START, STOP, DIGIT_PRESSED, OK_PRESSED, OPEN_IN_APP, DEL_PRESSED,
        SELECT_INPUT, SELECT_WEIGHT, NEXT_EXERCISE, LAST_EXERCISE,
        NAVIGATE_TO_THE_FRAGMENT, FRAGMENT_ASKS_FOR_DATA, GIVE_DATA_TO_FRAGMENT
    }

    private  fun start(intent: Intent?){
        remoteView = RemoteViews(packageName, R.layout.notification_do_training)
        val gson = intent?.extras?.getString("division")?: return
        val type = object : TypeToken<DivisionForUi>(){}.type
        val division = Gson().fromJson(gson, type) as DivisionForUi
        divisionForUi = division
        hasValue = true
        val color = ContextCompat.getColor(this, divisionForUi.colorForBackGround())
        inputWeight = divisionForUi.exercises[actualExerciseId].weight.toString()
        remoteView.setInt(layout, "setBackgroundColor", color)
        setButtonActions()
        startForeground(1, notification.build())
        notificationManager = this.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
        updateNotification()
    }

    private fun stop(){
        stopSelf()
    }

    private val notification = NotificationCompat
        .Builder(this, Constants.NOTIFICATION_DO_TRAINING_CHANNEL)
            .setSmallIcon(R.drawable.baseline_weight)
            .setStyle(NotificationCompat.BigTextStyle())
    private lateinit var notificationManager: NotificationManager
    private fun updateNotification(){
        val actualExercise = divisionForUi.exercises[actualExerciseId]

        remoteView.setTextViewText(tvInput, input)
        remoteView.setTextViewText(tvInputWeight, inputWeight)
        remoteView.setTextViewText(tvRepsDone, bigTextInput)
        val text = "${actualExercise.name}   -   ${actualExercise.numOfSeries} series"
        remoteView.setTextViewText(tvExerciseAndSeries, text)
        notification.setCustomBigContentView(remoteView)
        notificationManager.notify(1, notification.build())
    }

    private fun updateData(digit: String?){
        if (digit == null || digit == "" || digit == "null"){ return }
        if (inputSelected){
            if (digit == "DELETE" && input.isNotEmpty()){
                input = input.removeRange(input.lastIndex..input.lastIndex)
            }else if(digit != "DELETE"){
                input += digit
            }
        }else {
            if (digit == "DELETE" && inputWeight.isNotEmpty()){
                inputWeight = inputWeight.removeRange(inputWeight.lastIndex..inputWeight.lastIndex)
            } else if(digit.toIntOrNull() != null){
                inputWeight += digit
            }
            divisionForUi.exercises[actualExerciseId].weight = inputWeight.toIntOrNull()
        }
        updateNotification()
    }

    private fun updateDataWithOk(){
        val repsDone = input.getNumberList()
        input = ""
        val actualExercise = divisionForUi.exercises[actualExerciseId]
        if (repsDone.isEmpty() || actualExercise.repsDone[actualRepId].isNotEmpty()){return}
        actualExercise.repsDone[actualRepId] = repsDone.toMutableList()
        println(divisionForUi.exercises.toString())
        if (actualRepId < actualExercise.repsDone.lastIndex){
            actualRepId ++
        }else {
            goToNextExercise()
            updateNotification()
            return
        }
        bigTextInput += listToUiText(repsDone)
        updateNotification()
    }
    private fun listToUiText(list: List<Int>): String{
        if (list.isEmpty()){ return ""}
        var text = "("
        for(n in list){
            text += " - $n"
        }
        text = text.replaceFirst(" -", "")
        return text+" )"
    }

    private fun goToNextExercise(){
        if (actualExerciseId < divisionForUi.exercises.lastIndex){
            actualExerciseId++
            val actualExercise = divisionForUi.exercises[actualExerciseId]
            for(i in actualExercise.repsDone.withIndex()){
                if (i.value.isEmpty()){
                    actualRepId = i.index
                    break
                }else if(i.index == actualExercise.repsDone.lastIndex){
                    actualRepId = 0
                }
            }
            inputWeight = actualExercise.weight.toString()
            var text = ""
            for(i in actualExercise.repsDone){
                text += listToUiText(i)
            }
            bigTextInput = text
            updateNotification()
        }
    }

    private fun goToLastExercise(){
        if (actualExerciseId > 0){
            actualExerciseId--
            val actualExercise = divisionForUi.exercises[actualExerciseId]
            for(i in actualExercise.repsDone.withIndex()){
                if (i.value.isEmpty()){
                    actualRepId = i.index
                    break
                }else if(i.index == actualExercise.repsDone.lastIndex){
                    actualRepId = 0
                }
            }
            inputWeight = actualExercise.weight.toString()
            var text = ""
            for(i in actualExercise.repsDone){
                text += listToUiText(i)
            }
            bigTextInput = text
            updateNotification()
        }
    }

    private fun selectInput(){
        inputSelected = true
        remoteView.setInt(tvInput, "setBackgroundResource", R.drawable.shape_exercises_container_selected)
        remoteView.setInt(tvInputWeight, "setBackgroundResource", R.drawable.shape_exercises_container)
        updateNotification()
    }

    private fun selectWeight(){
        inputSelected = false
        remoteView.setInt(tvInputWeight, "setBackgroundResource", R.drawable.shape_exercises_container_selected)
        remoteView.setInt(tvInput, "setBackgroundResource", R.drawable.shape_exercises_container)
        updateNotification()
    }

    private fun openTheApp(){
        val intent = Intent(this, MainActivity::class.java)
        intent.action = Actions.NAVIGATE_TO_THE_FRAGMENT.toString()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        pendingIntent.send()
    }

    private fun giveDataToTheFragment(){
        print("someone asks for data")
        if (!hasValue) return
        val gsonDivisionForUi = Gson().toJson(divisionForUi)
        Intent().also {
            it.action = Actions.GIVE_DATA_TO_FRAGMENT.toString()
            it.putExtra("divisionForUi", gsonDivisionForUi)
            sendBroadcast(it)
        }
    }

    private fun setButtonActions(){
        for((key, bt) in numericKeyBoard.withIndex()){
            Intent(this, this::class.java).also {
                it.action = Actions.DIGIT_PRESSED.toString()
                it.putExtra("digit", "$key")
                val btPendingIntent = PendingIntentCompat.getService(this, key, it,
                    PendingIntent.FLAG_UPDATE_CURRENT, true)
                remoteView.setOnClickPendingIntent(bt, btPendingIntent)
            }
        }
        Intent(this, this::class.java).also {
            it.action = Actions.DIGIT_PRESSED.toString()
            it.putExtra("digit", "-")
            val spacePendingIntent = PendingIntentCompat.getService(this, 10, it,
                PendingIntent.FLAG_UPDATE_CURRENT, true)
            remoteView.setOnClickPendingIntent(buttonSpace, spacePendingIntent)
        }
        Intent(this, this::class.java).also {
            it.action = Actions.OK_PRESSED.toString()
            val okPendingIntent = PendingIntentCompat.getService(this, 11, it,
                PendingIntent.FLAG_UPDATE_CURRENT, true)
            remoteView.setOnClickPendingIntent(buttonOk, okPendingIntent)
        }
        Intent(this, this::class.java).also {
            it.action = Actions.OPEN_IN_APP.toString()
            val appPendingIntent = PendingIntentCompat.getService(this, 12, it,
                PendingIntent.FLAG_UPDATE_CURRENT, true)
            remoteView.setOnClickPendingIntent(buttonOpenInApp, appPendingIntent)
        }
        Intent(this, this::class.java).also {
            it.action = Actions.SELECT_INPUT.toString()
            val appPendingIntent = PendingIntentCompat.getService(this, 13, it,
                PendingIntent.FLAG_UPDATE_CURRENT, true)
            remoteView.setOnClickPendingIntent(tvInput, appPendingIntent)
        }
        Intent(this, this::class.java).also {
            it.action = Actions.SELECT_WEIGHT.toString()
            val appPendingIntent = PendingIntentCompat.getService(this, 14, it,
                PendingIntent.FLAG_UPDATE_CURRENT, true)
            remoteView.setOnClickPendingIntent(tvInputWeight, appPendingIntent)
        }
        Intent(this, this::class.java).also {
            it.action = Actions.NEXT_EXERCISE.toString()
            val appPendingIntent = PendingIntentCompat.getService(this, 15, it,
                PendingIntent.FLAG_UPDATE_CURRENT, true)
            remoteView.setOnClickPendingIntent(buttonNextExercise, appPendingIntent)
        }
        Intent(this, this::class.java).also {
            it.action = Actions.LAST_EXERCISE.toString()
            val appPendingIntent = PendingIntentCompat.getService(this, 16, it,
                PendingIntent.FLAG_UPDATE_CURRENT, true)
            remoteView.setOnClickPendingIntent(buttonLastExercise, appPendingIntent)
        }
        Intent(this, this::class.java).also {
            it.action = Actions.DEL_PRESSED.toString()
            val appPendingIntent = PendingIntentCompat.getService(this, 17, it,
                PendingIntent.FLAG_UPDATE_CURRENT, true)
            remoteView.setOnClickPendingIntent(buttonDel, appPendingIntent)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}
