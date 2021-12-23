package file

import java.nio.file.{Files, Paths}
import scala.io.Source as src
import scala.util.{Failure, Success, Try, Using}

/**
 * File operations to save time..
 * 2021 | anf
 */
object FileOps extends App {

  class FileOps {

    def applyByLine(fname: String, f: (str: String) => String): Try[Seq[String]] =
      println(s"$fname exists ->  ${Files.exists(Paths.get(fname))}")
      Using(src.fromFile(fname)) { buff =>
        val ucLines = for line <- buff.getLines()
            //char <- line        // to work with each char
          yield
            // each line here
            f(line)

        ucLines.toSeq
      }
  }


  def testApplyByLine() =
    val func = (s: String) => s.toLowerCase()
    val upper = new FileOps().applyByLine("/revision/dataset/20211217-policy.data.flattening.csv", func)
    val lines = if (upper.isSuccess) upper.get else None
    println(lines.iterator.length)



  testApplyByLine()
}