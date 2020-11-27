import java.util.stream.Collectors

/**
 * @author 邱道长
 *         2020/11/27
 */
object Test {

  def main(args: Array[String]): Unit = {


    val l = List.empty[String]
//    "a" "b" "c"
//    val result = List("a","b","c")
    val result = List.apply("a","b","c")
//    val result =  "a"::"b" ::"c"::l
//    val result = l.::("c").::("b").::("a")
    println(result)
  }

}
