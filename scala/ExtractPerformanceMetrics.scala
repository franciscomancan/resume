import java.nio.file.Paths
import scala.collection.mutable.ListBuffer

object ExtractPerformanceMetrics extends App {

  val loc = "c:/tmp/20220105-perf.txt"

  val f = Paths.get(loc)    //.toFile()
  if(f.toFile().exists())
    println(s"File is available => ${loc}")

  val bufferedSource = scala.io.Source.fromFile(loc)
  var count = 0
  var stats = new ListBuffer[String]()
  var sum = 0
  bufferedSource.getLines().foreach{ line =>
    if(line.contains("Performance")) {
      count += 1
      val start = line.indexOf("Performance") + 12
      val stop = line.indexOf(" ", start)
      val stat = line.substring(start, stop)
      sum += stat.toInt
      stats += stat
    }
  }
  bufferedSource.close()

  println(s"Total stats = $count")
  println(s"Total execution time = $sum")
  println(s"Mean request execution time = ${sum/count}")
  println("Complete.")

}