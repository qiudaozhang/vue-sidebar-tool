import java.io.{File, PrintWriter}

import scala.io.Source


/**
 * @author 邱道长
 *         2020/11/27
 *         vue的侧边栏导航工具，第二版，读取config.js文件
 */
object MainV2 {

  def main(args: Array[String]): Unit = {
    // 空的侧边栏列表
    var bars = List[Bar]()
    // 获取路径
    val readPath = getPath(args)
    // 获取根目录
    val root = new File(readPath)
    // 解析所有的文件和目录
    for {
      f <- root.listFiles
    } {
      if (f.isFile) {
        handleFile(f)
      } else if (f.isDirectory) {
        val childrens = handleDirectory(f)
        val path = "/" + f.getName + "/"
        if (!childrens.isEmpty) {
          // 构建Bar对象
          val bar = Bar(path, childrens)
          // bar加入到SideBar
          bars = bar :: bars
        }
      }
    }
    // 最后构建好SideBar
    //    构建好内容
    val content = createSideBarJson(bars)
    //    write(content = content)
    readConfig(readPath)

  }


  /**
   * 寻找满足的结束行
   *
   * @param lines
   * @return
   */
  def findDropEndLine(lines: Iterator[String]): Int = {
    var list = List.empty[String]

    while(lines.hasNext) {
      list = lines.next::list
    }
    println("总行数：" + list.size)
    var lineNumber = 0
    var braceNum = 0
    var stop = false
    for {
      c <- list
      if !stop
    } {
      lineNumber += 1
      if (braceNum == 3) {
        // 结束
        stop = true
      }
      if (c.contains("}") && !c.contains("//") && !c.contains("*/") && !c.contains("/*")) {

        println("执行。。。")
        println(c)
        braceNum += 1
        println("当前行 :" + lineNumber)
        println("braceNum:" + braceNum)
      }

    }
//  无论如何都会多加一次，所以要先减去1
    lineNumber = lineNumber - 1
    println("当前行" + lineNumber)
    println("总行" + list.size)
    list.size - (lineNumber-1)
  }


  def readConfig(readPath: String): Unit = {
    val root = new File(readPath)
    //   root.listFiles().foreach(println)

    val ve = for {
      f <- root.listFiles
      if (f.isDirectory && f.getName.equals(".vuepress"))
    } yield f

    val vuepress = ve.take(1)(0)
    // 通过它获取config.js
    val cfg = vuepress.listFiles().filter(c => {
      c.getName.equals("config.js")
    }).take(1)(0)
    val lines = Source.fromFile(cfg).getLines()
    println("lines " + lines)
    var counter = 0
    var startLineNumber = 0
    var stop = false
    for {line <- lines
         if !stop
         } {
      counter += 1
      if (line.contains("sidebar: {") && !line.contains("//") && !line.contains("/*")) {
        startLineNumber = counter
        stop = true
      }
    }
    println("起始行：" + startLineNumber)
    //   lines 倒过来找第三个 }
    //   sidebar: { 作为删除开始
    // 从倒数第三个没有注释的 } 结束
//    println(Source.fromFile(cfg).getLines())
//    Source.fromFile(cfg).getLines().foreach(println)
    val endLine: Int = findDropEndLine(Source.fromFile(cfg).getLines()) // 之前的lines已经迭代用完了
    println("结束行:" +  endLine)

  }

  //  def findDropEndLine()

  def createSideBarJson(bars: List[Bar]): String = {
    //    var sideBarStr = List.empty[String]
    var list = List.empty[String]
    bars.foreach(c => {
      list = barJson(c) :: list
    })
    var s1 = ""
    for (e <- list) {
      if (s1.equals("")) {
        s1 +=
          s"""${e},""".stripMargin
      } else {
        s1 +=
          s"""
             |${e},""".stripMargin
      }
    }
    s1 = s1.dropRight(1)
    s1
  }


  def write(file: String = "out.txt", content: String): Unit = {
    // 输出到file
    val f = new File(file)
    if (f == null || !f.exists()) {
      // 创建文件
      f.createNewFile()

    }
    val pw = new PrintWriter(f)
    pw.write(content)
    pw.close()
  }

  def barJson(bar: Bar): String = {
    var s = bar.path + ": ["
    val tab = " " * 4
    val temp = bar.children.map(c => {
      s"""${tab}{
         |${tab * 2}title: '${c.title}',
         |${tab * 2}path: '${c.path}'
         |${tab}}""".stripMargin
    })
    var s3 = ""
    temp.foreach(t => {
      if (s3.equals("")) {
        s3 += s"""${t},""".stripMargin
      } else {
        s3 +=
          s"""
             |${t},""".stripMargin
      }
    })
    s3 = s3.dropRight(1)
    val s4 =
      s"""'${bar.path}': [
         |${s3}
         |]""".stripMargin
    s4
  }


  def handleFile(f: File) = {
    // 暂时未处理
  }

  /**
   * 处理文件夹
   *
   * @param f
   */
  def handleDirectory(f: File): List[Children] = {
    var childrens = List.empty[Children]
    if (!f.getName.equalsIgnoreCase(".vuepress")) {
      // 边栏的path
      val path = "/" + f.getName + "/"
      // 获取所有的子文件
      // 不要解析readme.md
      for (c <- f.listFiles()) {
        if (!c.getName.equalsIgnoreCase("README.md")) {
          val name = c.getName.replace(".md", "")
          val children = Children(name, name)
          childrens = children :: childrens
        }
      }
    }
    childrens
  }

  /**
   * 获取路径
   *
   * @param args 程序参数
   * @return
   */
  def getPath(args: Array[String]) = {
    if (args.isEmpty) {
      "."
    } else {
      args(0)
    }
  }
}
