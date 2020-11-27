import java.io.{File, PrintWriter}


/**
 * @author 邱道长
 *         2020/11/27
 *         vue的侧边栏导航工具
 */
object Main {

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
    write(content = content)

  }

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
