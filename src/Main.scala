import java.io.File

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
    createSideBarJson(bars)

  }

  def createSideBarJson(bars: List[Bar]) = {
    //    var sideBarStr = List.empty[String]
    bars.foreach(barJson)
  }


  /*
  '/blog/': [
                {
                    title: '博客1',
                    path: 'blog1'
                },
                {
                    title: '博客2',
                    path: 'blog2'
                }
            ]
   */
  def barJson(bar: Bar) = {
    var s = bar.path + ": ["
    val tab = " " * 4
    val doubleTab = tab * 2
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
    var s4 =
    s"""'${bar.path}': [
       |${s3}
       |]""".stripMargin

    println(s4)
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
      for (c <- f.listFiles()) {
        val name = c.getName.replace(".md", "")
        val children = Children(name, name)
        //        println(children)
        childrens = children :: childrens
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
