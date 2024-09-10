import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class SmartDevice(val name: String, val category: String){

    var deviceStatus = "online"
        protected set(value) { //property будет доступно только в дочерних классах
            field = value //такое написание set value = default implementation
        }

    //можно переопределять не только методы, но и функции
    open val deviceType = "unknown"

    //open так как методы перезаписываются в дочерних классах
    open fun turnOn() {

    }

    open fun turnOff() {

    }
}

class SmartTvDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType: String = "Smart TV"

        //by delegate заменяет конструкцию с set. class ниже
        private var speakerVolume by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)
//            set(value) {
//                if(value in 0..100) field = value
//            }

        private var channelNumber by RangeRegulator(initialValue = 1, minValue = 0, maxValue = 200)
//            set(value) {
//                if (value in 0 .. 200) field = value
//            }

    fun increaseSpeakerVolume() {
        speakerVolume++
        println("Speaker volume increased to $speakerVolume")
    }

    fun nextChannel() {
        channelNumber++
        println("Channel number increased to $channelNumber")
    }

    override fun turnOn() {
        super.turnOn() //обращение к методу из супер класса
        deviceStatus = "on"
        println("$name is turned on. Speaker volume is set to $speakerVolume and channel number is " +
                "set to $channelNumber.")
    }

    override fun turnOff() {
        super.turnOff()
        deviceStatus = "off"
        println("$name turned off")
    }
    }

class SmartLightDevice(deviceName: String, deviceCategory: String) :
        SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType: String = "Smart Light"

            private var brightnessLevel by RangeRegulator(initialValue = 0, minValue = 0, maxValue = 100)
//                set(value) {
//                    if (value in 0..100) field = value
//                }

        fun increaseBrightness() {
            brightnessLevel++
            println("Brightness increased to $brightnessLevel")
        }

        override fun turnOn() {
            super.turnOn()
            deviceStatus = "on"
            brightnessLevel = 2
            println("$name turned on. The brightness level is $brightnessLevel")
        }

        override fun turnOff() {
            super.turnOff()
            deviceStatus = "off"
            brightnessLevel = 0
            println("Smart light turned off")
        }
        }

//класс ниже отражает модель HAS-A
class SmartHome(
    val smartTvDevice: SmartTvDevice,
    val smartLightDevice: SmartLightDevice) {

    var deviceTurnOnCount = 0
        private set

    fun turnOnTv() {
        deviceTurnOnCount++
        smartTvDevice.turnOn()
    }

    fun turnOffTv() {
        smartTvDevice.turnOff()
    }

    fun increaseTvVolume() {
        smartTvDevice.increaseSpeakerVolume()
    }

    fun changeTvChannelToNext() {
        smartTvDevice.nextChannel()
    }

    fun turnOnLight() {
        deviceTurnOnCount++
        smartLightDevice.turnOn()
    }

    fun turnOffLight() {
        smartLightDevice.turnOff()
    }

    fun increaseLightBrightness() {
        smartLightDevice.increaseBrightness()
    }

    fun turnOffAllDevices() {
        turnOffTv()
        turnOffLight()
    }
}

//delegate
class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int,
) : ReadWriteProperty<Any?, Int> {

    var fieldData = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return fieldData
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}

fun main() {
    var smartDevice: SmartDevice = SmartTvDevice("Android TV", "Enertainment")
    smartDevice.turnOn()

    smartDevice = SmartLightDevice("Google Light", "Utility")
    smartDevice.turnOn()
}