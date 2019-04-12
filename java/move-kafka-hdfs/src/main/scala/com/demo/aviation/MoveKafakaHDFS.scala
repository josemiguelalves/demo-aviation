package com.demo.aviation


import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent

/**
 * A Main to run Camel with MyRouteBuilder
 */
object MoveKafakaHDFS extends App {
  val conf = new SparkConf().setAppName("Demo Aviation Data").setMaster("spark://spark-master:7077").set("spark.streaming.kafka.maxRatePerPartition", "3000")

  // val conf = new SparkConf().setAppName("appName").setMaster("local[*]").set("spark.streaming.kafka.maxRatePerPartition", "100")
  val streamingContext = new StreamingContext(conf, Seconds(600))
  // val streamingContext = new StreamingContext(conf, Seconds(10))
  val spark = SparkSession.builder.config(conf).getOrCreate()
  import spark.implicits._

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "kafka2:9092,kafka3:9092,kafka1:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "demo-aviation-data",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )

  val topics = Array("flightSimulator-0", "flightSimulator-1", "flightSimulator-2", "flightSimulator-03",
    "flightSimulator-4", "flightSimulator-5", "flightSimulator-6", "flightSimulator-7", "flightSimulator-8", "flightSimulator-9")


  val stream = KafkaUtils.createDirectStream[String, String](
    streamingContext,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )

  val eventName = stream.map(record => record.value())

  eventName.foreachRDD(rddRaw => {

    if (!rddRaw.isEmpty()) {

      val messageRDD = spark.read.json(rddRaw)

      val df = messageRDD.toDF()

      val timeTimestamp = System.currentTimeMillis/1000;

      val df_date = df.withColumn("year_partition", lit(year(from_unixtime(unix_timestamp()))))
        .withColumn("month_partition", lit(month(from_unixtime(unix_timestamp()))))
        .withColumn("day_partition", lit(dayofmonth(from_unixtime(unix_timestamp()))))
        .withColumn("hour_partition", lit(hour(from_unixtime(unix_timestamp()))))
        .withColumn("year", $"year_partition")
        .withColumn("month", $"month_partition")
        .withColumn("day", $"day_partition")
        .withColumn("hour", $"hour_partition")

      val df_final = df_date.toDF(df_date.columns map (_.toLowerCase): _*)

//      df_final.show(5)

      df_final.repartition(1).write.partitionBy("year", "month", "day", "hour")
        .mode("append").format("orc").save("hdfs://namenodecm:9000/demo-aviation/sensorRawData")

    }

  })


  streamingContext.start()
  streamingContext.awaitTermination()




}

