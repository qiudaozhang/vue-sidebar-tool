package test

import java.io.File
import java.nio.file.Paths
/**
 * @author 邱道长
 *         2020/11/28
 */
object Sample {

  def main(args: Array[String]): Unit = {

    val path = Paths.get("").toAbsolutePath
    println(path)
    val f = new java.io.File(path.toString)
    println(f.listFiles())
//    val f = new java.io.File(".")
//    println(f.getAbsolutePath)
//    val files = f.listFiles().filter(_.isFile)
//    val result = files.sortBy(- _.lastModified())
//    result.foreach(c => {
//      print("文件名：" + c.getName)
//      println("修改时间：" + c.lastModified())
//    })
  }



}
