package glucosense.org;

class SensorData {
    var id: Int = 0
    var sensorID: String = ""
    var version: String = ""
    var timeLeft: Int = 0
    var lastScanSensorTime: Int = 0
    var startDate: String = ""
    var expireDate: String = ""

    constructor() { }

    constructor(id: Int, sensor_id: String, version: String, time_left: Int,
                last_scan_sensor_time: Int, start_date: String, expire_date: String) {
        this.id = id
        this.sensorID = sensor_id
        this.version = version
        this.timeLeft = time_left
        this.lastScanSensorTime = last_scan_sensor_time
        this.startDate = start_date
        this.expireDate = expire_date

    }

    constructor(sensor_id: String, version: String, time_left: Int, last_scan_sensor_time: Int,
                start_date: String, expire_date: String) {
        this.sensorID = sensor_id
        this.version = version
        this.timeLeft = time_left
        this.lastScanSensorTime = last_scan_sensor_time
        this.startDate = start_date
        this.expireDate = expire_date
    }
}