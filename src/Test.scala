import java.util.stream.Collectors

/**
 * @author 邱道长
 *         2020/11/27
 */
object Test {

  def main(args: Array[String]): Unit = {

    removeNullLine()
  }

  def removeNullLine(): Unit = {
    val s =
      s"""
         |我的天空
         |哈哈""".stripMargin

//    val r = s.lines().filter(!_.isBlank).collect(Collectors.toList)
//    println("==")
//    println(r)
    var r2 = ""
    s.lines().filter(!_.isBlank).forEach(c => {r2 = r2+c})
//    s.lines().filter(!_.isBlank).forEach(println)
    println(r2)
  }

}
